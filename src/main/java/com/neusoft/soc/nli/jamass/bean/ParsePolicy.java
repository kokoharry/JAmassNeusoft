package com.neusoft.soc.nli.jamass.bean;

import java.io.Serializable;

/**
 * ��������
 */
public class ParsePolicy implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2220271644571163023L;
    // ����ID
    private String strateid;
    // ����״̬ 0-���ã�1-ͣ�ã�2-ɾ��������
    private Integer sstatus;
    // �豸����
    private String deviceType;
    // ����ģʽ 0-�ָ�����1-������ʽ������
    private Integer resomode;
    // �ָ���
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
