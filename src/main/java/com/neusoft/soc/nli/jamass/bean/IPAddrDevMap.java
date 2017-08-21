package com.neusoft.soc.nli.jamass.bean;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Set;

public class IPAddrDevMap implements Serializable{
    /**
     * 设备IP
     */
    private String ip;
    /**
     * IP对应设备信息
     */
    private Set<DevPatternInfo> patterns;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Set<DevPatternInfo> getPatterns() {
        return patterns;
    }

    public void setPatterns(Set<DevPatternInfo> patterns) {
        this.patterns = patterns;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
