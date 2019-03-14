package com.huihuang.session;

import com.huihuang.build.SelectSqlBuilder;
import com.huihuang.executor.BaseExecutor;
import com.huihuang.executor.Executor;
import com.huihuang.mapping.BoundSql;
import com.huihuang.util.ObjectUtils;
import com.huihuang.util.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *session
 */
public class MyDefaultSqlSession implements MySqlSession {

    private static Executor executor = new BaseExecutor();

    @Override
    public <T> T doQuery(Class<?> returnType, String className, String sql, List<String> paramNames,Object[] params) throws Throwable {
        BoundSql boundSql;
        if (params.length == 1){
            Object param = params[0];
            if (param instanceof Map){
                boundSql = SelectSqlBuilder.setParam((Map) param, sql);
                return executor.doQuery(boundSql);
            }else if (param.getClass().getName().equals(className)){
                boundSql = SelectSqlBuilder.setParam(param, sql);
                return executor.doQuery(boundSql);
            }
        }
        boundSql = SelectSqlBuilder.setParam(params, paramNames, sql);
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
