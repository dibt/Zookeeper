package com.di;


import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.Properties;

/**
 * Created by bentengdi on 2018/5/21.
 */
public class Log4jTest {
    private static final Logger LOGGER = Logger.getLogger(Log4jTest.class);
    @Test
    public void log4jTset() {
        try{
            throw new Exception();
        } catch (Exception e) {
            LOGGER.error("this is a error",e);
        }
    }
}

