/*-
 * Copyright (c) 2008-2010 Neusoft SOC
 * All rights reserved. 
 * EventStatus.java
 * Date: 2010-06-10
 * Author: Zhuang Di
 */
package com.neusoft.soc.nli.jamass.bean;

/**
 * EventStatus
 * <p>
 * Date: 2010-06-10,14:15:56 +0800
 * 
 * @author Zhuang Di
 * @version 1.0
 */
public enum EventStatus {

    // �¼�״̬��Ҫ��ϸ���
    // Received �������״̬
    Received(0),
    // Filted �������״̬
    Filted(1),
    // Identified����ʶ��״̬
    Identidfied(2),
    // Unknown����δ֪״̬
    Unknown(3),
    // Parsed�����¼�״̬
    Parsed(4),
    // Aggregated����ۺ�״̬,���Բ���&�������������
    Aggregated(5),
    // ʧ��״̬
    Failed(6),
    // ����״̬
    Send(7);

    /**
     * ״ֵ̬
     */
    private final int status;

    /**
     * ���캯��
     * 
     * @param status
     *            ״ֵ̬
     */
    private EventStatus(int status) {
        this.status = status;
    }

    /**
     * 
     * @return ��ȡ״ֵ̬
     */
    public int getStatus() {
        return status;
    }
}
