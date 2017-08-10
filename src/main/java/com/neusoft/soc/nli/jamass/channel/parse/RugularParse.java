package com.neusoft.soc.nli.jamass.channel.parse;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ����������ʽ�����õ����Զ�Ӧ�������
 * Created by kokoharry on 2017/5/12.
 */
public class RugularParse extends ParseFunction {

    @Override
    public boolean executeParse(AmassEvent amassEvent) {
        boolean mustFlag = false;
        long a = System.nanoTime();
        // �Ƿ��� �ؼ�����
        if(this.getParseScript().getMustPatternMap() != null && this.getParseScript().getMustPatternMap().size() > 0){
            mustFlag = doParse(amassEvent ,this.getParseScript().getMustPatternMap(),true);
        }
        //����ͳ�ƹ��ܣ�ͳ�Ƴɹ�����
        if(mustFlag){
            this.getParseScript().add_parse_number();
        }
        // �ؼ����Խ����ɹ� �Ƿ��� �ǹؼ�����
        if(mustFlag && this.getParseScript().getPatternMap() != null && this.getParseScript().getPatternMap().size() > 0){
            doParse(amassEvent ,this.getParseScript().getPatternMap(),false);
        }
      logger.debug( this.getParseScript().getId()+"�����������Խű�����ʱ�䣺" + (System.nanoTime() - a));
       return mustFlag;
    }

    @Override
    public void initData() {
        this.getParseScript().setParse_type(1);
        Map<String,String> mapMust = this.getParseScript().getAttribute_regular_mapping_must();
        //�ؼ�����
        if( mapMust != null && mapMust.size() > 0){
            Map<String,Pattern> map = new HashMap<String,Pattern>();
            for (Map.Entry<String, String> entry : mapMust.entrySet()) {
                map.put(entry.getKey(),Pattern.compile(entry.getValue()));
            }
            logger.debug("���ɹؼ�����������ʽΪ��" + map);
            this.getParseScript().setMustPatternMap(map);
        }
        Map<String,String> map = this.getParseScript().getAttribute_regular_mapping();
        //�ǹؼ�����
        if(map != null && map.size() > 0){
            Map<String,Pattern> mapTemp = new HashMap<String,Pattern>();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                mapTemp.put(entry.getKey(),Pattern.compile(entry.getValue()));
            }
            logger.debug("���ɷǹؼ�����������ʽΪ��" + mapTemp);
            this.getParseScript().setPatternMap(mapTemp);
        }
    }

    /**
     * �������ݽ���
     * @param amassEvent �¼�����
     * @param map ���Ժ���������б�
     * @param type �Ƿ�ؼ����� ���ؼ�������һ��δ���������ʧ��
     * @return �������
     */
    private boolean doParse(AmassEvent amassEvent, Map<String ,Pattern> map,boolean type){
        Map<String,String> mapAttribute = new HashMap<String,String>();
        //ѭ�����Խ����б�
        for (Map.Entry<String, Pattern> entry : map.entrySet()) {
            Pattern pattern = entry.getValue();
            Matcher matcher = pattern.matcher(amassEvent.getRaw());
            //ֻ����һ�Σ���������ȡ
            if(matcher.find()){
                logger.debug(entry.getKey()+":"+matcher.group(1));
                mapAttribute.put(entry.getKey(),matcher.group(1));
            }else if(type){
                //����ؼ����ԣ���û���ҵ�����ֱ�ӷ���ʧ��
                return false;
            }
        }
        //��������ɹ�����������Ե�Ce����ĸ�ֵ
        return setCollectorEventAttribute(amassEvent,mapAttribute,this.getParseScript().getAttribute_key_value());
    }
}
