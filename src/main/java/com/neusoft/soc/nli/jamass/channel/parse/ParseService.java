package com.neusoft.soc.nli.jamass.channel.parse;


import com.neusoft.soc.nli.jamass.bean.ParseAttrbute;
import com.neusoft.soc.nli.jamass.bean.ParseEncode;
import com.neusoft.soc.nli.jamass.bean.ParsePolicy;
import com.neusoft.soc.nli.jamass.bean.ParseScript;
import com.neusoft.soc.nli.jamass.dao.JAmassDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * �������Լ���
 *
 */
public class ParseService {

    /**
     * ��־��
     */
    public static Logger logger = LogManager.getLogger(ParseService.class);
    
    
    public static void main(String[] args) {
        Map<Integer, Map<String, ParseScript>> map = new ParseService().initPatternMap();
        System.out.println(map.toString());
    }
    
    /**
     * ��ʼ������
     * 
     * @return
     */
    public Map<Integer, Map<String, ParseScript>> initPatternMap() {

        Map<Integer, Map<String, ParseScript>> patternMap = new HashMap<Integer, Map<String, ParseScript>>();

        //��ȡ���в���
        List<ParsePolicy> policys = JAmassDao.getInstance().selectParsePolicy();

        if (policys == null || policys.size() == 0) {
            logger.error("���ݿ��ȡ�����ű�ʧ��,����Ϊ��");
            return patternMap;
        }

        for (ParsePolicy policy : policys) {
            Map<String, ParseScript> parseMap = new LinkedHashMap<>();
            ParseScript ps = fillParseScript(policy);
            parseMap.put(ps.getId(), ps);
            // ���
            Map<String, ParseScript> temp = patternMap.get(Integer.parseInt(policy.getDeviceType()));
            if (temp == null) {
                patternMap.put(Integer.parseInt(policy.getDeviceType()),
                    parseMap);
            } else {
                temp.putAll(parseMap);
            }

        }

        return patternMap;
    }

    /**
     * ���ݲ���id��ѯ����
     * @param policyId
     * @return
     */
    public ParsePolicy selectParsePolicyById(String policyId){
        return JAmassDao.getInstance().selectParsePolicyById(policyId);
    }
    
    
    /**
     * �������ű�
     * 
     * @param policy
     * @return
     */
    public ParseScript fillParseScript(ParsePolicy policy) {

        ParseScript ps = new ParseScript();
        ps.setId(policy.getStrateid());
        ps.setDev_type(Integer.parseInt(policy.getDeviceType()));
        
        //����ʽ
        if(policy.getResomode() == 1){
            ps.setParseFunction(new RugularParse());  
        }else{
            //˳����������Ʒָ���
            ps.setSplit_sign(policy.getSeparate());
            ps.setParseFunction(new SequenceParse());  
        }

        // ��ѯ����id��Ӧ������ �������start
        List<ParseAttrbute> attrbutes = JAmassDao.getInstance().selectParseAttrbute(policy
            .getStrateid());
        
        // ����ؼ�����map
        Map<String, String> mustMap = new HashMap<String, String>();
        // ����ǹؼ�����map
        Map<String, String> commonMap = new HashMap<String, String>();
        //  ˳��ǹؼ�����map
        Map<String, Integer> seqCommonMap = new HashMap<String, Integer>();
        //  ˳��ؼ����� ˳��� �� ��Ӧ�Ĺ̶�ֵӳ��
        Map<Integer, String> seqMustMap = new HashMap<Integer, String>();
        // ���Զ�Ӧ���map
        Map<String, Map<String, String>> encodeMap = new HashMap<String, Map<String, String>>();
        ps.setAttribute_key_value(encodeMap);
        
        if (attrbutes != null && attrbutes.size() > 0) {
            for (ParseAttrbute attr : attrbutes) {
                // �ؼ����� 0-�ǣ�1-��
                if (attr.getKeyattr() == 1) {
                    //����ʽ
                    if(policy.getResomode() == 1){
                        commonMap.put(attr.getParaattr(), attr.getRegular()); 
                    }else{
                        seqCommonMap.put(attr.getParaattr(), attr.getSeqid()-1);
                    }
                } else {
                    //����ʽ
                    if(policy.getResomode() == 1){
                        mustMap.put(attr.getParaattr(), attr.getRegular());
                    }else{
                        seqCommonMap.put(attr.getParaattr(), attr.getSeqid()-1);
                        seqMustMap.put(attr.getSeqid()-1, attr.getKeyval());
                    }
                }
                //��ֵ�����Ϣ
                fillParseEncode(attr, ps);
            }
        }
        
        ps.setAttribute_regular_mapping_must(mustMap);
        ps.setAttribute_regular_mapping(commonMap);
        ps.setAttribute_sque(seqCommonMap);
        ps.setAttribute_sque_must_value(seqMustMap);

        // ��ѯ����id��Ӧ������ �������end
        ps.getParseFunction().initData();

        return ps;
    }

    /**
     * ��������Ϣ
     * 
     * @param attr
     * @param ps
     */
    private void fillParseEncode(ParseAttrbute attr, ParseScript ps) {
        
        List<ParseEncode> encodes = JAmassDao.getInstance().selectParseEncode(attr
            .getAttrid());
        Map<String, String> map_value = new HashMap<String, String>();
        if (encodes != null && encodes.size() > 0) {
            for (ParseEncode code : encodes) {
                // �����Ϣmap
                map_value.put(code.getKeyval(), code.getVal());
                ps.getAttribute_key_value().put(attr.getParaattr(), map_value);
            }
        }
    }

}
