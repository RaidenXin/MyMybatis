package com.huihuang.executor;

import com.huihuang.mapping.BoundSql;

import java.util.List;

public interface Executor {

    public <T> T doQuery(BoundSql boundSql) throws Throwable;

    public Integer doInsert(BoundSql boundSql) throws Throwable;

    public Integer doUpdate(BoundSql boundSql) throws Throwable;

    public void doDelete(BoundSql boundSql) throws Throwable;
}
