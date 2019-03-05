package com.huihuang.session;

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
    public <T> T execute(Class<?> returnType, String className, String sql,Object[] args) throws Throwable {
        if (args.length > 0 ){
            Object object = args[0];
            if (object instanceof Map){

            }
        }
        return executor.doQuery(returnType, sql, className);
    }

}
