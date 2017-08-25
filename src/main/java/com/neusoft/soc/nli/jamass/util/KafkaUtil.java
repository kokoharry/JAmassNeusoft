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
     * 拉取消息间隔 ms
     */
    private final int CONSUME_POLL_TIME_OUT = 1000;
    /**
     * kafka server ip端口列表
     */
    private static String kafka_server_list;
    /**
     * 单例实例
     */
    private static KafkaUtil kafkaUtil;
    /**
     * 代理编号
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
     * 关闭构造方法
     */
    private KafkaUtil(){

    }
    /**
     * kafka工具类 单例模式
     * @param serverList kafka服务器列表 例如：10.2.4.12:9092 或者 10.2.4.12:9092,10.2.4.13:9092
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
     * kafka工具类 单例模式 为了解决消息订阅模式，需要每一个consumer有不同的group id 并且 需要固定
     * @param serverList kafka服务器列表 例如：10.2.4.12:9092 或者 10.2.4.12:9092,10.2.4.13:9092
     * @param agent_id agent服务器ID
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
     * kafka client 生产者对象
     */
    private KafkaProducer<String, String> producer;
    /**
     * kafka client 消费者对象
     */
    private KafkaConsumer<String, String> consumer;

    /**
     * 获取生产者对象，获取kafka生产者连接 目前只实现key和value都是String类型
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
            logger.debug("创建kafka producer 连接");
        }
        return producer;
    }

    public void initKafkaClient(){
        getKafkaProducer();
    }

    /**
     * 获取消费者对象，获取kafka消费者连接 目前只实现key和value都是String类型
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
            logger.debug("创建kafka consumer 连接");
        }
        return consumer;
    }

    public ConsumerRecords consumeMessage(String topic){
        ConsumerRecords<String, String> records = null;
        if(waitMS > 0){
            try {
                logger.info("KAFKA消费等待"+ waitMS + "ms");
                Thread.sleep(waitMS);
            } catch (InterruptedException e) {
                logger.error("KAFKA 消费 等待异常",e);
            }
        }else{
            getKafkaConsumer().subscribe(Collections.singletonList(topic));
            records = consumer.poll(CONSUME_POLL_TIME_OUT);
            logger.debug("本次poll从kafka消费信息：" + records.count() + "条");
        }
        return records;
    }

    /**
     *  通过kafka produce 信息 普通模式无需指定分区自动根据key进行分区
     * @param topic 主题
     * @param message 发送内容
     * @param isAsync 是否异步
     */
    public void produceMessage(String topic ,String message ,Boolean isAsync){
        ProducerRecord producerRecord = new ProducerRecord(topic,message);
        if (isAsync) {
            // Send 异步
            long startTime = System.currentTimeMillis();
            getKafkaProducer().send(producerRecord, new DemoCallBack(startTime, message));
            logger.debug("向kafka普通异步生产信息：" + message);
        } else {
            // Send 同步
            try {
                getKafkaProducer().send(producerRecord).get();
                logger.debug("向kafka普通同步生产信息：" + message);
            }catch (Exception e) {
                logger.error("调用kafkaUtil进行同步信息生产异常",e);
            }
        }
    }

    /**
     *  通过kafka produce 信息 普通模式无需指定分区自动根据key进行分区
     * @param topic 主题
     * @param key 映射key
     * @param message 发送内容
     * @param isAsync 是否异步
     */
    public void produceMessage(String topic ,String key ,String message,Boolean isAsync){
        ProducerRecord producerRecord = new ProducerRecord(topic,key,message);
        if (isAsync) {
            // Send 异步
            long startTime = System.currentTimeMillis();
            getKafkaProducer().send(producerRecord, new DemoCallBack(startTime, message));
            logger.debug("向kafka指定key值异步生产信息：key：" + key + ";message:" + message);
        } else {
            // Send 同步
            try {
                getKafkaProducer().send(producerRecord).get();
                logger.debug("向kafka指定key值同步生产信息：key：" + key + ";message:" + message);
            }catch (Exception e) {
                logger.error("调用kafkaUtil进行同步信息生产异常",e);
            }
        }
    }

    /**
     * 通过kafka produce 信息 指定分区模式
     * @param topic 主题
     * @param key 映射key
     * @param message 发送内容
     * @param isAsync 是否异步
     * @param partition 分区标号 从0开始
     */
    public void produceMessage(String topic ,int partition ,String key ,String message,Boolean isAsync){
        ProducerRecord producerRecord = new ProducerRecord(topic,partition ,key,message);
        if (isAsync) {
            // Send 异步
            long startTime = System.currentTimeMillis();
            getKafkaProducer().send(producerRecord, new DemoCallBack(startTime, message));
            logger.debug("向kafka指定key值异步生产信息：key：" + key + ";message:" + message);
        } else {
            // Send 同步
            try {
                getKafkaProducer().send(producerRecord).get();
                logger.debug("向kafka指定分区生产信息：partition：" + partition + ";message:"  + message);
            }catch (Exception e) {
                logger.error("调用kafkaUtil进行指定分区同步信息生产异常",e);
            }
        }
    }
    public static void main(String[] agrs){

    }
}
