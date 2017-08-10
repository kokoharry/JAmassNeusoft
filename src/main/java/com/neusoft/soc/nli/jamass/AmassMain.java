package com.neusoft.soc.nli.jamass;

import com.neusoft.soc.nli.jamass.core.AmassEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * JAmass ���࣬�����ռ���־����
 * Created by luyb on 2017/6/5.
 */
public class AmassMain {

    private static Logger logger = LogManager.getLogger(AmassMain.class);

    /**
     * JAmass��ں���
     * @param args
     *            �����ض��������,[start|stop]��û�в���
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            // ��ʽ������
            logger.info("Args: " + Arrays.toString(args));
            if (args[0].equals("stop")) {
                stopAmass();
            } else {
                startAmass();
            }
        }else{
            // debug ģʽ
            startAmass();
        }
        String ver = System.getProperty("$version", "");
        logger.info("Neusoft SOC: JAmass#" + ver);
        logger.info("(c)Copyright 2017-2020");

    }

    /**
     * ֹͣ����,���ö�ȡ����ID,kill������
     */
    private static void stopAmass() {

    }

    /**
     * ֹͣ����,���ö�ȡ����ID,kill������
     */
    private static void startAmass() {
        AmassEngine.getInstance().initAmassEngine();
        AmassEngine.getInstance().startThreadAll();
        new Timer("Configration File ").schedule(new TimerTask() {
            public void run() {
              System.out.println(AmassEngine.getInstance().count);
            }
        }, 5*1000, 5*1000);
    }
}
