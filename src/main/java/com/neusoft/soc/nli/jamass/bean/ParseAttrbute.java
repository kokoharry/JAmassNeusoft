package com.neusoft.soc.nli.jamass.bean;

import java.io.Serializable;

/**
 * ���ؽ������Զ�Ӧ����
 */
public class ParseAttrbute implements Serializable{
    /**
     * 
     */
    private static final long serialVersionUID = 2054179676487808648L;
    // ����ID
    private String attrid;
    // ���
    private Integer seqid;
    // ������ʽ
    private String regular;
    // �������� 
    private String paraattr;
    // �ؼ����� 0-�ǣ�1-��
    private Integer keyattr;
    // �ؼ�ֵ
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
