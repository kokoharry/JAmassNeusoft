package com.neusoft.soc.nli.jamass.channel.parse;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据顺序号定义的属性顺序进行解析
 * Created by kokoharry on 2017/5/12.
 */
public class SequenceParse extends ParseFunction {

    @Override
    public boolean executeParse(AmassEvent amassEvent) {
        boolean mustFlag = false;
        long a = System.nanoTime();
        // 是否含有 顺序号及属性对应关系，并且存在分割符号
        if(this.getParseScript().getSplit_sign()!= null && this.getParseScript().getAttribute_sque() != null && this.getParseScript().getAttribute_sque().size() > 0){
            mustFlag = doParse(amassEvent ,this.getParseScript().getSplit_sign(),this.getParseScript().getAttribute_sque(),this.getParseScript().getAttribute_sque_must_value());
        }
        //增加统计功能，统计成功次数
        if(mustFlag){
            this.getParseScript().add_parse_number();
        }
        logger.debug( this.getParseScript().getId()+"单条解析策略脚本消耗时间：" + (System.nanoTime() - a));
        return mustFlag;
    }

    @Override
    public void initData() {
        this.getParseScript().setParse_type(0);
    }

    /**
     * 进行数据解析
     * @param amassEvent 收集事件
     * @param splitSign 顺序分隔符
     * @param map   非关键属性和序号映射
     * @param mapMust 关键属性序号 以及映射值
     * @return
     */
    private boolean doParse(AmassEvent amassEvent, String splitSign, Map<String,Integer> map ,Map<Integer,String> mapMust){
        Map<String,String> mapAttribute = new HashMap<String,String>();
        String[] strings = amassEvent.getMessage().split(escapeExprSpecialWord(splitSign));
        //只有有分割结果，并且分割完的数组比需要属性长
        if(strings != null && strings.length >= (getMaxIndexFromMap(map)+1)){
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String value = strings[entry.getValue()];
                String mustValue = mapMust.get(entry.getValue());
                //有关键属性，并且关键属性存在赋值
                if(mustValue != null && !mustValue.trim().equals("")){
                    //此顺序号，为关键属性
                    if(mustValue.equals(value)){
                        //关键属性对应 设置对应值
                        mapAttribute.put(entry.getKey(),value);
                    }else{
                        return false;
                    }
                }else{
                    // 非关键属性
                    mapAttribute.put(entry.getKey(),value);
                }
            }
            return setCollectorEventAttribute(amassEvent,mapAttribute,this.getParseScript().getAttribute_key_value());
        }else{
            return false;
        }
    }

    /**
     * 在map中获取最大的value值
     * @param map
     * @return
     */
    private int getMaxIndexFromMap(Map<String,Integer> map){
        int max = 0;
        for (Integer integer : map.values()) {
            if(max <= integer){
                max = integer;
            }
        }
        return max;
    }

    /**
     * 转义正则特殊字符 （$()*+.[]?\^{},|）
     *
     * @param keyword
     * @return
     */
    public String escapeExprSpecialWord(String keyword) {
        if (StringUtils.isNotBlank(keyword)) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }
}
