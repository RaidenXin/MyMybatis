package com.huihuang.executor;

import java.util.List;

public interface Executor {

    public <T> T execute(Class<?> returnType,String sql,String className) throws Throwable;
}
