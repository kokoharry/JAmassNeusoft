package com.neusoft.soc.nli.jamass.sink;

import com.neusoft.soc.nli.jamass.core.AmassConstant;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by luyb on 2017/6/9.
 */
public class SinkFactory {

    /**
     * ��־��
     */
    private static Logger logger = LogManager.getLogger(SinkFactory.class);

    /**
     * ����sinkname ����sinkʵ��
     * @param sinkName
     * @return
     */
    public static ISink createSink(String sinkName){
        String sinkClass = AmassConstant.SINK_PACKAGE_PATH + "."+sinkName;
//        logger.debug("sinkClass = " + sinkClass);
        try {
            Class onwClass = Class.forName(sinkClass);
            ISink iSink =  (ISink)onwClass.newInstance();
            iSink.init();
            return iSink;
        } catch (Exception e) {
            logger.error("sink ���� ����ָ����sink:"+sinkClass+"�����쳣",e);
        }
        return null;
    }
}
