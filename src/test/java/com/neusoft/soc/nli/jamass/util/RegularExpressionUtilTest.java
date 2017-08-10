package com.neusoft.soc.nli.jamass.util;

import com.neusoft.soc.nli.jamass.bean.AmassEvent;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Administrator on 2017/5/22.
 */
public class RegularExpressionUtilTest {

    public static RegularExpressionUtil regularExpressionUtil;

    @Test
    public void resetParseScript() throws Exception {

    }

    @Before
    public void setUp() throws Exception {

        regularExpressionUtil = RegularExpressionUtil.getInstance();

        regularExpressionUtil.initData();

    }

    @Test
    public void testRegular() throws Exception {
        int dev_type = 107101;
        String id = "1981501662408999";
        String message = "<5>time:2010-08-13 15:35:18;danger_degree:1;breaking_sighn:0;event:[50257]HTTP;src_addr:192.168.7.10;src_port:2983;dst_addr:192.168.7.166;dst_port:80;proto:TCP.HTTP";
        AmassEvent collectorEvent = regularExpressionUtil.testRegular(dev_type,id,message);
        System.out.println(collectorEvent);
    }

    @Test
    public void initData() throws Exception {
//        regularExpressionUtil.initData();
//        System.out.println(collectorEvent);
        System.out.println(1);
    }

}