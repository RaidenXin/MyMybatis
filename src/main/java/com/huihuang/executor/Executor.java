package com.huihuang.executor;

import java.util.List;

public interface Executor {

    public <T> T doQuery(Class<?> returnType, String sql, String className) throws Throwable;

    public Integer doInsert(String sql) throws Throwable;

    public Integer doUpdate(String sql) throws Throwable;

    public void doDelete(String sql) throws Throwable;
}
