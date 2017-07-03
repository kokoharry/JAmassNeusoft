package com.neusoft.soc.nli.jamass.bean;

/**
 * Created by luyb on 2017/6/30.
 */
public enum SinkEnum {
    /**
     * log �����־
     */
    LogSink("LogSink",0),
    /**
     * kafka ���䵽kafka��Ϣ�м��
     */
    KafkaSink("KafkaSink",1);
    /**
     * sink����
     */
    private String name ;
    /**
     * sink���
     */
    private int index ;

    private SinkEnum(String name , int index ){
        this.name = name ;
        this.index = index ;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public int getIndex() {
        return index;
    }
    public void setIndex(int index) {
        this.index = index;
    }
}
