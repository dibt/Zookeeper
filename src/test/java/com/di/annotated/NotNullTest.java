package com.di.annotated;

import com.di.annotation.NotNullAnnotationProcessor;
import org.junit.Test;

public class NotNullTest {
    @Test
    public void testNotNull(){
        try {
            NotNullAnnotationProcessor.processor("com.di.annotation.NotNullTest");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
