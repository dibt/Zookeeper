package com.di.annotated;

import com.di.annotation.MyTagAnnotationProcessor;
import org.junit.Test;

public class MyTagTest {
    @Test
    public void testMyTag(){
        try {
            MyTagAnnotationProcessor.process("com.di.annotation.MyTagTest");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
}
