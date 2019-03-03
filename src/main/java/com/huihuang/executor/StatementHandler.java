package com.huihuang.executor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class StatementHandler implements Statement{
    @Override
    public <T> T query(Class<?> returnType,String sql,String className) throws Throwable{
        List<Object> list = new ArrayList<>();
        Class<?> clazz = Class.forName(className);
        Object instance = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(instance, null);
        }
        return (T) list;
    }
}
