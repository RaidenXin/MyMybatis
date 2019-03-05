package com.huihuang.session;

import java.util.List;
import java.util.Map;

public interface MySqlSession {

    public <T> T execute(Class<?> returnType,String className,String sql,Object[] args) throws Throwable;
}
