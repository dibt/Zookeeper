package com.di.annotation;


import java.lang.annotation.Annotation;

public class MyTagAnnotationProcessor {
    public static void process(String className) throws ClassNotFoundException, NoSuchMethodException {
        Class clazz = Class.forName(className);
        Annotation[] annotations = clazz.getMethod("info").getAnnotations();
        for(Annotation annotation:annotations){
            //打印注解
            System.out.println(annotation);
            if(annotation instanceof MyTag){
                MyTag myTag = (MyTag) annotation;
                System.out.println(myTag.name());
                System.out.println(myTag.age());
            }
        }

    }
}
