package com.test;

import com.huihuang.mapper.UserMapper;
import com.huihuang.model.User;
import com.huihuang.session.SqlSessionManager;
import org.junit.jupiter.api.Test;

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
        UserMapper userMapper = (UserMapper) SqlSessionManager.getMapper();
        List<User> userList = userMapper.select(new User());
        for (User user : userList){
            System.err.println(user);
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
