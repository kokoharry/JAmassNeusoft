package com.neusoft.soc.nli.jamass.sink;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.core.AmassConfigration;
import com.neusoft.soc.nli.jamass.core.AmassConstant;
import com.neusoft.soc.nli.jamass.core.AmassEngine;
import com.neusoft.soc.nli.jamass.util.KafkaUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Created by luyb on 2017/6/9.
 */
public class KafkaSink implements ISink{

    /**
     * ��־��
     */
    private static Logger logger = LogManager.getLogger(KafkaSink.class);

    private KafkaUtil kafkaUtil;

    private Map<String,String> mapConfig;

    /**
     * ���췽����ִ�г�ʼ��
     */
    public KafkaSink(){
        init();
    }

    @Override
    public void init() {
        this.kafkaUtil = AmassEngine.getInstance().getKafkaUtil();
        this.mapConfig = AmassConfigration.getKafkaConfig();
    }

    @Override
    public void send(AmassEvent amassEvent) {
        logger.info("KafkaSink receive evnet:"+amassEvent);
        try{
            kafkaUtil.produceMessage(mapConfig.get(AmassConstant.SINK_KAFKASINK_CONFIG_TOPIC_NAME),
                    amassEvent.toString(),true);
        }catch (Exception e){
            logger.error("���ݴ����쳣",e);
            doException();
        }
    }

    /**
     * �쳣����
     */
    @Override
    public void doException() {

    }

    @Override
    public boolean checkConnect() {
        return false;
    }
}
