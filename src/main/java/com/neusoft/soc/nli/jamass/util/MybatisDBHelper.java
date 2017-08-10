package com.neusoft.soc.nli.jamass.util;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/9/9.
 *
 * Ϊ�˽��������Դ�������ļ����ö�μ��ص���ʽ������ͨ����дSqlSessionFactoryBuilderʵ��
 * Ŀǰ����Ҫ
 */
public class MybatisDBHelper {

    private static Logger logger = LogManager.getLogger(MybatisDBHelper.class);

    private static SqlSessionFactory sessionFactory;

    //��̬����飬��ʼ�����������ļ�
    static {
        InputStream is = null;
        try {
            //ʹ�������������mybatis�������ļ�����Ҳ���ع�����ӳ���ļ���
            is = MybatisDBHelper.class.getResourceAsStream("/MybatisConfig.xml");
            //����sqlSession�Ĺ���oracle sessionFactory
            sessionFactory = new SqlSessionFactoryBuilder().build(is,"mysql");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * ��ȡsession ����name��ȡ��Ӧ����Դ��session
     * @return SqlSession
     */
    public static SqlSessionFactory getSqlSession(String environmentName) {
        //������ִ��ӳ���ļ���sql��sqlSession
        return sessionFactory;
    }

}
