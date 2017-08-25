package com.neusoft.soc.nli.jamass.core;

import com.neusoft.soc.nli.jamass.bean.SinkEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * JAmass 配置信息
 * Created by luyb on 2017/6/5.
 */
public class AmassConfigration {
    /**
     * pid文件 修改支持可扩展
     */
    public static final String PIDFILE = "/soc/var/pid/collector.pid";
    /**
     * 属性读取类
     */
    private static Properties props = null;
    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(AmassConfigration.class);
    /**
     * 配置文件路径
     */
    private static final String CONFIGFILE = "/amass.properties";
    /**
     * 文件修改时间戳
     */
    private static Long lastModified = 0l;

    /**
     * 初始化加载属性文件
     */
    static {
        //初始化
        reloadProps();
        //动态加载配置文件
        new Timer("Configration File Monitor").schedule(new TimerTask() {
            public void run() {
                try {
                    if (props == null || isPropertiesModified()) {
                        reloadProps();
                        logger.info("配置文件动态更新");
                    }
                } catch (Exception e) {
                    logger.error("定时动态更新配置文件异常", e);
                }
            }
        }, 60 * 1000, 60 * 1000);

//        LogManager.getLogger("kafka.producer").s.setLevel(Level.OFF);
    }

    /**
     * 重新加载属性文件
     */
    public static void reloadProps() {
        InputStream stream = AmassConfigration.class.getResourceAsStream(CONFIGFILE);
        try {
            props = new Properties();
            props.load(stream);
            //记录文件最后一次修改时间
            File file = new File(AmassConfigration.class.getResource(CONFIGFILE).getPath());
            if (file != null) {
                lastModified = file.lastModified();
            }
            stream.close();
        } catch (Exception e) {
            logger.error("JAmass can not load amass.properties", e);
        }
    }

    /**
     * 获取日志收集JAmass工作模式 source
     *
     * @param defaultValue 如果为空，默认值
     * @return
     */
    public static Set<String> getSources(String defaultValue) {
        String result = "";
        if (StringUtils.isNotBlank(defaultValue)) {
            result = getValuesFromProp(AmassConstant.JAMASS_SOURCE, defaultValue);
        } else {
            result = getValuesFromProp(AmassConstant.JAMASS_SOURCE);
        }
        if (!StringUtils.isNotBlank(result)) {
            result = defaultValue;
            logger.error("配置文件异常 source 获取失败 使用默认配置" + defaultValue);
        }
        Set<String> set = new HashSet<>();
        String[] arr = result.split(AmassConstant.JAMASS_WORK_MODE_SPLIT);
        for (String config : arr) {
            set.add(config);
        }
        return set;
    }

    /**
     * 获取日志收集JAmass工作模式 sink
     *
     * @param defaultValue 如果为空，默认值
     * @return
     */
    public static Set<String> getSinks(String defaultValue) {
        String result = "";
        if (StringUtils.isNotBlank(defaultValue)) {
            result = getValuesFromProp(AmassConstant.JAMASS_SINK, defaultValue);
        } else {
            result = getValuesFromProp(AmassConstant.JAMASS_SINK);
        }
        if (!StringUtils.isNotBlank(result)) {
            result = defaultValue;
            logger.error("配置文件异常 source 获取失败 使用默认配置" + defaultValue);
        }
        Set<String> set = new HashSet<>();
        String[] arr = result.split(AmassConstant.JAMASS_WORK_MODE_SPLIT);
        for (String config : arr) {
            set.add(config);
        }
        return set;
    }

    /**
     * 获取日志收集JAmass工作模式 sink
     *
     * @return
     */
    public static Map<String, String> getKafkaConfig() {
        String configName = AmassConstant.JAMASS_SINK + AmassConstant
                .JAMASS_CONFIGRATION_FILE_POINT + SinkEnum.KafkaSink.getName() + AmassConstant
                .JAMASS_CONFIGRATION_FILE_POINT;
        Map<String, String> map = new HashMap<>();
        map.put(AmassConstant.SINK_KAFKASINK_CONFIG_SERVER_LIST_NAME,
                getValuesFromProp(configName + AmassConstant.SINK_KAFKASINK_CONFIG_SERVER_LIST_NAME));
        map.put(AmassConstant.SINK_KAFKASINK_CONFIG_TOPIC_NAME,
                getValuesFromProp(configName + AmassConstant.SINK_KAFKASINK_CONFIG_TOPIC_NAME));
        // TODO: 更多的参数
        return map;
    }

    /**
     * 通过 prop 配置文件加载类获取对应的属性配置值 如果为空返回 “”
     *
     * @param key 属性key
     * @return
     */
    private static String getValuesFromProp(String key) {
        String value = "";
        if (props != null) {
            value = props.getProperty(key, "");
        }
        return value;
    }

    /**
     * 通过 prop 配置文件加载类获取对应的属性配置值 如果为空返回 defaultValue
     *
     * @param key          属性key
     * @param defaultValue 指定的默认返回值
     * @return
     */
    public static String getValuesFromProp(String key, String defaultValue) {
        String value = defaultValue;
        if (props != null) {
            value = props.getProperty(key, defaultValue);
        }
        return value;
    }


    /**
     * 判断文件是否被修改过
     *
     * @return
     */
    private static boolean isPropertiesModified() {
        boolean returnValue = false;
        File file = new File(AmassConfigration.class.getResource(CONFIGFILE).getPath());
        if (file.lastModified() > lastModified) {
            lastModified = file.lastModified();
            returnValue = true;
        }
        return returnValue;
    }

}
