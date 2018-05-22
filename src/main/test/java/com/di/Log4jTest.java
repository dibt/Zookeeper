package com.di;


import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by bentengdi on 2018/5/21.
 */
public class Log4jTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(Log4jTest.class);
    @Test
    public void log4jTset() {
        Integer integer = 23;
        String string = "占位符测试";
        try{
            throw new Exception();
        } catch (Exception e) {
            LOGGER.error("this is {} error {}",integer,string,e);
        }
    }
}

