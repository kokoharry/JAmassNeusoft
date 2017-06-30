package com.neusoft.soc.nli.jamass.core;

/**
 * Created by luyb on 2017/6/5.
 */
public class AmassConstant {
    /**
     * JAmass工作模式 例如syslog_udp 多个使用“JAMASS_WORK_MODE_SPLIT”分割
     */
    public static final String JAMASS_SINK = "jamass.sink";
    /**
     * JAmass工作模式 例如syslog_udp 多个使用“JAMASS_WORK_MODE_SPLIT”分割
     */
    public static final String JAMASS_SOURCE = "jamass.source";
    /**
     * JAmass工作模式 指定分隔符
     */
    public static final String JAMASS_WORK_MODE_SPLIT = ",";
    /**
     * sink package path
     */
    public static final String SINK_PACKAGE_PATH = "com.neusoft.soc.nli.jamass.sink";
}
