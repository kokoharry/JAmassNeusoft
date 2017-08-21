package com.neusoft.soc.nli.jamass.dao;

import com.neusoft.soc.nli.jamass.bean.*;
import com.neusoft.soc.nli.jamass.util.MybatisDBHelper;
import com.neusoft.soc.nli.jamass.util.RegularExpressionUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luyb on 2017/8/9.
 */
public class JAmassDao{
    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(JAmassDao.class);

    private static JAmassDao jAmassDao;

    private static SqlSession sqlSession;

    private JAmassDao(){
        sqlSession = MybatisDBHelper.getSqlSession("").openSession();
    }

    public static JAmassDao getInstance(){
        if(jAmassDao == null){
            jAmassDao = new JAmassDao();
        }
        return jAmassDao;
    }

    /**
     * 查询启用的解析策略
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ParsePolicy> selectParsePolicy(){
        List<ParsePolicy> list = null;
        try {
            list = sqlSession.selectList("selectParsePolicy");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * 查询启用的解析策略id
     *
     * @return
     */
    public ParsePolicy selectParsePolicyById(String policyId){
        ParsePolicy parsePolicy = null;
        try {
            Object obj = sqlSession.selectOne("selectParsePolicyById", policyId);
            if(obj != null){
                parsePolicy = (ParsePolicy)obj;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return parsePolicy;
    }

    /**
     * 通过策略id查询解析属性值
     * @param policyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ParseAttrbute> selectParseAttrbute(String policyId){
        List<ParseAttrbute> list = null;
        try {
            list = sqlSession.selectList("selectParseAttrbute", policyId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * 通过属性值查询对应码表值
     * @return
     */
    @SuppressWarnings("unchecked")
    public List<ParseEncode> selectParseEncode(String attrId){
        List<ParseEncode> list = null;
        try {
            list = sqlSession.selectList("selectParseEncode", attrId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * 获取数据库中IP与设备类型映射，一个设备可能映射多个设备
     * @return
     */
    public List<IPAddrDevMap> getManualMapping() {
        List<IPAddrDevMap> list = null;
        try {
            list = sqlSession.selectList("selectIpDevMappings");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

}
