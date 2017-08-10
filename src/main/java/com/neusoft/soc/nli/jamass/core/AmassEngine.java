package com.neusoft.soc.nli.jamass.core;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.bean.DevPatternInfo;
import com.neusoft.soc.nli.jamass.bean.EventStatus;
import com.neusoft.soc.nli.jamass.bean.ProtocolType;
import com.neusoft.soc.nli.jamass.channel.forward.Forward;
import com.neusoft.soc.nli.jamass.channel.forward.ForwardFactory;
import com.neusoft.soc.nli.jamass.channel.identify.DevTypeIdentifier;
import com.neusoft.soc.nli.jamass.source.ICollectSource;
import com.neusoft.soc.nli.jamass.source.TcpNettySource;
import com.neusoft.soc.nli.jamass.source.UdpNettySource;
import com.neusoft.soc.nli.jamass.source.UdpSocketSource;
import com.neusoft.soc.nli.jamass.util.KafkaUtil;
import com.neusoft.soc.nli.jamass.util.RegularExpressionUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by luyb on 2017/6/5.
 */
public class AmassEngine {
    /**
     * ��־��
     */
    private static Logger logger = LogManager.getLogger(AmassEngine.class);
    /**
     * ���������̳߳�
     */
    private ThreadPoolExecutor receiverPool;
    /**
     * ��־ʶ���̳߳�
     */
    private ThreadPoolExecutor identifyPool;
    /**
     * �ռ���־�̳߳�
     */
    private ThreadPoolExecutor parsePool;
    /**
     * ������־�̳߳�
     */
    private ThreadPoolExecutor sinkPool;
    /**
     * ����Դ�б�
     */
    private Map<String,ICollectSource> sourceMap = new HashMap<>();
    /**
     *
     */
    public Map<String,Forward> sinkMap = new HashMap<>();
    /**
     * ��־����
     */
    private Queue<AmassEvent> unknowLogQueue = new LinkedBlockingQueue<>();
    /**
     * �¼�����
     */
    private Queue<AmassEvent> eventQueue = new LinkedBlockingQueue<>();

    /**
     * ʶ���¼��������
     */
    private LinkedBlockingQueue<AmassEvent> eventIdentifyQueue = new LinkedBlockingQueue<>();

    /**
     * �����¼��������
     */
    private LinkedBlockingQueue<AmassEvent> eventParseQueue = new LinkedBlockingQueue<>();

    /**
     * �Զ������������
     */
    private RegularExpressionUtil regularExpressionUtil;

    private KafkaUtil kafkaUtil;

    private Map<String,Set<DevPatternInfo>> ipDevMap;

    public AtomicInteger count = new AtomicInteger();

    private AmassEngine(){

    }
    private static AmassEngine amassEngine = new AmassEngine();

    public static AmassEngine getInstance(){
        if(amassEngine == null){
            amassEngine = new AmassEngine();
        }
        return amassEngine;
    }

    /**
     * JAmass�����ʼ��
     */
    public void initAmassEngine(){
        initJamassSources();
        initJamassSinks();
        initThreadPool();
        initIpDevMap();
        regularExpressionUtil = RegularExpressionUtil.getInstance();
        regularExpressionUtil.initData();
        this.setKafkaUtil(KafkaUtil.getInstance(
                AmassConfigration.getKafkaConfig().get(AmassConstant.SINK_KAFKASINK_CONFIG_SERVER_LIST_NAME)
        ));
        this.kafkaUtil.initKafkaClient();
    }

    /**
     * ��ʼ������source
     */
    public void initJamassSources(){
        Set<String> set = AmassConfigration.getSources("NettyUdp:514:SysLog");
        if(set.isEmpty()){
            //source ��ʼ���쳣
            logger.error("source ��ʼ���쳣,û�л�ȡ����Ӧ��source����");
            System.exit(0);
        }
        for(String sourceConfig : set){
            //����source
            String workMode = sourceConfig.split(":")[0];
            //���ն˿�
            int port = Integer.parseInt(sourceConfig.split(":")[1]);
            //����Э��
            String protocol = sourceConfig.split(":")[2];
            ICollectSource source = createISource(workMode,port,protocol);
            sourceMap.put(workMode+port,source);
        }
    }

    /**
     * ��ʼ������sink
     */
    public void initJamassSinks(){
        Set<String> set = AmassConfigration.getSinks("Parsed:LogSink");
        if(set.isEmpty()){
            //source ��ʼ���쳣
            logger.error("sink ��ʼ���쳣,û�л�ȡ����Ӧ��sink����");
            System.exit(0);
        }
        Map<String,List<String>> map = new HashMap<>();
        for(String sinkConfig : set){
            //��������־����
            String logType = sinkConfig.split(":")[0];
            //����sink����
            String sinkName = sinkConfig.split(":")[1];
            if(map.containsKey(logType)){
                map.get(logType).add(sinkName);
            }else{
                List<String> list = new ArrayList<>();
                list.add(sinkName);
                map.put(logType,list);
            }
        }
        for (Map.Entry<String, List<String>> sink : map.entrySet()) {
            Forward forward = createISink(sink.getKey(),sink.getValue());
            sinkMap.put(sink.getKey()+" to "+(sink.getValue()).toString(),forward);
        }

    }

    /**
     * ��������sink�߳�
     * @param logType
     * @param sinkList
     * @return
     */
    private Forward createISink(String logType ,List<String> sinkList){
        Forward forward = ForwardFactory.createForward(EventStatus.valueOf(logType),sinkList);
        return forward;
    }

    /**
     * ��������source�߳�
     * @param source
     * @param port
     * @param protocol
     * @return
     */
    private ICollectSource createISource(String source , int port , String protocol){
        ICollectSource iCollectSource = null;
        switch (source){
            case "NettyUdp":
                iCollectSource = new UdpNettySource(source+port,port,ProtocolType.valueOf(protocol));
                break;
            case "SocketUdp":
                iCollectSource = new UdpSocketSource(source+port,port);
                break;
            case "NettyTcp":
                iCollectSource = new TcpNettySource(source+port,port,ProtocolType.valueOf(protocol));
                break;
        }
        return iCollectSource;
    }

    public void initIpDevMap(){
        ipDevMap = new HashMap<>();
        DevPatternInfo devPatternInfo = new DevPatternInfo();
        devPatternInfo.setDevId(1);
        DevPatternInfo devPatternInfo2 = new DevPatternInfo();
        devPatternInfo2.setDevId(2);
        DevPatternInfo devPatternInfo3 = new DevPatternInfo();
        devPatternInfo3.setDevId(107101); //��������IDS
        Set<DevPatternInfo> set = new LinkedHashSet<>();
        Set<DevPatternInfo> set1 = new LinkedHashSet<>();
        set.add(devPatternInfo);
        set.add(devPatternInfo2);
        set1.add(devPatternInfo3);
        ipDevMap.put("10.2.4.12",set1);
        ipDevMap.put("10.2.1.130",set);
    }

    public void startThreadAll(){
        //���� source �߳�
        for (Map.Entry<String, ICollectSource> source : sourceMap.entrySet()) {
            Thread thread = new Thread(source.getValue());
            thread.start();
            logger.info("Source Thread Start:"+source.getKey());
        }

        //���� ת�� sink �߳�
        for (Map.Entry<String, Forward> sink : sinkMap.entrySet()) {
            Thread thread = new Thread(sink.getValue());
            thread.start();
            logger.info("Sink Forward Thread Start:"+sink.getKey());
        }

        this.getIdentifyPool().execute(new DevTypeIdentifier());
        this.getIdentifyPool().execute(new DevTypeIdentifier());
        this.getIdentifyPool().execute(new DevTypeIdentifier());
        this.getIdentifyPool().execute(new DevTypeIdentifier());
        this.getIdentifyPool().execute(new DevTypeIdentifier());
        this.getParsePool().prestartAllCoreThreads();
    }

    private void initThreadPool(){

        setReceiverPool(new ThreadPoolExecutor(1,5,0,TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10000),
                new CollectorThreadFactory("receive"),
                new RejectedExecutionHandler() {
                    public void rejectedExecution(Runnable r,ThreadPoolExecutor executor) {
                        return;
                    }
                }
        ));

//        setIdentifyPool(new ThreadPoolExecutor(1,5,0,TimeUnit.MILLISECONDS,
//                new ArrayBlockingQueue<Runnable>(10000),
//                new CollectorThreadFactory("identify"),
//                new RejectedExecutionHandler() {
//                    public void rejectedExecution(Runnable r,ThreadPoolExecutor executor) {
//                        return;
//                    }
//                }));

        setIdentifyPool(new ThreadPoolExecutor(5,5,0,TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10000),
                new CollectorThreadFactory("identify"),
                new RejectedExecutionHandler() {
                    public void rejectedExecution(Runnable r,ThreadPoolExecutor executor) {
                        return;
                    }
                }));

        setParsePool(new ThreadPoolExecutor(10,10,0,TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10000),
                new CollectorThreadFactory("parse"),
                new RejectedExecutionHandler() {
                    public void rejectedExecution(Runnable r,ThreadPoolExecutor executor) {
                        return;
                    }
                }));

        setSinkPool(new ThreadPoolExecutor(5,10,0,TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(10000),
                new RejectedExecutionHandler() {
                    public void rejectedExecution(Runnable r,ThreadPoolExecutor executor) {
                        return;
                    }
                }));
    }


    static class CollectorThreadFactory implements ThreadFactory {
        static final AtomicInteger poolNumber = new AtomicInteger(1);
        final ThreadGroup group;
        final AtomicInteger threadNumber = new AtomicInteger(1);
        final String namePrefix;

        public CollectorThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = name + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()){
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY){
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

    public ThreadPoolExecutor getReceiverPool() {
        return receiverPool;
    }

    public void setReceiverPool(ThreadPoolExecutor receiverPool) {
        this.receiverPool = receiverPool;
    }

    public ThreadPoolExecutor getParsePool() {
        return parsePool;
    }

    public void setParsePool(ThreadPoolExecutor parsePool) {
        this.parsePool = parsePool;
    }

    public ThreadPoolExecutor getIdentifyPool() {
        return identifyPool;
    }

    public void setIdentifyPool(ThreadPoolExecutor identifyPool) {
        this.identifyPool = identifyPool;
    }

    public Map<String, Set<DevPatternInfo>> getIpDevMap() {
        return ipDevMap;
    }

    public void setIpDevMap(Map<String, Set<DevPatternInfo>> ipDevMap) {
        this.ipDevMap = ipDevMap;
    }

    public RegularExpressionUtil getRegularExpressionUtil() {
        return regularExpressionUtil;
    }

    public void setRegularExpressionUtil(RegularExpressionUtil regularExpressionUtil) {
        this.regularExpressionUtil = regularExpressionUtil;
    }

    public Queue<AmassEvent> getUnknowLogQueue() {
        return unknowLogQueue;
    }

    public void setUnknowLogQueue(Queue<AmassEvent> unknowLogQueue) {
        this.unknowLogQueue = unknowLogQueue;
    }

    public Queue<AmassEvent> getEventQueue() {
        return eventQueue;
    }

    public void setEventQueue(Queue<AmassEvent> eventQueue) {
        this.eventQueue = eventQueue;
    }

    public ThreadPoolExecutor getSinkPool() {
        return sinkPool;
    }

    public void setSinkPool(ThreadPoolExecutor sinkPool) {
        this.sinkPool = sinkPool;
    }

    public KafkaUtil getKafkaUtil() {
        return kafkaUtil;
    }

    public void setKafkaUtil(KafkaUtil kafkaUtil) {
        this.kafkaUtil = kafkaUtil;
    }

    public LinkedBlockingQueue<AmassEvent> getEventIdentifyQueue() {
        return eventIdentifyQueue;
    }

    public void setEventIdentifyQueue(LinkedBlockingQueue<AmassEvent> eventIdentifyQueue) {
        this.eventIdentifyQueue = eventIdentifyQueue;
    }

    public LinkedBlockingQueue<AmassEvent> getEventParseQueue() {
        return eventParseQueue;
    }

    public void setEventParseQueue(LinkedBlockingQueue<AmassEvent> eventParseQueue) {
        this.eventParseQueue = eventParseQueue;
    }
}
