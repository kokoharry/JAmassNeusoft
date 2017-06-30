package com.neusoft.soc.nli.jamass.sink;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;

/**
 * Created by luyb on 2017/6/9.
 */
public interface ISink {

    /**
     * sink ��ʼ��
     */
    void init();

    /**
     * ͨ��ָ����sink send����
     * @param amassEvent
     */
    void send(AmassEvent amassEvent);

    /**
     * sink �쳣����
     */
    void doException();

    /**
     * ���sinkĿ��״̬
     * @return
     */
    boolean checkConnect();

}
