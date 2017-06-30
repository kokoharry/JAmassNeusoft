package com.neusoft.soc.nli.jamass.sink;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.source.ICollectSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by luyb on 2017/6/9.
 */
public class LogSink implements ISink{

    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(LogSink.class);

    /**
     * 构造方法，执行初始化
     */
    public LogSink(){
        init();
    }

    @Override
    public void init() {

    }

    @Override
    public void send(AmassEvent amassEvent) {
        logger.info("LogSink receive evnet:"+amassEvent);
    }

    @Override
    public void doException() {

    }

    @Override
    public boolean checkConnect() {
        return false;
    }
}
