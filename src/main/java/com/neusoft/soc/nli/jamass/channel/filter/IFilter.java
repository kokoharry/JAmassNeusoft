package com.neusoft.soc.nli.jamass.channel.filter;

/**
 * ������ӿڣ�ϵͳ֧�ָ�����־�ķ���ԴIP��ַ������ʱ�䣬��־�ȼ����豸���͵���������־���й���
 * Created by luyb on 2017/6/5.
 */
public interface IFilter {

    /**
     * ���ݷ���ԴIp����־���й���
     * 
     * @param ip
     *            ����ԴIP
     * @return �����˷���true, ���򷵻�false
     */
    boolean doIpFilte(long ip, int type);
    
    /**
     * ���ݷ���ԴIp����־���й���
     * 
     * @param ip
     *            ����ԴIP
     * @return �����˷���true, ���򷵻�false
     */
    boolean doIpFilte(String ip, int type);

    /**
     * ���ݽ���ʱ�����־���й���,�ⲿ��ͨ�����ò�����Чʱ��ʵ������ͬ����
     * 
     * @param time
     *            ����ʱ��
     * @return �����˷���true, ���򷵻�false
     */
    @Deprecated
    boolean doTimeFilte(long time);

    /**
     * �����豸���ͽ��й���
     * 
     * @param devType
     *            �豸���ͺ�
     * @return �����˷���true, ���򷵻�false
     */
    boolean doDevTypeFilte(int devType);

    /**
     * ������־�ȼ�����־���й���
     * 
     * @param level
     *            ��־�ȼ�
     * @return �����˷���true, ���򷵻�false
     */
    boolean doLevelFilte(int level);

    boolean doMessageFilte(String message);

    boolean doAlarmTypeFilte(int alarmId);

    boolean doAlarmCodeFilte(String alarmCode, int devId);

}
