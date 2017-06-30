package com.neusoft.soc.nli.jamass.bean;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.regex.Pattern;

public class DevPatternInfo implements Serializable{
    /**
     * serialVersionUID
     */
    public static final long serialVersionUID = 6995409482209870060L;
    /**
     * ������
     */
    private int sortId;
    /**
     * �豸���ͱ��
     */
    private int devId;
    /**
     * �豸����������ʽ
     */
    private Pattern pattern;
    /**
     * @param devId
     *            the devId to set
     */
    public void setDevId(int devId) {
        this.devId = devId;
    }
    /**
     * @return the devId
     */
    public int getDevId() {
        return devId;
    }
    /**
     * @param pattern
     *            the pattern to set
     */
    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
    /**
     * @return the pattern
     */
    public Pattern getPattern() {
        return pattern;
    }
    /**
     * @param sortId
     *            the sortId to set
     */
    public void setSortId(int sortId) {
        this.sortId = sortId;
    }
    /**
     * @return the sortId
     */
    public int getSortId() {
        return sortId;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
