package com.neusoft.soc.nli.jamass.channel.parse;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * ����˳��Ŷ��������˳����н���
 * Created by kokoharry on 2017/5/12.
 */
public class SequenceParse extends ParseFunction {

    @Override
    public boolean executeParse(AmassEvent amassEvent) {
        boolean mustFlag = false;
        long a = System.nanoTime();
        // �Ƿ��� ˳��ż����Զ�Ӧ��ϵ�����Ҵ��ڷָ����
        if(this.getParseScript().getSplit_sign()!= null && this.getParseScript().getAttribute_sque() != null && this.getParseScript().getAttribute_sque().size() > 0){
            mustFlag = doParse(amassEvent ,this.getParseScript().getSplit_sign(),this.getParseScript().getAttribute_sque(),this.getParseScript().getAttribute_sque_must_value());
        }
        //����ͳ�ƹ��ܣ�ͳ�Ƴɹ�����
        if(mustFlag){
            this.getParseScript().add_parse_number();
        }
        logger.debug( this.getParseScript().getId()+"�����������Խű�����ʱ�䣺" + (System.nanoTime() - a));
        return mustFlag;
    }

    @Override
    public void initData() {
        this.getParseScript().setParse_type(0);
    }

    /**
     * �������ݽ���
     * @param amassEvent �ռ��¼�
     * @param splitSign ˳��ָ���
     * @param map   �ǹؼ����Ժ����ӳ��
     * @param mapMust �ؼ�������� �Լ�ӳ��ֵ
     * @return
     */
    private boolean doParse(AmassEvent amassEvent, String splitSign, Map<String,Integer> map ,Map<Integer,String> mapMust){
        Map<String,String> mapAttribute = new HashMap<String,String>();
        String[] strings = amassEvent.getMessage().split(escapeExprSpecialWord(splitSign));
        //ֻ���зָ��������ҷָ�����������Ҫ���Գ�
        if(strings != null && strings.length >= (getMaxIndexFromMap(map)+1)){
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String value = strings[entry.getValue()];
                String mustValue = mapMust.get(entry.getValue());
                //�йؼ����ԣ����ҹؼ����Դ��ڸ�ֵ
                if(mustValue != null && !mustValue.trim().equals("")){
                    //��˳��ţ�Ϊ�ؼ�����
                    if(mustValue.equals(value)){
                        //�ؼ����Զ�Ӧ ���ö�Ӧֵ
                        mapAttribute.put(entry.getKey(),value);
                    }else{
                        return false;
                    }
                }else{
                    // �ǹؼ�����
                    mapAttribute.put(entry.getKey(),value);
                }
            }
            return setCollectorEventAttribute(amassEvent,mapAttribute,this.getParseScript().getAttribute_key_value());
        }else{
            return false;
        }
    }

    /**
     * ��map�л�ȡ����valueֵ
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
     * ת�����������ַ� ��$()*+.[]?\^{},|��
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
