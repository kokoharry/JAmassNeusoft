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
 * 解析策略加载
 *
 */
public class ParseService {

    /**
     * 日志类
     */
    public static Logger logger = LogManager.getLogger(ParseService.class);
    
    
    public static void main(String[] args) {
        Map<Integer, Map<String, ParseScript>> map = new ParseService().initPatternMap();
        System.out.println(map.toString());
    }
    
    /**
     * 初始化策略
     * 
     * @return
     */
    public Map<Integer, Map<String, ParseScript>> initPatternMap() {

        Map<Integer, Map<String, ParseScript>> patternMap = new HashMap<Integer, Map<String, ParseScript>>();

        //获取所有策略
        List<ParsePolicy> policys = JAmassDao.getInstance().selectParsePolicy();

        if (policys == null || policys.size() == 0) {
            logger.error("数据库获取解析脚本失败,内容为空");
            return patternMap;
        }

        for (ParsePolicy policy : policys) {
            Map<String, ParseScript> parseMap = new LinkedHashMap<>();
            ParseScript ps = fillParseScript(policy);
            parseMap.put(ps.getId(), ps);
            // 组合
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
     * 根据策略id查询策略
     * @param policyId
     * @return
     */
    public ParsePolicy selectParsePolicyById(String policyId){
        return JAmassDao.getInstance().selectParsePolicyById(policyId);
    }
    
    
    /**
     * 填充解析脚本
     * 
     * @param policy
     * @return
     */
    public ParseScript fillParseScript(ParsePolicy policy) {

        ParseScript ps = new ParseScript();
        ps.setId(policy.getStrateid());
        ps.setDev_type(Integer.parseInt(policy.getDeviceType()));
        
        //正则方式
        if(policy.getResomode() == 1){
            ps.setParseFunction(new RugularParse());  
        }else{
            //顺序解析，复制分隔符
            ps.setSplit_sign(policy.getSeparate());
            ps.setParseFunction(new SequenceParse());  
        }

        // 查询策略id对应的属性 填充属性start
        List<ParseAttrbute> attrbutes = JAmassDao.getInstance().selectParseAttrbute(policy
            .getStrateid());
        
        // 正则关键属性map
        Map<String, String> mustMap = new HashMap<String, String>();
        // 正则非关键属性map
        Map<String, String> commonMap = new HashMap<String, String>();
        //  顺序非关键属性map
        Map<String, Integer> seqCommonMap = new HashMap<String, Integer>();
        //  顺序关键属性 顺序号 和 对应的固定值映射
        Map<Integer, String> seqMustMap = new HashMap<Integer, String>();
        // 属性对应码表map
        Map<String, Map<String, String>> encodeMap = new HashMap<String, Map<String, String>>();
        ps.setAttribute_key_value(encodeMap);
        
        if (attrbutes != null && attrbutes.size() > 0) {
            for (ParseAttrbute attr : attrbutes) {
                // 关键属性 0-是；1-否
                if (attr.getKeyattr() == 1) {
                    //正则方式
                    if(policy.getResomode() == 1){
                        commonMap.put(attr.getParaattr(), attr.getRegular()); 
                    }else{
                        seqCommonMap.put(attr.getParaattr(), attr.getSeqid()-1);
                    }
                } else {
                    //正则方式
                    if(policy.getResomode() == 1){
                        mustMap.put(attr.getParaattr(), attr.getRegular());
                    }else{
                        seqCommonMap.put(attr.getParaattr(), attr.getSeqid()-1);
                        seqMustMap.put(attr.getSeqid()-1, attr.getKeyval());
                    }
                }
                //赋值码表信息
                fillParseEncode(attr, ps);
            }
        }
        
        ps.setAttribute_regular_mapping_must(mustMap);
        ps.setAttribute_regular_mapping(commonMap);
        ps.setAttribute_sque(seqCommonMap);
        ps.setAttribute_sque_must_value(seqMustMap);

        // 查询策略id对应的属性 填充属性end
        ps.getParseFunction().initData();

        return ps;
    }

    /**
     * 填充码表信息
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
                // 码表信息map
                map_value.put(code.getKeyval(), code.getVal());
                ps.getAttribute_key_value().put(attr.getParaattr(), map_value);
            }
        }
    }

}
