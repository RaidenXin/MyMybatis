package com.huihuang.session;

import java.util.List;
import java.util.Map;

public interface MySqlSession {

    public <T> T doQuery(Class<?> returnType, String className, String sql, Object[] params) throws Throwable;

    public <T> T doInsert(Class<?> returnType,String className,String sql,Object param) throws Throwable;

    public <T> T selectByMap(Class<?> returnType,String className,Map<String, Object> param) throws Throwable;
}
