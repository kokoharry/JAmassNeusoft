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
}
