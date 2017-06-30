/*-
 * Copyright (c) 2008-2010 Neusoft SOC
 * All rights reserved 
 * SOCEventjava
 * Date: 2010-06-04
 * Author: Song Sun
 */

package com.neusoft.soc.nli.jamass.bean;


import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * JAmass 主类，启动收集日志引擎
 * Created by luyb on 2017/6/5.
 */
public class AmassEvent implements Serializable ,Cloneable{

    /**
     * serialVersionUID
     */
    public static final long serialVersionUID = 6995409482109870060L;
    /**
     * Raw Log Level, 0-7: Emergency, Alert, Critical, Error, Warning, Notice,
     * Info , Debug
     */
    public int logLevel;
    /**
     * 收集协议
     */
    public ProtocolType receiveProtocol;
    /**
     * 事件状态
     */
    public EventStatus status;
    /**
     * 未知日志状态
     * <p>
     * true : 已识别，未格式化或未生成事件
     * <p>
     * false: 未识别
     */
    public boolean unknownFlag = true;
    /**
     * 编码标志位
     * <p>
     * true : 单IP单编码
     * <p>
     * false: 单IP多编码
     */
    public boolean charsetFlag = true;
    /**
     * trap日志编码格式
     */
    public String snmptrapCharset;
    /**
     * IP标志位
     * <p>
     * true : IPv4
     * <p>
     * false: IPv6
     */
    public boolean isIpv4Flag = true;
    /**
     * 接收源端口
     */
    public int sport;
    /**
     * 日志类型
     */
    public ProtocolType proType;
    /**
     * 聚合日志数目
     */
    public AtomicInteger aggretenum;

    /**
     * 忽略事件
     */
    public boolean omit;
    /**
     * 解析策略id 解析使用的策略id 可以为空，如果为空 则 未解析成功或者使用就解析方式 add by kokoharry
     */
    public String parseId;
    /**
     * 事件编号 Event ID, Primary Key
     */
    public String id;
    /**
     * 告警等级 Event Level, 0-7: Emergency, Alert, Critical, Error, Warning,
     * Notice, Info , Debug
     */
    public int level;
    /**
     * 告警种类编号 Event Kind: 1,Denial of Service; 2,Reconnaissance;
     * 3,Access/Authentication/Authorization; 4,Application Exploit; 5,Evasion;
     * 6,Virus/Trojan; 7, Unknown/Suspicious; 8,System Status/Configuration;
     * 9,Policy Violations; 10,Customized
     */
    public int kind;
    /**
     * 告警编号 Event Type, about 100 types
     */
    public int type;
    /**
     * 设备种类编号 Device category
     */
    public int category;
    /**
     * 设备类型编号 Device type
     */
    public int device;
    /**
     * 日志编号 Log code via device defination
     */
    public String code;
    /**
     * 协议 Network protocol
     */
    public String protocol;
    /**
     * 发生源ip Producer's IP
     */
    public String ip0;
    /**
     * 发生源地址 Producer's address
     */
    public long addr0;
    /**
     * 代理编号 Agent's ID
     */
    public String agent;
    /**
     * 原始日志编号 Raw log's ID made by Agent
     */
    public long rawid;
    /**
     * 原始日志 Raw log object, maybe String
     */
    public String raw;
   /**
    * 处理建议
    */
   public String solutions;
   /**
    * 未知原始日志
    */
   public int unknowLog;
    /**
     * Asset or entity associated with the event
     */
    public String object;
    /**
     * 源ip Source's IP
     */
    public String ip1;
    /**
     * 源地址 Source's IP in integer
     */
    public long addr1;
    /**
     * 源端口 Source's port
     */
    public int port1;
    /**
     * 源mac地址 Source's MAC address
     */
    public String mac1;
    /**
     * 源子网掩码 Source's subnet mask
     */
    public String mask1;
    /**
     * 目的ip Target's IP
     */
    public String ip2;
    /**
     * 目的地址 Target's IP in integer
     */
    public long addr2;
    /**
     * 目的端口 Target's port
     */
    public int port2;
    /**
     * 目的MAC地址 Target's MAC address
     */
    public String mac2;
    /**
     * 目的子网掩码 Target's subnet mask
     */
    public String mask2;
    /**
     * 其他ip Other's IP
     */
    public String ip3;
    /**
     * 其他地址 Other's IP in integer
     */
    public long addr3;
    /**
     * 其他端口 Other's port
     */
    public int port3;
    /**
     * 其他MAC地址 Other's MAC address
     */
    public String mac3;
    /**
     * 其他子网掩码 Other's subnet mask
     */
    public String mask3;
    /**
     * 开始时间 Timestamp of the event
     */
    public long time;
    /**
     * 入库时间 enterTime
     */
    public long enterTime;
    /**
     * 持续时间 Duration of the event
     */
    public int duration;
    /**
     * 事件数量 Count of the event
     */
    public int count;
    /**
     * 用户名 Username associated with the event
     */
    public String user;
    /**
     * 主机 Host name associated with the event
     */
    public String host;
    /**
     * 进程 Process or path associated with the event
     */
    public String process;
    /**
     * 标题 Subject or title associated with the event
     */
    public String subject;
    /**
     * 关键字 Keyword or index associated with the event
     */
    public String keyword;
    /**
     * 动作 Action associated with the event
     */
    public String action;
    /**
     * 状态 Status associated with the event
     */
    public String state;
    /**
     * CVE编号 CVE number associated with the event
     */
    public String cve;
    /**
     * Bugtraq Bugtraq number associated with the event
     */
    public int bugtraq;
    /**
     * 规则编号 Rule number associated with the event
     */
    public int rule;
    /**
     * 策略 Policy associated with the event
     */
    public String policy;
    /**
     * String note 1
     */
    public String char1;
    /**
     * String note 2
     */
    public String char2;
    /**
     * String note 3
     */
    public String char3;
    /**
     * String note 4
     */
    public String char4;
    /**
     * Number note 1
     */
    public int number1;

    /**
     * Number note 2
     */
    public double number2;

    /**
     * 资产ID
     */
    public String assetId;
    /**
     * 监控器名称
     */
    public String monitorName;
    /**
     * 性能值
     */
    public float value;
    /**
     * 源ip责任单位
     */
    public String srcRuId;
    /**
     * 源ip信息系统
     */
    public String srcIsId;
    /**
     * 目的ip责任单位
     */
    public String dstRuId;
    /**
     * 目的ip信息系统
     */
    public String dstIsId;
    /**
     * 节点ID
     */
    public int mnId;
    /**
     * code+device
     */
    public String innerKey;
    /**
     * 链路Id
     */
    public int linkId;
    /**
     * 链路名称
     */
    public String linkName;
    /**
     * 事件类型
     */
    public String eventType;

    /**
     * 发生时间
     */
    public String enterDate;
    /**
     * 更新时间
     */
    public String updateDate;
    /**
     * 消息
     */
    public String message;
    /**
     * 事件名称
     */
   public String eventName;
   /**
    * 业务系统
    */
   public String bsName;
   /**
    * 资产名称
    */
   public String edName;
   /**
    * 应用告警Id
    */
   public String appAlarmId;
   /**
    * 资产价值
    */
   public int edValue;
   /**
    * 事件可信度
    */
   public int confidence;
   /**
    * 事件可信度
    */
   public int relatePercent;

    public long receiveTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getKind() {
        return kind;
    }

    public void setKind(int kind) {
        this.kind = kind;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getDevice() {
        return device;
    }

    public void setDevice(int device) {
        this.device = device;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getIp0() {
        return ip0;
    }

    public void setIp0(String ip0) {
        this.ip0 = ip0;
    }

    public long getAddr0() {
        return addr0;
    }

    public void setAddr0(long addr0) {
        this.addr0 = addr0;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public long getRawid() {
        return rawid;
    }

    public void setRawid(long rawid) {
        this.rawid = rawid;
    }

    public String getRaw() {
        return raw;
    }

    public void setRaw(String raw) {
        this.raw = raw;
    }

    public String getSolutions() {
        return solutions;
    }

    public void setSolutions(String solutions) {
        this.solutions = solutions;
    }

    public int getUnknowLog() {
        return unknowLog;
    }

    public void setUnknowLog(int unknowLog) {
        this.unknowLog = unknowLog;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getIp1() {
        return ip1;
    }

    public void setIp1(String ip1) {
        this.ip1 = ip1;
    }

    public long getAddr1() {
        return addr1;
    }

    public void setAddr1(long addr1) {
        this.addr1 = addr1;
    }

    public int getPort1() {
        return port1;
    }

    public void setPort1(int port1) {
        this.port1 = port1;
    }

    public String getMac1() {
        return mac1;
    }

    public void setMac1(String mac1) {
        this.mac1 = mac1;
    }

    public String getMask1() {
        return mask1;
    }

    public void setMask1(String mask1) {
        this.mask1 = mask1;
    }

    public String getIp2() {
        return ip2;
    }

    public void setIp2(String ip2) {
        this.ip2 = ip2;
    }

    public long getAddr2() {
        return addr2;
    }

    public void setAddr2(long addr2) {
        this.addr2 = addr2;
    }

    public int getPort2() {
        return port2;
    }

    public void setPort2(int port2) {
        this.port2 = port2;
    }

    public String getMac2() {
        return mac2;
    }

    public void setMac2(String mac2) {
        this.mac2 = mac2;
    }

    public String getMask2() {
        return mask2;
    }

    public void setMask2(String mask2) {
        this.mask2 = mask2;
    }

    public String getIp3() {
        return ip3;
    }

    public void setIp3(String ip3) {
        this.ip3 = ip3;
    }

    public long getAddr3() {
        return addr3;
    }

    public void setAddr3(long addr3) {
        this.addr3 = addr3;
    }

    public int getPort3() {
        return port3;
    }

    public void setPort3(int port3) {
        this.port3 = port3;
    }

    public String getMac3() {
        return mac3;
    }

    public void setMac3(String mac3) {
        this.mac3 = mac3;
    }

    public String getMask3() {
        return mask3;
    }

    public void setMask3(String mask3) {
        this.mask3 = mask3;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(long enterTime) {
        this.enterTime = enterTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCve() {
        return cve;
    }

    public void setCve(String cve) {
        this.cve = cve;
    }

    public int getBugtraq() {
        return bugtraq;
    }

    public void setBugtraq(int bugtraq) {
        this.bugtraq = bugtraq;
    }

    public int getRule() {
        return rule;
    }

    public void setRule(int rule) {
        this.rule = rule;
    }

    public String getPolicy() {
        return policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public String getChar1() {
        return char1;
    }

    public void setChar1(String char1) {
        this.char1 = char1;
    }

    public String getChar2() {
        return char2;
    }

    public void setChar2(String char2) {
        this.char2 = char2;
    }

    public String getChar3() {
        return char3;
    }

    public void setChar3(String char3) {
        this.char3 = char3;
    }

    public String getChar4() {
        return char4;
    }

    public void setChar4(String char4) {
        this.char4 = char4;
    }

    public int getNumber1() {
        return number1;
    }

    public void setNumber1(int number1) {
        this.number1 = number1;
    }

    public double getNumber2() {
        return number2;
    }

    public void setNumber2(double number2) {
        this.number2 = number2;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getMonitorName() {
        return monitorName;
    }

    public void setMonitorName(String monitorName) {
        this.monitorName = monitorName;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getSrcRuId() {
        return srcRuId;
    }

    public void setSrcRuId(String srcRuId) {
        this.srcRuId = srcRuId;
    }

    public String getSrcIsId() {
        return srcIsId;
    }

    public void setSrcIsId(String srcIsId) {
        this.srcIsId = srcIsId;
    }

    public String getDstRuId() {
        return dstRuId;
    }

    public void setDstRuId(String dstRuId) {
        this.dstRuId = dstRuId;
    }

    public String getDstIsId() {
        return dstIsId;
    }

    public void setDstIsId(String dstIsId) {
        this.dstIsId = dstIsId;
    }

    public int getMnId() {
        return mnId;
    }

    public void setMnId(int mnId) {
        this.mnId = mnId;
    }

    public String getInnerKey() {
        return innerKey;
    }

    public void setInnerKey(String innerKey) {
        this.innerKey = innerKey;
    }

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public String getLinkName() {
        return linkName;
    }

    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEnterDate() {
        return enterDate;
    }

    public void setEnterDate(String enterDate) {
        this.enterDate = enterDate;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getBsName() {
        return bsName;
    }

    public void setBsName(String bsName) {
        this.bsName = bsName;
    }

    public String getEdName() {
        return edName;
    }

    public void setEdName(String edName) {
        this.edName = edName;
    }

    public String getAppAlarmId() {
        return appAlarmId;
    }

    public void setAppAlarmId(String appAlarmId) {
        this.appAlarmId = appAlarmId;
    }

    public int getEdValue() {
        return edValue;
    }

    public void setEdValue(int edValue) {
        this.edValue = edValue;
    }

    public int getConfidence() {
        return confidence;
    }

    public void setConfidence(int confidence) {
        this.confidence = confidence;
    }

    public int getRelatePercent() {
        return relatePercent;
    }

    public void setRelatePercent(int relatePercent) {
        this.relatePercent = relatePercent;
    }

    public int getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public ProtocolType getReceiveProtocol() {
        return receiveProtocol;
    }

    public void setReceiveProtocol(ProtocolType receiveProtocol) {
        this.receiveProtocol = receiveProtocol;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    public boolean isUnknownFlag() {
        return unknownFlag;
    }

    public void setUnknownFlag(boolean unknownFlag) {
        this.unknownFlag = unknownFlag;
    }

    public boolean isCharsetFlag() {
        return charsetFlag;
    }

    public void setCharsetFlag(boolean charsetFlag) {
        this.charsetFlag = charsetFlag;
    }

    public String getSnmptrapCharset() {
        return snmptrapCharset;
    }

    public void setSnmptrapCharset(String snmptrapCharset) {
        this.snmptrapCharset = snmptrapCharset;
    }

    public boolean isIpv4Flag() {
        return isIpv4Flag;
    }

    public void setIpv4Flag(boolean ipv4Flag) {
        isIpv4Flag = ipv4Flag;
    }

    public int getSport() {
        return sport;
    }

    public void setSport(int sport) {
        this.sport = sport;
    }

    public ProtocolType getProType() {
        return proType;
    }

    public void setProType(ProtocolType proType) {
        this.proType = proType;
    }

    public AtomicInteger getAggretenum() {
        return aggretenum;
    }

    public void setAggretenum(AtomicInteger aggretenum) {
        this.aggretenum = aggretenum;
    }

    public boolean isOmit() {
        return omit;
    }

    public void setOmit(boolean omit) {
        this.omit = omit;
    }

    public String getParseId() {
        return parseId;
    }

    public void setParseId(String parseId) {
        this.parseId = parseId;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public AmassEvent clone() {
        try {
            return (AmassEvent) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
