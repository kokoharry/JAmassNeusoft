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
 * 为了解决多数据源，配置文件采用多次加载的形式，可以通过重写SqlSessionFactoryBuilder实现
 * 目前不需要
 */
public class MybatisDBHelper {

    private static Logger logger = LogManager.getLogger(MybatisDBHelper.class);

    private static SqlSessionFactory sessionFactory;

    //静态代码块，初始化加载配置文件
    static {
        InputStream is = null;
        try {
            //使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
            is = MybatisDBHelper.class.getResourceAsStream("/MybatisConfig.xml");
            //构建sqlSession的工厂oracle sessionFactory
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
     * 获取session 根据name获取对应数据源的session
     * @return SqlSession
     */
    public static SqlSessionFactory getSqlSession(String environmentName) {
        //创建能执行映射文件中sql的sqlSession
        return sessionFactory;
    }

}
