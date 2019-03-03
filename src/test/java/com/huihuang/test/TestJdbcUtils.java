package com.huihuang.test;

import com.huihuang.annotation.MyParam;
import com.huihuang.mapper.BaseMapper;
import test.User;
import com.huihuang.session.SqlSessionManager;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class TestJdbcUtils {

    @Test
    public void testUser(){
        Class clazz = null;
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getName().equals("selectAll")){
                Class returnClass = m.getReturnType();
                System.err.println(returnClass.getName() + "1");
                System.err.println(m.getName());
                Type type = m.getGenericReturnType();
                if (type instanceof ParameterizedType){
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type[] types = parameterizedType.getActualTypeArguments();
                    for (Type t : types){
                        System.err.println(t.toString() + "2");
                    }
                }
            }
        }
    }

    @Test
    public void testList() {
        Class clazz = BaseMapper.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.err.println(method.getName());
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            System.err.println(parameterAnnotations.length);
            for (Annotation[] as : parameterAnnotations) {
                for (Annotation a : as) {
                    MyParam param = (MyParam) a;
                    System.err.println(param.value());
                }
            }
            System.err.println();
        }
    }

    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {
        Integer a = 1,b = 2;
        System.err.println("a:" + a + " " + "b:" + b);
        swap(a,b);
        System.err.println("a:" + a + " " + "b:" + b);
    }

    private void swap(Integer a, Integer b) throws NoSuchFieldException, IllegalAccessException {
        Field field = Integer.class.getDeclaredField("value");
        field.setAccessible(true);
        int temp = a.intValue();
        field.set(a, b.intValue());
        field.set(b, temp);
    }
}
