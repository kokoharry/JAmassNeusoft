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

    // 事件状态需要仔细设计
    // Received 代表接收状态
    Received(0),
    // Filted 代表过滤状态
    Filted(1),
    // Identified代表识别状态
    Identidfied(2),
    // Unknown代表未知状态
    Unknown(3),
    // Parsed代表事件状态
    Parsed(4),
    // Aggregated代表聚合状态,可以采用&操作符解决问题
    Aggregated(5),
    // 失败状态
    Failed(6),
    // 发送状态
    Send(7);

    /**
     * 状态值
     */
    private final int status;

    /**
     * 构造函数
     * 
     * @param status
     *            状态值
     */
    private EventStatus(int status) {
        this.status = status;
    }

    /**
     * 
     * @return 获取状态值
     */
    public int getStatus() {
        return status;
    }
}
