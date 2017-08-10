package com.neusoft.soc.nli.jamass.channel.parse;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 根据正则表达式，配置的属性对应正则解析
 * Created by kokoharry on 2017/5/12.
 */
public class RugularParse extends ParseFunction {

    @Override
    public boolean executeParse(AmassEvent amassEvent) {
        boolean mustFlag = false;
        long a = System.nanoTime();
        // 是否含有 关键属性
        if(this.getParseScript().getMustPatternMap() != null && this.getParseScript().getMustPatternMap().size() > 0){
            mustFlag = doParse(amassEvent ,this.getParseScript().getMustPatternMap(),true);
        }
        //增加统计功能，统计成功次数
        if(mustFlag){
            this.getParseScript().add_parse_number();
        }
        // 关键属性解析成功 是否含有 非关键属性
        if(mustFlag && this.getParseScript().getPatternMap() != null && this.getParseScript().getPatternMap().size() > 0){
            doParse(amassEvent ,this.getParseScript().getPatternMap(),false);
        }
      logger.debug( this.getParseScript().getId()+"单条解析策略脚本消耗时间：" + (System.nanoTime() - a));
       return mustFlag;
    }

    @Override
    public void initData() {
        this.getParseScript().setParse_type(1);
        Map<String,String> mapMust = this.getParseScript().getAttribute_regular_mapping_must();
        //关键属性
        if( mapMust != null && mapMust.size() > 0){
            Map<String,Pattern> map = new HashMap<String,Pattern>();
            for (Map.Entry<String, String> entry : mapMust.entrySet()) {
                map.put(entry.getKey(),Pattern.compile(entry.getValue()));
            }
            logger.debug("生成关键属性正则表达式为：" + map);
            this.getParseScript().setMustPatternMap(map);
        }
        Map<String,String> map = this.getParseScript().getAttribute_regular_mapping();
        //非关键属性
        if(map != null && map.size() > 0){
            Map<String,Pattern> mapTemp = new HashMap<String,Pattern>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                mapTemp.put(entry.getKey(),Pattern.compile(entry.getValue()));
            }
            logger.debug("生成非关键属性正则表达式为：" + mapTemp);
            this.getParseScript().setPatternMap(mapTemp);
        }
    }

    /**
     * 进行数据解析
     * @param amassEvent 事件对象
     * @param map 属性和正则解析列表
     * @param type 是否关键属性 ，关键属性有一个未解出来就算失败
     * @return 解析结果
     */
    private boolean doParse(AmassEvent amassEvent, Map<String ,Pattern> map,boolean type){
        Map<String,String> mapAttribute = new HashMap<String,String>();
        //循环属性解析列表
        for (Map.Entry<String, Pattern> entry : map.entrySet()) {
            Pattern pattern = entry.getValue();
            Matcher matcher = pattern.matcher(amassEvent.getRaw());
            //只查找一次，如果有则获取
            if(matcher.find()){
                logger.debug(entry.getKey()+":"+matcher.group(1));
                mapAttribute.put(entry.getKey(),matcher.group(1));
            }else if(type){
                //如果关键属性，有没有找到的则直接返回失败
                return false;
            }
        }
        //如果解析成功，则进行属性到Ce对象的赋值
        return setCollectorEventAttribute(amassEvent,mapAttribute,this.getParseScript().getAttribute_key_value());
    }
}
