package com.huihuang.session;

import com.huihuang.build.SelectSqlBuilder;
import com.huihuang.executor.BaseExecutor;
import com.huihuang.executor.Executor;
import com.huihuang.util.ObjectUtils;
import com.huihuang.util.StringUtils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *session
 */
public class MyDefaultSqlSession implements MySqlSession {

    private static final String BLANK_SPACE = " ";
    private static final String AND = "AND";
    private static Executor executor = new BaseExecutor();

    @Override
    public <T> T doQuery(Class<?> returnType,String className,String sql,Object param) throws Throwable {
        if (param.getClass().getName().equals(className)){
            sql = SelectSqlBuilder.setParam(param, sql);
            return executor.doQuery(returnType, sql, className);
        }
        sql = SelectSqlBuilder.setParam((Map) param, sql);
        return executor.doQuery(returnType, sql, className);
    }

    @Override
    public <T> T doInsert(Class<?> returnType, String className, String sql, Object param) throws Throwable {
        return null;
    }

    @Override
    public <T> T selectByMap(Class<?> returnType, String className, Map<String, Object> param) throws Throwable {
        Class<?> clazz = Class.forName(className);
        String sql = SelectSqlBuilder.createSql(param, clazz.newInstance());
        return executor.doQuery(returnType, sql, className);
    }

}
