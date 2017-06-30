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
     * ��־��
     */
    public static Logger logger = LogManager.getLogger(ParseFunction.class);

    /**
     * �����ű�
     */
    private ParseScript parseScript;

    public ParseScript getParseScript() {
        return parseScript;
    }

    public void setParseScript(ParseScript parseScript) {
        this.parseScript = parseScript;
    }

    /**
     * ��Ҫִ�еĽ������� ��Ҫ��д
     * @param amassEvent
     * @return
     */
    public abstract boolean executeParse(AmassEvent amassEvent);

    /**
     * ��Ҫִ�е����ݳ�ʼ������ ��Ҫ��д
     */
    public abstract void initData();

    /**
     * ��CE������Ӧ���� ��ֵ
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
                    //������Դ��ڶ�Ӧ���������valueת��
                    value = attribute_key_value.get(entry.getKey()).get(entry.getValue());
                    if(value == null){
                        throw new Exception("�����Զ�Ӧ���û�д�key��"+entry.getValue()+"����ӳ��ֵ,ӳ���ϵΪ��"+ attribute_key_value.get(entry.getKey()));
                    }
                }
                setFeildByType(field,value,amassEvent);
            } catch (Exception e) {
                logger.error("ͨ���������ce����"+entry.getKey()+"��ֵ�쳣",e);
                return false;
            }
        }
        //��ӳɹ������Ĳ���id
        amassEvent.parseId = this.getParseScript().getId();
        return true;
    }

    /**
     * ���ݲ�ͬ������feild ��ֵ Ŀǰ֧�� String��Boolean��Integer��Long��int��long��boolean
     * @param field
     * @param value
     * @param amassEvent
     */
    public void setFeildByType(Field field,String value,AmassEvent amassEvent){
        if(field != null){
            String type = field.getGenericType().toString();
            // ���type�������ͣ���ǰ�����"class "�����������
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
                logger.error("���ݲ�ͬ�������Խ��и�ֵ�쳣",e);
            }

        }

    }

}
