package com.neusoft.soc.nli.jamass.channel.forward;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import com.neusoft.soc.nli.jamass.bean.EventStatus;
import com.neusoft.soc.nli.jamass.core.AmassEngine;
import com.neusoft.soc.nli.jamass.sink.SinkThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by luyb on 2017/6/8.
 */
public class Forward implements Runnable{

    /**
     * 日志类
     */
    private static Logger logger = LogManager.getLogger(Forward.class);

    private EventStatus eventStatus;

    private Queue<AmassEvent> queue;

    private List<String> sinkList;

    public Forward(EventStatus eventStatus,List sinkList){
        this.eventStatus = eventStatus;
        this.sinkList = sinkList;
    }

    @Override
    public void run() {
        Thread.currentThread().setName(eventStatus.name()+"-Forward-Thread");
        switch (eventStatus){
            case Unknown:
                queue = AmassEngine.getInstance().getUnknowLogQueue();
                break;
            case Parsed:
                queue = AmassEngine.getInstance().getEventQueue();
                break;
            case Identidfied:
                queue = null;
                break;
            default:
                queue = null;
        }
        if(queue != null){
            while(true){
                try {
                    AmassEvent amassEvent = ((LinkedBlockingQueue<AmassEvent>)queue).take();
                    //TODO: 找一共多少个sink 注册
                    for(String sinkName : sinkList){
                        SinkThread sinkThread = new SinkThread(sinkName,amassEvent.clone());
                        AmassEngine.getInstance().getSinkPool().execute(sinkThread);
                    }
                } catch (InterruptedException e) {
                    logger.error("转发队列获取事件异常",e);
                }
            }
        }
    }
}
