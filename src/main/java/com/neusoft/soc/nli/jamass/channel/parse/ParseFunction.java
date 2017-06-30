package com.neusoft.soc.nli.jamass.channel.parse;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.bean.ParseScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * Created by kokoharry on 2017/5/12.
 */
public abstract class ParseFunction {

    /**
     * 日志类
     */
    public static Logger logger = LogManager.getLogger(ParseFunction.class);

    /**
     * 解析脚本
     */
    private ParseScript parseScript;

    public ParseScript getParseScript() {
        return parseScript;
    }

    public void setParseScript(ParseScript parseScript) {
        this.parseScript = parseScript;
    }

    /**
     * 需要执行的解析方法 需要重写
     * @param amassEvent
     * @return
     */
    public abstract boolean executeParse(AmassEvent amassEvent);

    /**
     * 需要执行的数据初始化方法 需要重写
     */
    public abstract void initData();

    /**
     * 给CE对象相应属性 赋值
     * @param amassEvent
     * @param mapAttribute
     * @param attribute_key_value
     * @return
     */
    public boolean setCollectorEventAttribute(AmassEvent amassEvent, Map<String, String> mapAttribute, Map<String,Map<String, String>> attribute_key_value){
        Field field = null;
        for (Map.Entry<String, String> entry : mapAttribute.entrySet()) {
            try {
                field = amassEvent.getClass().getField(entry.getKey());
                String value = entry.getValue();
                if(attribute_key_value != null && attribute_key_value.get(entry.getKey()) != null && attribute_key_value.get(entry.getKey()).size() > 0){
                    //这个属性存在对应的码表，进行value转换
                    value = attribute_key_value.get(entry.getKey()).get(entry.getValue());
                    if(value == null){
                        throw new Exception("此属性对应码表没有此key（"+entry.getValue()+"）的映射值,映射关系为："+ attribute_key_value.get(entry.getKey()));
                    }
                }
                setFeildByType(field,value,amassEvent);
            } catch (Exception e) {
                logger.error("通过反射进行ce属性"+entry.getKey()+"赋值异常",e);
                return false;
            }
        }
        //添加成功解析的策略id
        amassEvent.parseId = this.getParseScript().getId();
        return true;
    }

    /**
     * 根据不同的类型feild 赋值 目前支持 String，Boolean，Integer，Long，int，long，boolean
     * @param field
     * @param value
     * @param amassEvent
     */
    public void setFeildByType(Field field,String value,AmassEvent amassEvent){
        if(field != null){
            String type = field.getGenericType().toString();
            // 如果type是类类型，则前面包含"class "，后面跟类名
            try {
                if (type.equals("class java.lang.String")) {
                    field.set(amassEvent,value);
                }else if(type.equals("class java.lang.Integer") || type.equals("int")) {
                    field.setInt(amassEvent,Integer.parseInt(value));
                }else if (type.equals("class java.lang.Boolean") || type.equals("boolean")) {
                    field.setBoolean(amassEvent,Boolean.parseBoolean(value));
                }else if (type.equals("class java.lang.Long") || type.equals("long")) {
                    field.setLong(amassEvent, Long.parseLong(value));
                }
            } catch (Exception e) {
                logger.error("根据不同类型属性进行赋值异常",e);
            }

        }

    }

}
