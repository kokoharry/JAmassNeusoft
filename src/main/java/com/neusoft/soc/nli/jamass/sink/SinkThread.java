package com.neusoft.soc.nli.jamass.sink;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by luyb on 2017/6/9.
 */
public class SinkThread implements Runnable {

    private String sinkName;

    private AmassEvent amassEvent;

    public SinkThread(String sinkName,AmassEvent amassEvent){
        this.sinkName = sinkName;
        this.amassEvent = amassEvent;
    }

    /**
     * »’÷æ¿‡
     */
    private static Logger logger = LogManager.getLogger(SinkThread.class);

    @Override
    public void run() {
        Thread.currentThread().setName(sinkName+"-Thread");
        ISink sink = SinkFactory.createSink(sinkName);
        sink.send(amassEvent);
    }
}
