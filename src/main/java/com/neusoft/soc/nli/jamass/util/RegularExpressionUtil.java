package com.neusoft.soc.nli.jamass.util;


import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.bean.ParseScript;
import com.neusoft.soc.nli.jamass.channel.identify.DevTypeIdentifier;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by kokoharry on 2017/5/12.
 */
public class RegularExpressionUtil {

    private static RegularExpressionUtil expressionUtil;

    /**
     * ���в�ֵ���л������Ⱥ�˳��
     */
    private static final long limitNum = 10000l;
    /**
     * ��־��
     */
    private static Logger logger = LogManager.getLogger(RegularExpressionUtil.class);

    //��Ӧ�豸���͵Ľ����ű���������������㷨 key���豸���� value����������ű�����
    private Map<Integer,Map<String ,ParseScript>> patternMap;

    public Map<Integer, Map<String, ParseScript>> getPatternMap() {
        return patternMap;
    }

    private RegularExpressionUtil(){
        patternMap = new HashMap<Integer,Map<String ,ParseScript>>();
    }

    public static RegularExpressionUtil getInstance(){
        if(expressionUtil == null){
            expressionUtil = new RegularExpressionUtil();
        }
        return expressionUtil;
    }

    /**
     * �������ݳ�ʼ������ʼ�����ݿ��еĽ����ű���Ϣ
     * @return
     */
    public boolean initData(){
//        ParseService parseService = new ParseService();
        try{
//            patternMap = parseService.initPatternMap();
            logger.debug("�Զ�������ű���ʼ���ɹ�"+patternMap);
        }catch (Exception e){
            logger.error("mysql��ʼ�������ű������쳣",e);
            return false;
        }
        return true;
    }

    /**
     * �ж��Զ�������Ƿ��д��豸����
     * @return
     */
    public boolean hasDeviceType(int device_type){
        Map<String ,ParseScript> map = patternMap.get(device_type);
        if(map != null && map.size() > 0){
            return true;
        }
        return false;
    }

    /**
     * �޸Ļ��������Զ���������Խӿ�ʵ��
     * @return
     */
    public synchronized boolean addOrUpdateParseScript(ParseScript parseScript){
        Map<String ,ParseScript> map  =  patternMap.get(parseScript.getDev_type());
        try{
            if(map == null || map.size() == 0){
                //�����豸���ͽ����ű�
                map = new HashMap<String ,ParseScript>();
                map.put(parseScript.getId(),parseScript);
                patternMap.put(parseScript.getDev_type(),map);
            }else{
                //ԭ���豸���ͽ����ű��������߸���
                map.put(parseScript.getId(),parseScript);
            }
        }catch (Exception e){
            logger.error("�������߸��²���"+parseScript.getId()+"�쳣",e);
            return false;
        }
        return true;
    }

    /**
     * ɾ���Զ���������Խӿ�ʵ��
     * @return
     */
    public synchronized boolean deleteParseScript(String id){
        try{
            for(Map<String ,ParseScript> map : patternMap.values()){
                if(map.containsKey(id)){
                    map.remove(id);
                    break;
                }
            }
        }catch (Exception e){
            logger.error("ɾ������"+id+"�쳣",e);
            return false;
        }
        return true;
    }

    /**
     * ִ�н����ű��������豸���ͻ�ȡ�����ű���ִ�У������ռ��¼�ce
     * @param amassEvent
     * @return
     */
    public AmassEvent executeExpression(AmassEvent amassEvent){
        int dev_type = amassEvent.device;
        //�ҵ������ű�
        Map<String,ParseScript> map = patternMap.get(dev_type);
        //����ж����Ҫ��������������
        if(map.size() > 1){
            patternMap.put(dev_type,resetParseScript(map));
        }
        for( ParseScript parseScript: map.values()){
            //ִ�н����ű� ���ݽ����ű�
            if(parseScript.getParseFunction().executeParse(amassEvent)){
                //�����ɹ�����Ҫ��������������
                break;
            }
        }
        return amassEvent;
    }

    /**
     * ���Խӿ�ִ��ָ������id�����ű��������ռ��¼�ce
     * @param amassEvent
     * @param id
     * @return
     */
    public AmassEvent executeTestExpression(AmassEvent amassEvent,String id){
        int dev_type = amassEvent.device;
        //�ҵ������ű�
        Map<String,ParseScript> map = patternMap.get(dev_type);
        ParseScript parseScript = map.get(id);
        //�ҵ���Ӧ�Ľ�������
        if(parseScript != null){
            parseScript.getParseFunction().executeParse(amassEvent);
        }
        return amassEvent;
    }

    /**
     * ������ ��������㷨
     * @param map
     */
    public synchronized Map<String,ParseScript> resetParseScript(Map<String,ParseScript> map){
        Map<String,ParseScript> mapReset = new LinkedHashMap<String,ParseScript>();
        long parse_max = 0l;
        String max_id = "";
        for (Map.Entry<String, ParseScript> entry : map.entrySet()) {
            //�����������ֵ�ģ����������ֵ ������Ƶ���л�
            if(parse_max < entry.getValue().getParse_number() && (entry.getValue().getParse_number() - parse_max) > limitNum ){
                parse_max = entry.getValue().getParse_number();
                max_id = entry.getValue().getId();
            }
        }
        if(StringUtils.isNotBlank(max_id)){
            ParseScript parseScriptMax = map.get(max_id);
            mapReset.put(parseScriptMax.getId(),parseScriptMax);
            map.remove(max_id);
            mapReset.putAll(map);
            logger.debug("���������ʽ����˽����ű���������");
            return mapReset;
        }else{
            return map;
        }
    }

    /**
     *  ���Խ������Խӿ�ʵ��
     * @param dev_id �豸id
     * @param id ��������id
     * @param message ���Ե���Ϣ
     * @return
     */
    public AmassEvent testRegular(int dev_id,String id,String message){
        AmassEvent amassEvent = new AmassEvent();
        amassEvent.device = dev_id;
        amassEvent.setMessage(message);
        amassEvent = executeTestExpression(amassEvent,id);
        //ȥ����ֵ����
        amassEvent.device = 0;
        amassEvent.setMessage("");
        return amassEvent;
    }

    // TODO: ��ʼ������

    public static void main(String[] args){
        RegularExpressionUtil expressionUtil = RegularExpressionUtil.getInstance();
        expressionUtil.initData();
        AmassEvent ce = new AmassEvent();
        ce.device = 120031;
//        ce.logLevel = 11;
        ce.char1 = "test";
        ce.char2 = "test";
        ce.char3 = "test";
        ce.char4 = "test";
        AmassEvent ce1 = new AmassEvent();
        ce1.device = 190001;
//        ce1.logLevel = 1;
        ce1.char1 = "test";
        ce1.char2 = "test";
        ce1.char3 = "test";
        ce1.char4 = "test";
        long a = System.currentTimeMillis();
        for(int i=0;i<1000000;i++){
            ce.setMessage("{\"content\":{\"sccount\":\"1\",\"virus_version\":\"2017.2.18.17\",\"guid\":\"3150C49A-7F21-40E5-BC1E-8780D51D8C6E\",\"ucclient\":\""+1+"\",\"cclient\":\"157\",\"begintime\":\"2000-02-01 00:00:00\",\"ip\":\"192.168.129.30\",\"level\":\"1\",\"usccount\":\"1\",\"usclient\":\"1\",\"parentid\":\"E9F622A5-C345-4F31-A01C-BEA810194BAF\",\"sclient\":\"1\",\"scname\":\"�����ȵ����޹�˾\",\"scid\":\"000208\",\"endtime\":\"2017-03-31 00:00:00\"},\"type\":3,\"version\":1.1,\"sendtime\":1487713266}");
//            ce1.setMessage("WAR*|15*|�豸δ�Ǽ�*|2014-03-10 10:58:35*|192.168.9.234*|000C29C2CB37*|VRV-ELG91VNEJO3*|*|*|*|*|*|*|969*|*|ɨ�豨��,�豸û��ע��");
            expressionUtil.executeExpression(ce);
//            expressionUtil.executeExpression(ce1);
        }
        System.out.println(  ce + "\n"+(System.currentTimeMillis() - a) );
    }

}
