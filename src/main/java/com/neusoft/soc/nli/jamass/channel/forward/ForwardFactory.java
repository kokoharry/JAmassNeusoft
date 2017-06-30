package com.neusoft.soc.nli.jamass.channel.forward;

import com.neusoft.soc.nli.jamass.bean.EventStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by luyb on 2017/6/8.
 */
public class ForwardFactory {
    /**
     * 日志类
     */
    public static Logger logger = LogManager.getLogger(ForwardFactory.class);
    /**
     * @return 日志格式化对象，若失败则返回null
     */
    public static Forward createForward(EventStatus eventStatus, List<String> sinkList) {
        return new Forward(eventStatus,sinkList);
    }
}
