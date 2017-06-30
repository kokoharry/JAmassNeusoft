/*-
 * Copyright (c) 2008-2010 Neusoft SOC
 * All rights reserved. 
 * ProtocolType.java
 * Date: 2010-06-21
 * Author: Zhuang Di
 */
package com.neusoft.soc.nli.jamass.bean;

/**
 * JAmass ���࣬�����ռ���־����
 * Created by luyb on 2017/6/5.
 */
public enum ProtocolType {
    /**
     * Э���б�Ŀǰ֧��syslog �� snmp �� file
     */
    SysLog("SYSLOG",0),
    SnmpTrap("SNMPTRAP",1),
    FileTrace("FILETRACE",2),
    OtherProtocol("OTHERPROTOCOL",100);
    /**
     * Э������
     */
    private String name ;
    /**
     * Э�����
     */
    private int index ;

    private ProtocolType(String name , int index ){
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
