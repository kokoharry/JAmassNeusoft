package com.neusoft.soc.nli.jamass.core;

/**
 * Created by luyb on 2017/6/5.
 */
public class AmassConstant {
    /**
     * JAmass����ģʽ ����syslog_udp ���ʹ�á�JAMASS_WORK_MODE_SPLIT���ָ�
     */
    public static final String JAMASS_SINK = "jamass.sink";
    /**
     * JAmass����ģʽ ����syslog_udp ���ʹ�á�JAMASS_WORK_MODE_SPLIT���ָ�
     */
    public static final String JAMASS_SOURCE = "jamass.source";
    /**
     * JAmass����ģʽ ָ���ָ���
     */
    public static final String JAMASS_WORK_MODE_SPLIT = ",";
    /**
     * JAmass �����ļ� key�㼶�ָ���
     */
    public static final String JAMASS_CONFIGRATION_FILE_POINT = ".";
    /**
     * sink package path
     */
    public static final String SINK_PACKAGE_PATH = "com.neusoft.soc.nli.jamass.sink";
    /**
     * sink kafka config server list key
     */
    public static final String SINK_KAFKASINK_CONFIG_SERVER_LIST_NAME = "server";
    /**
     * sink kafka config topic key
     */
    public static final String SINK_KAFKASINK_CONFIG_TOPIC_NAME = "topic";
    /**
     * sink kafka config topic key
     */
    public static final String CHANNEL_CONFIG_INDENTIFY_THREAD_NUM = "jamass.channel.indentify.thread";
    /**
     * sink kafka config topic key
     */
    public static final String ENGINE_FILTER_CACHE_SIZE = "jamass.engine.filteQueue.size";
    /**
     * sink kafka config topic key
     */
    public static final String ENGINE_UNKNOWN_CACHE_SIZE = "jamass.engine.unkownQueue.size";
    /**
     * sink kafka config topic key
     */
    public static final String ENGINE_RECIVE_CACHE_SIZE = "jamass.engine.eventQueue.size";

    /**
     * sink kafka config topic key
     */
    public static final String INDENTIFY_ERROR_MSG_NO_IP = "�����ڶ�Ӧ��IP�豸ӳ���ϵIP��";
    /**
     * sink kafka config topic key
     */
    public static final String INDENTIFY_ERROR_MSG_NO_DEVPATTERN = "�����ڶ�Ӧ��IP���豸ʶ����ʽIP��";
    /**
     * sink kafka config topic key
     */
    public static final String INDENTIFY_ERROR_MSG_FAIL = "��Ӧ��IP���豸ʶ��ʧ��IP��";
}
