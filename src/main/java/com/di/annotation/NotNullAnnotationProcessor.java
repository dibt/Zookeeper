package com.di.annotation;


import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

public class NotNullAnnotationProcessor {

    public static void processor(String className) throws ClassNotFoundException, NoSuchMethodException {
        Class clazz = Class.forName(className);
        AnnotatedType[] annotations = clazz.getAnnotatedInterfaces();
        print(annotations);
        Method method = clazz.getMethod("foo",List.class);
        AnnotatedType[] annotatedTypes = method.getAnnotatedExceptionTypes();
        print(annotatedTypes);
        AnnotatedType[] annotatedTypes1 = method.getAnnotatedParameterTypes();
        print(annotatedTypes1);
        //System.out.println(method.getDefaultValue());//null
        //System.out.println(method.getDeclaredAnnotations().length);//0
        //System.out.println(method.getDeclaredAnnotation(MyTag.class));//null
//        System.out.println(method.getAnnotatedReturnType().getType());//void
//        Class[] classes = method.getExceptionTypes();
//        for(Class claz:classes){
//            System.out.println(claz.getName());//java.lang.ClassNotFoundException
//        }
//
//        System.out.println(method.getDeclaringClass());//class com.di.annotation.NotNullTest
//        Type[] types = method.getGenericExceptionTypes();
//        for (Type type:types){
//            System.out.println(type.getTypeName());//java.lang.ClassNotFoundException
//        }
        Field[] fields = clazz.getFields();
        for(Field field:fields){
            System.out.println(field.getName());
        }

    }
    public static void print(AnnotatedType[] annotatedTypes){
        for(AnnotatedType annotatedType:annotatedTypes){
            Type type = annotatedType.getType();
            Annotation[] annotations = annotatedType.getAnnotations();
            System.out.println("打印注解类型"+type);
            for(Annotation annotation:annotations){
                System.out.println("打印注解:"+annotation);
            }
        }

    }
}
