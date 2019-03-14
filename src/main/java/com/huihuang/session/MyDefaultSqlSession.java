package com.huihuang.session;

import com.huihuang.build.SelectSqlBuilder;
import com.huihuang.executor.BaseExecutor;
import com.huihuang.executor.Executor;
import com.huihuang.mapping.BoundSql;

import java.util.Map;

/**
 *session
 */
public class MyDefaultSqlSession implements MySqlSession {

    private static Executor executor = new BaseExecutor();

    @Override
    public <T> T doQuery(Class<?> returnType, String className, String sql, Map<String, Object> params) throws Throwable {
        BoundSql boundSql = SelectSqlBuilder.setParam(params, sql);
        boundSql.setClassName(className);
        boundSql.setReturnType(returnType);
        return executor.doQuery(boundSql);
    }

    @Override
    public <T> T doInsert(Class<?> returnType, String className, String sql, Object param) throws Throwable {
        return null;
    }

    @Override
    public <T> T selectByMap(Class<?> returnType, String className, Map<String, Object> param) throws Throwable {
        Class<?> clazz = Class.forName(className);
        BoundSql boundSql = SelectSqlBuilder.createSql(param, clazz.newInstance());
        boundSql.setClassName(className);
        boundSql.setReturnType(returnType);
        return executor.doQuery(boundSql);
    }

}
