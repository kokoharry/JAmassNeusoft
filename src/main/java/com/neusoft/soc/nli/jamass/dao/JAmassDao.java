package com.neusoft.soc.nli.jamass.dao;

import com.neusoft.soc.nli.jamass.bean.ParseAttrbute;
import com.neusoft.soc.nli.jamass.bean.ParseEncode;
import com.neusoft.soc.nli.jamass.bean.ParsePolicy;
import com.neusoft.soc.nli.jamass.bean.ParseScript;
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
     * ��־��
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
     * ��ѯ���õĽ�������
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<ParsePolicy> selectParsePolicy(){
        List<ParsePolicy> list = null;
        try {
            list = sqlSession.selectList("selectParsePolicy");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * ��ѯ���õĽ�������id
     *
     * @return
     */
    public static ParsePolicy selectParsePolicyById(String policyId){
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
     * ͨ������id��ѯ��������ֵ
     * @param policyId
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<ParseAttrbute> selectParseAttrbute(String policyId){
        List<ParseAttrbute> list = null;
        try {
            list = sqlSession.selectList("selectParseAttrbute", policyId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

    /**
     * ͨ������ֵ��ѯ��Ӧ���ֵ
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<ParseEncode> selectParseEncode(String attrId){
        List<ParseEncode> list = null;
        try {
            list = sqlSession.selectList("selectParseEncode", attrId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return list;
    }

}
