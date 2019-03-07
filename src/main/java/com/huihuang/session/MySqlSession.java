package com.huihuang.session;

import java.util.List;
import java.util.Map;

public interface MySqlSession {

    public <T> T doQuery(Class<?> returnType,String className,String sql,Object param) throws Throwable;

    public <T> T doInsert(Class<?> returnType,String className,String sql,Object param) throws Throwable;
}
