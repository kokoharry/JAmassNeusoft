package com.neusoft.soc.nli.jamass.bean;

import java.io.Serializable;

/**
 * 解析对应码表
 */
public class ParseEncode implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 3983960767023617150L;

    // 码表key值
    private String keyval;

    // 码表key对应value
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
