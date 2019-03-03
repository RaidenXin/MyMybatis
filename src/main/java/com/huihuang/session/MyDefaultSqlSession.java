package com.huihuang.session;

import com.huihuang.executor.BaseExecutor;
import com.huihuang.executor.Executor;
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
    public <T> T execute(Class<?> returnType,String className,Object o) throws Throwable {
        String sql = createSelectSql(o);
        return executor.execute(returnType, sql, className);
    }

    @Override
    public <T> T execute(Class<?> returnType, String className, Map<String, Object> map) throws Throwable {
        String sql = createSelectSql(map);
        return executor.execute(returnType, sql, className);
    }

    @Override
    public <T> T execute(Class<?> returnType, String className, String sql) throws Throwable {
        return executor.execute(returnType, sql, className);
    }

    private String createSelectSql(Object o){
        Map<String, Object> map = object2Map(o);
        return createSelectSql(o);
    }

    private String createSelectSql(Map<String, Object> map,Object o){
        Map<String, Object> objectMap = object2Map(o);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            objectMap.put(entry.getKey(), entry.getValue());
        }
        return createSelectSql(objectMap);
    }

    private String createSelectSql(Map<String, Object> map){
        StringBuilder builder = new StringBuilder("SELECT");
        StringBuilder condition = new StringBuilder("WHERE");
        condition.append(BLANK_SPACE);
        condition.append("1=1");
        builder.append(BLANK_SPACE);
        boolean flag = true;
        for (Map.Entry<String, Object> e : map.entrySet()) {
            if (flag){
                flag = false;
            }else {
                builder.append(",");
            }
            Object object = e.getValue();
            String key = e.getKey();
            builder.append(key);
            if (null != object){
                condition.append(BLANK_SPACE);
                condition.append(AND);
                condition.append(BLANK_SPACE);
                condition.append(key);
                condition.append(BLANK_SPACE);
                condition.append("=");
                condition.append(BLANK_SPACE);
                condition.append("'");
                condition.append(object2String(object));
                condition.append("'");
            }
        }
        builder.append(BLANK_SPACE);
        builder.append(condition);
        return builder.toString();
    }


    private String object2String(Object o){
        if (o instanceof Date){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format((Date) o);
        }else if (o instanceof BigDecimal){
            return o.toString();
        }
        return String.valueOf(o);
    }

    private Map<String, Object> object2Map(Object o){
        Class clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> result = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String key = StringUtils.transformationOfFieldName(field.getName());
            try{
                result.put(key, field.get(o));
            }catch (Exception e){
                return result;
            }
        }
        return result;
    }

}
