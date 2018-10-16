package com.di.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义带成员变量注解MyTag
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE_USE})
public @interface MyTag {
     String name() default "povosdi";
     int age();
}
