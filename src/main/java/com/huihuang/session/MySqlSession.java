package com.huihuang.session;

import java.util.List;
import java.util.Map;

public interface MySqlSession {

    public <T> T execute(Class<?> returnType,String className,Object o) throws ClassNotFoundException, Throwable;

    public <T> T execute(Class<?> returnType,String className,Map<String, Object> map) throws Throwable;

    public <T> T execute(Class<?> returnType,String className,String sql) throws Throwable;
}
