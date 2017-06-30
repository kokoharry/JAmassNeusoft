package com.neusoft.soc.nli.jamass.sink;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;

/**
 * Created by luyb on 2017/6/9.
 */
public interface ISink {

    /**
     * sink 初始化
     */
    void init();

    /**
     * 通过指定的sink send数据
     * @param amassEvent
     */
    void send(AmassEvent amassEvent);

    /**
     * sink 异常处理
     */
    void doException();

    /**
     * 检查sink目标状态
     * @return
     */
    boolean checkConnect();

}
