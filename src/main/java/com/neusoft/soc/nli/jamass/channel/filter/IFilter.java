package com.neusoft.soc.nli.jamass.channel.filter;

/**
 * 过滤类接口，系统支持根据日志的发生源IP地址，接收时间，日志等级，设备类型等条件对日志进行过滤
 * Created by luyb on 2017/6/5.
 */
public interface IFilter {

    /**
     * 根据发生源Ip对日志进行过滤
     * 
     * @param ip
     *            发生源IP
     * @return 不过滤返回true, 否则返回false
     */
    boolean doIpFilte(long ip, int type);
    
    /**
     * 根据发生源Ip对日志进行过滤
     * 
     * @param ip
     *            发生源IP
     * @return 不过滤返回true, 否则返回false
     */
    boolean doIpFilte(String ip, int type);

    /**
     * 根据接收时间对日志进行过滤,这部分通过设置策略生效时间实现了相同功能
     * 
     * @param time
     *            接收时间
     * @return 不过滤返回true, 否则返回false
     */
    @Deprecated
    boolean doTimeFilte(long time);

    /**
     * 根据设备类型进行过滤
     * 
     * @param devType
     *            设备类型号
     * @return 不过滤返回true, 否则返回false
     */
    boolean doDevTypeFilte(int devType);

    /**
     * 根据日志等级对日志进行过滤
     * 
     * @param level
     *            日志等级
     * @return 不过滤返回true, 否则返回false
     */
    boolean doLevelFilte(int level);

    boolean doMessageFilte(String message);

    boolean doAlarmTypeFilte(int alarmId);

    boolean doAlarmCodeFilte(String alarmCode, int devId);

}
