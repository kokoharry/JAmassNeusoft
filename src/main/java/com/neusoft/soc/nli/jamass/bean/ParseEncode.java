package com.neusoft.soc.nli.jamass.bean;

import java.io.Serializable;

/**
 * ������Ӧ���
 */
public class ParseEncode implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3983960767023617150L;

    // ���keyֵ
    private String keyval;

    // ���key��Ӧvalue
    private String val;

    public String getKeyval() {
        return keyval;
    }

    public void setKeyval(String keyval) {
        this.keyval = keyval;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }
    
}
