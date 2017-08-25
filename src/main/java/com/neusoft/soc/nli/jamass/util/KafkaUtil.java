package com.neusoft.soc.nli.jamass.util;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.Properties;

/**
 * Created by lyb on 2017/4/25.
 */
public class KafkaUtil {

    public static final Logger logger = LogManager.getLogger(KafkaUtil.class);


    static {

    }
    /**
     * ��ȡ��Ϣ��� ms
     */
    private final int CONSUME_POLL_TIME_OUT = 1000;
    /**
     * kafka server ip�˿��б�
     */
    private static String kafka_server_list;
    /**
     * ����ʵ��
     */
    private static KafkaUtil kafkaUtil;
    /**
     * ������
     */
    private static String agentId = "";

    private long waitMS = 0l;

    public long getWaitMS() {
        return waitMS;
    }

    public void setWaitMS(long waitMS) {
        this.waitMS = waitMS;
    }

    /**
     * �رչ��췽��
     */
    private KafkaUtil(){

    }
    /**
     * kafka������ ����ģʽ
     * @param serverList kafka�������б� ���磺10.2.4.12:9092 ���� 10.2.4.12:9092,10.2.4.13:9092
     * @return KafkaUtil
     */
    public static KafkaUtil getInstance(String serverList){
        if(kafkaUtil == null){
            kafkaUtil = new KafkaUtil();
        }
        kafka_server_list = serverList;
        return kafkaUtil;
    }
    /**
     * kafka������ ����ģʽ Ϊ�˽����Ϣ����ģʽ����Ҫÿһ��consumer�в�ͬ��group id ���� ��Ҫ�̶�
     * @param serverList kafka�������б� ���磺10.2.4.12:9092 ���� 10.2.4.12:9092,10.2.4.13:9092
     * @param agent_id agent������ID
     * @return KafkaUtil
     */
    public static KafkaUtil getInstance(String serverList, String agent_id){
        if(kafkaUtil == null){
            kafkaUtil = new KafkaUtil();
        }
        kafka_server_list = serverList;
        agentId = agent_id;
        return kafkaUtil;
    }
    /**
     * kafka client �����߶���
     */
    private KafkaProducer<String, String> producer;
    /**
     * kafka client �����߶���
     */
    private KafkaConsumer<String, String> consumer;

    /**
     * ��ȡ�����߶��󣬻�ȡkafka���������� Ŀǰֻʵ��key��value����String����
     * @return
     */
    private KafkaProducer getKafkaProducer(){
        if(producer == null){
            Properties props = new Properties();
            props.put("bootstrap.servers", kafka_server_list);
            props.put("client.id", "SocProducer");
            props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            producer = new KafkaProducer(props);
            logger.debug("����kafka producer ����");
        }
        return producer;
    }

    public void initKafkaClient(){
        getKafkaProducer();
    }

    /**
     * ��ȡ�����߶��󣬻�ȡkafka���������� Ŀǰֻʵ��key��value����String����
     * @return
     */
    private KafkaConsumer getKafkaConsumer(){
        if(consumer == null){
            Properties props = new Properties();
            props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafka_server_list);
            props.put(ConsumerConfig.GROUP_ID_CONFIG, "SocConsumerTest"+ agentId);
            props.put(ConsumerConfig.CLIENT_ID_CONFIG, "SocConsumerTest"+ agentId);
            props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
            props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
            props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
            props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
            consumer = new KafkaConsumer(props);
            logger.debug("����kafka consumer ����");
        }
        return consumer;
    }

    public ConsumerRecords consumeMessage(String topic){
        ConsumerRecords<String, String> records = null;
        if(waitMS > 0){
            try {
                logger.info("KAFKA���ѵȴ�"+ waitMS + "ms");
                Thread.sleep(waitMS);
            } catch (InterruptedException e) {
                logger.error("KAFKA ���� �ȴ��쳣",e);
            }
        }else{
            getKafkaConsumer().subscribe(Collections.singletonList(topic));
            records = consumer.poll(CONSUME_POLL_TIME_OUT);
            logger.debug("����poll��kafka������Ϣ��" + records.count() + "��");
        }
        return records;
    }

    /**
     *  ͨ��kafka produce ��Ϣ ��ͨģʽ����ָ�������Զ�����key���з���
     * @param topic ����
     * @param message ��������
     * @param isAsync �Ƿ��첽
     */
    public void produceMessage(String topic ,String message ,Boolean isAsync){
        ProducerRecord producerRecord = new ProducerRecord(topic,message);
        if (isAsync) {
            // Send �첽
            long startTime = System.currentTimeMillis();
            getKafkaProducer().send(producerRecord, new DemoCallBack(startTime, message));
            logger.debug("��kafka��ͨ�첽������Ϣ��" + message);
        } else {
            // Send ͬ��
            try {
                getKafkaProducer().send(producerRecord).get();
                logger.debug("��kafka��ͨͬ��������Ϣ��" + message);
            }catch (Exception e) {
                logger.error("����kafkaUtil����ͬ����Ϣ�����쳣",e);
            }
        }
    }

    /**
     *  ͨ��kafka produce ��Ϣ ��ͨģʽ����ָ�������Զ�����key���з���
     * @param topic ����
     * @param key ӳ��key
     * @param message ��������
     * @param isAsync �Ƿ��첽
     */
    public void produceMessage(String topic ,String key ,String message,Boolean isAsync){
        ProducerRecord producerRecord = new ProducerRecord(topic,key,message);
        if (isAsync) {
            // Send �첽
            long startTime = System.currentTimeMillis();
            getKafkaProducer().send(producerRecord, new DemoCallBack(startTime, message));
            logger.debug("��kafkaָ��keyֵ�첽������Ϣ��key��" + key + ";message:" + message);
        } else {
            // Send ͬ��
            try {
                getKafkaProducer().send(producerRecord).get();
                logger.debug("��kafkaָ��keyֵͬ��������Ϣ��key��" + key + ";message:" + message);
            }catch (Exception e) {
                logger.error("����kafkaUtil����ͬ����Ϣ�����쳣",e);
            }
        }
    }

    /**
     * ͨ��kafka produce ��Ϣ ָ������ģʽ
     * @param topic ����
     * @param key ӳ��key
     * @param message ��������
     * @param isAsync �Ƿ��첽
     * @param partition ������� ��0��ʼ
     */
    public void produceMessage(String topic ,int partition ,String key ,String message,Boolean isAsync){
        ProducerRecord producerRecord = new ProducerRecord(topic,partition ,key,message);
        if (isAsync) {
            // Send �첽
            long startTime = System.currentTimeMillis();
            getKafkaProducer().send(producerRecord, new DemoCallBack(startTime, message));
            logger.debug("��kafkaָ��keyֵ�첽������Ϣ��key��" + key + ";message:" + message);
        } else {
            // Send ͬ��
            try {
                getKafkaProducer().send(producerRecord).get();
                logger.debug("��kafkaָ������������Ϣ��partition��" + partition + ";message:"  + message);
            }catch (Exception e) {
                logger.error("����kafkaUtil����ָ������ͬ����Ϣ�����쳣",e);
            }
        }
    }
    public static void main(String[] agrs){

    }
}
