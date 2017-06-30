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
     * 命中差值，切换解析先后顺序
     */
    private static final long limitNum = 10000l;
    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(RegularExpressionUtil.class);

    //对应设备类型的解析脚本，有序根据命中算法 key：设备类型 value：有序解析脚本集合
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
     * 进行数据初始化，初始化数据库中的解析脚本信息
     * @return
     */
    public boolean initData(){
//        ParseService parseService = new ParseService();
        try{
//            patternMap = parseService.initPatternMap();
            logger.debug("自定义解析脚本初始化成功"+patternMap);
        }catch (Exception e){
            logger.error("mysql初始化解析脚本数据异常",e);
            return false;
        }
        return true;
    }

    /**
     * 判断自定义解析是否含有此设备类型
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
     * 修改或者新增自定义解析策略接口实现
     * @return
     */
    public synchronized boolean addOrUpdateParseScript(ParseScript parseScript){
        Map<String ,ParseScript> map  =  patternMap.get(parseScript.getDev_type());
        try{
            if(map == null || map.size() == 0){
                //新增设备类型解析脚本
                map = new HashMap<String ,ParseScript>();
                map.put(parseScript.getId(),parseScript);
                patternMap.put(parseScript.getDev_type(),map);
            }else{
                //原有设备类型解析脚本新增或者更新
                map.put(parseScript.getId(),parseScript);
            }
        }catch (Exception e){
            logger.error("新增或者更新策略"+parseScript.getId()+"异常",e);
            return false;
        }
        return true;
    }

    /**
     * 删除自定义解析策略接口实现
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
            logger.error("删除策略"+id+"异常",e);
            return false;
        }
        return true;
    }

    /**
     * 执行解析脚本，根据设备类型获取解析脚本并执行，返回收集事件ce
     * @param amassEvent
     * @return
     */
    public AmassEvent executeExpression(AmassEvent amassEvent){
        int dev_type = amassEvent.device;
        //找到解析脚本
        Map<String,ParseScript> map = patternMap.get(dev_type);
        //如果有多个需要进行命中率排序
        if(map.size() > 1){
            patternMap.put(dev_type,resetParseScript(map));
        }
        for( ParseScript parseScript: map.values()){
            //执行解析脚本 根据解析脚本
            if(parseScript.getParseFunction().executeParse(amassEvent)){
                //解析成功，需要跳出不继续进行
                break;
            }
        }
        return amassEvent;
    }

    /**
     * 测试接口执行指定策略id解析脚本，返回收集事件ce
     * @param amassEvent
     * @param id
     * @return
     */
    public AmassEvent executeTestExpression(AmassEvent amassEvent,String id){
        int dev_type = amassEvent.device;
        //找到解析脚本
        Map<String,ParseScript> map = patternMap.get(dev_type);
        ParseScript parseScript = map.get(id);
        //找到对应的解析策略
        if(parseScript != null){
            parseScript.getParseFunction().executeParse(amassEvent);
        }
        return amassEvent;
    }

    /**
     * 命中率 排序核心算法
     * @param map
     */
    public synchronized Map<String,ParseScript> resetParseScript(Map<String,ParseScript> map){
        Map<String,ParseScript> mapReset = new LinkedHashMap<String,ParseScript>();
        long parse_max = 0l;
        String max_id = "";
        for (Map.Entry<String, ParseScript> entry : map.entrySet()) {
            //数量大于最大值的，并且设计阈值 来控制频繁切换
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
            logger.debug("根据命中率进行了解析脚本重新排序");
            return mapReset;
        }else{
            return map;
        }
    }

    /**
     *  测试解析策略接口实现
     * @param dev_id 设备id
     * @param id 解析策略id
     * @param message 测试的消息
     * @return
     */
    public AmassEvent testRegular(int dev_id,String id,String message){
        AmassEvent amassEvent = new AmassEvent();
        amassEvent.device = dev_id;
        amassEvent.setMessage(message);
        amassEvent = executeTestExpression(amassEvent,id);
        //去掉赋值属性
        amassEvent.device = 0;
        amassEvent.setMessage("");
        return amassEvent;
    }

    // TODO: 初始化数据

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
            ce.setMessage("{\"content\":{\"sccount\":\"1\",\"virus_version\":\"2017.2.18.17\",\"guid\":\"3150C49A-7F21-40E5-BC1E-8780D51D8C6E\",\"ucclient\":\""+1+"\",\"cclient\":\"157\",\"begintime\":\"2000-02-01 00:00:00\",\"ip\":\"192.168.129.30\",\"level\":\"1\",\"usccount\":\"1\",\"usclient\":\"1\",\"parentid\":\"E9F622A5-C345-4F31-A01C-BEA810194BAF\",\"sclient\":\"1\",\"scname\":\"中卫热电有限公司\",\"scid\":\"000208\",\"endtime\":\"2017-03-31 00:00:00\"},\"type\":3,\"version\":1.1,\"sendtime\":1487713266}");
//            ce1.setMessage("WAR*|15*|设备未登记*|2014-03-10 10:58:35*|192.168.9.234*|000C29C2CB37*|VRV-ELG91VNEJO3*|*|*|*|*|*|*|969*|*|扫描报警,设备没有注册");
            expressionUtil.executeExpression(ce);
//            expressionUtil.executeExpression(ce1);
        }
        System.out.println(  ce + "\n"+(System.currentTimeMillis() - a) );
    }

}
