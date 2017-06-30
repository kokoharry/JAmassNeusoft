package com.neusoft.soc.nli.jamass.core;

import com.neusoft.soc.nli.jamass.bean.ProtocolType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by luyb on 2017/6/5.
 */
public class AmassConfigrationTest {
    @Test
    public void reloadProps() throws Exception {

    }



    @Test
    public void getSources() throws Exception {
        System.out.println(AmassConfigration.getSources("") );
        System.out.println(ProtocolType.valueOf("SysLog"));
    }

    @Test
    public void getSinks() throws Exception {
        System.out.println(AmassConfigration.getSinks(""));
    }

}