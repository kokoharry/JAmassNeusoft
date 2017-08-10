package com.neusoft.soc.nli.jamass.bean;

import java.io.Serializable;

/**
 * 解析策略
 */
public class ParsePolicy implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2220271644571163023L;
    // 策略ID
    private String strateid;
    // 策略状态 0-启用；1-停用；2-删除，必填
    private Integer sstatus;
    // 设备类型
    private String deviceType;
    // 解析模式 0-分隔符；1-正则表达式，必填
    private Integer resomode;
    // 分隔符
    private String separate;

    public String getStrateid() {
        return strateid;
    }

    public void setStrateid(String strateid) {
        this.strateid = strateid;
    }

    public Integer getSstatus() {
        return sstatus;
    }

    public void setSstatus(Integer sstatus) {
        this.sstatus = sstatus;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Integer getResomode() {
        return resomode;
    }

    public void setResomode(Integer resomode) {
        this.resomode = resomode;
    }

    public String getSeparate() {
        return separate;
    }

    public void setSeparate(String separate) {
        this.separate = separate;
    }

}
