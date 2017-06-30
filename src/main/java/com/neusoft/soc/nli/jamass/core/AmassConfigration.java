package com.neusoft.soc.nli.jamass.core;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.InputStream;
import java.util.*;

/**
 * JAmass ������Ϣ
 * Created by luyb on 2017/6/5.
 */
public class AmassConfigration {
    /**
     * pid�ļ� �޸�֧�ֿ���չ
     */
    public static final String PIDFILE = "/soc/var/pid/collector.pid";
    /**
     * ���Զ�ȡ��
     */
    private static Properties props = null;
    /**
     * ��־��
     */
    private static Logger logger = LogManager.getLogger(AmassConfigration.class);
    /**
     * �����ļ�·��
     */
    private static final String CONFIGFILE = "/amass.properties";
    /**
     * �ļ��޸�ʱ���
     */
    private static Long lastModified = 0l;

    /**
     * ��ʼ�����������ļ�
     */
    static {
        //��ʼ��
        reloadProps();
        //��̬���������ļ�
        new Timer("Configration File Monitor").schedule(new TimerTask() {
            public void run() {
                try{
                    if(props == null || isPropertiesModified()){
                        reloadProps();
                        logger.info("�����ļ���̬����");
                    }
                }catch (Exception e){
                    logger.error("��ʱ��̬���������ļ��쳣",e);
                }
            }
        }, 60*1000, 60*1000);
    }

    /**
     * ���¼��������ļ�
     */
    public static void reloadProps() {
        InputStream stream = AmassConfigration.class.getResourceAsStream(CONFIGFILE);
        try {
            props =  new Properties();
            props.load(stream);
            //��¼�ļ����һ���޸�ʱ��
            File file = new File(AmassConfigration.class.getResource(CONFIGFILE).getPath());
            if(file != null){
                lastModified = file.lastModified();
            }
            stream.close();
        } catch (Exception e) {
            logger.error("JAmass can not load amass.properties",e);
        }
    }

    /**
     * ��ȡ��־�ռ�JAmass����ģʽ source
     * @param defaultValue ���Ϊ�գ�Ĭ��ֵ
     * @return
     */
    public static Set<String> getSources(String defaultValue){
        String result = "";
        if(StringUtils.isNotBlank(defaultValue)){
            result = getValuesFromProp(AmassConstant.JAMASS_SOURCE,defaultValue);
        }else{
            result = getValuesFromProp(AmassConstant.JAMASS_SOURCE);
        }
        if(!StringUtils.isNotBlank(result)){
            result = defaultValue;
            logger.error("�����ļ��쳣 source ��ȡʧ�� ʹ��Ĭ������" + defaultValue);
        }
        Set<String> set = new HashSet<>();
        String[] arr = result.split(AmassConstant.JAMASS_WORK_MODE_SPLIT);
        for(String config : arr){
            set.add(config);
        }
        return set;
    }

    /**
     * ��ȡ��־�ռ�JAmass����ģʽ sink
     * @param defaultValue ���Ϊ�գ�Ĭ��ֵ
     * @return
     */
    public static Set<String> getSinks(String defaultValue){
        String result = "";
        if(StringUtils.isNotBlank(defaultValue)){
            result = getValuesFromProp(AmassConstant.JAMASS_SINK,defaultValue);
        }else{
            result = getValuesFromProp(AmassConstant.JAMASS_SINK);
        }
        if(!StringUtils.isNotBlank(result)){
            result = defaultValue;
            logger.error("�����ļ��쳣 source ��ȡʧ�� ʹ��Ĭ������" + defaultValue);
        }
        Set<String> set = new HashSet<>();
        String[] arr = result.split(AmassConstant.JAMASS_WORK_MODE_SPLIT);
        for(String config : arr){
            set.add(config);
        }
        return set;
    }


    /**
     * ͨ�� prop �����ļ��������ȡ��Ӧ����������ֵ ���Ϊ�շ��� ����
     * @param key ����key
     * @return
     */
    private static String getValuesFromProp(String key){
        String value = "";
        if(props != null){
            value = props.getProperty(key,"");
        }
        return value;
    }

    /**
     * ͨ�� prop �����ļ��������ȡ��Ӧ����������ֵ ���Ϊ�շ��� defaultValue
     * @param key ����key
     * @param defaultValue ָ����Ĭ�Ϸ���ֵ
     * @return
     */
    private static String getValuesFromProp(String key,String defaultValue){
        String value = defaultValue;
        if(props != null){
            value = props.getProperty(key,defaultValue);
        }
        return value;
    }


    /**
     * �ж��ļ��Ƿ��޸Ĺ�
     * @return
     */
    private static boolean isPropertiesModified() {
        boolean returnValue = false;
        File file = new File(AmassConfigration.class.getResource(CONFIGFILE).getPath());
        if (file.lastModified() > lastModified) {
            lastModified=file.lastModified();
            returnValue = true;
        }
        return returnValue;
    }

}
