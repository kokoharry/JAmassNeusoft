package com.neusoft.soc.nli.jamass.bean;

/**
 * Created by luyb on 2017/6/30.
 */
public enum SinkEnum {
    /**
     * log 输出日志
     */
    LogSink("LogSink",0),
    /**
     * kafka 传输到kafka消息中间件
     */
    KafkaSink("KafkaSink",1);
    /**
     * sink名称
     */
    private String name ;
    /**
     * sink序号
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
