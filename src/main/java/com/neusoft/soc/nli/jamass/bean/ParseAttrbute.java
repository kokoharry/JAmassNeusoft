package com.neusoft.soc.nli.jamass.bean;

import java.io.Serializable;

/**
 * 加载解析策略对应属性
 */
public class ParseAttrbute implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 2054179676487808648L;
    // 属性ID
    private String attrid;
    // 序号
    private Integer seqid;
    // 正则表达式
    private String regular;
    // 解析属性 
    private String paraattr;
    // 关键属性 0-是；1-否
    private Integer keyattr;
    // 关键值
    private String keyval;

    public String getAttrid() {
        return attrid;
    }

    public void setAttrid(String attrid) {
        this.attrid = attrid;
    }

    public Integer getSeqid() {
        return seqid;
    }

    public void setSeqid(Integer seqid) {
        this.seqid = seqid;
    }

    public String getRegular() {
        return regular;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getParaattr() {
        return paraattr;
    }

    public void setParaattr(String paraattr) {
        this.paraattr = paraattr;
    }

    public Integer getKeyattr() {
        return keyattr;
    }

    public void setKeyattr(Integer keyattr) {
        this.keyattr = keyattr;
    }

    public String getKeyval() {
        return keyval;
    }

    public void setKeyval(String keyval) {
        this.keyval = keyval;
    }


    
}
