package com.huihuang.executor;

public class BaseExecutor implements Executor{

    @Override
    public <T> T execute(Class<?> returnType,String sql,String className) throws Throwable{
        Statement statement = new StatementHandler();
        return statement.<T>query(returnType, sql, className);
    }
}
