package com.di.annotation;


import java.io.Serializable;
import java.util.List;

//implements实现接口中使用Type Annotation
public class NotNullTest implements @NotNull(value = "Serializable") Serializable {

    //泛型中使用Type Annotation  、   抛出异常中使用Type Annotation
    public void foo(List<String> list) throws @NotNull(value="ClassNotFoundException") ClassNotFoundException {

        //创建对象中使用Type Annotation
        @MyTag(age = 26)
        Object object = new String();
        //强制类型转换中使用Type Annotation
        //@NotNull
        String string = ( String) object;
    }
    public void info(){

    }

}
