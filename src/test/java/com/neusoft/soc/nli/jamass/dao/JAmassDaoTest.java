package com.neusoft.soc.nli.jamass.dao;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by luyb on 2017/8/21.
 */
public class JAmassDaoTest {
    @Test
    public void selectParsePolicy() throws Exception {

    }

    @Test
    public void selectParsePolicyById() throws Exception {

    }

    @Test
    public void selectParseAttrbute() throws Exception {

    }

    @Test
    public void selectParseEncode() throws Exception {

    }

    @Test
    public void getManualMapping() throws Exception {
        System.out.println(JAmassDao.getInstance().getManualMapping().size());
    }

}