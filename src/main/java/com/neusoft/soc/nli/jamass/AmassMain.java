package com.neusoft.soc.nli.jamass;

import com.neusoft.soc.nli.jamass.core.AmassEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * JAmass 主类，启动收集日志引擎
 * Created by luyb on 2017/6/5.
 */
public class AmassMain {

    private static Logger logger = LogManager.getLogger(AmassMain.class);

    /**
     * JAmass入口函数
     * @param args
     *            启动特定服务参数,[start|stop]或没有参数
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            // 正式版启动
            logger.info("Args: " + Arrays.toString(args));
            if (args[0].equals("stop")) {
                stopAmass();
            } else {
                startAmass();
            }
        }else{
            // debug 模式
            startAmass();
        }
        String ver = System.getProperty("$version", "");
        logger.info("Neusoft SOC: JAmass#" + ver);
        logger.info("(c)Copyright 2017-2020");

    }

    /**
     * 停止方法,采用读取进程ID,kill掉进程
     */
    private static void stopAmass() {

    }

    /**
     * 停止方法,采用读取进程ID,kill掉进程
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
