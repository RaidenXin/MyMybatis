package com.huihuang.session;

import com.BaseExecutor;
import com.Executor;
import com.huihuang.model.User;
import org.junit.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyDefaultSqlSession implements MySqlSession {

    private static final String BLANK_SPACE = " ";
    private static final String AND = "AND";

    @Override
    public <T> List<T> select(Object o) {
        Executor executor = new BaseExecutor();
        String sql = createSelectSql(o);
        return executor.<T>selectAll(sql);
    }

    private String createSelectSql(Object o){
        StringBuilder builder = new StringBuilder("SELECT");
        StringBuilder condition = new StringBuilder("WHERE");
        condition.append(BLANK_SPACE);
        condition.append("1=1");
        builder.append(BLANK_SPACE);
        Map<String, Object> map = object2Map(o);
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
            String key = transformationOfFieldName(field.getName());
            try{
                result.put(key, field.get(o));
            }catch (Exception e){
                return result;
            }
        }
        return result;
    }

    private String transformationOfFieldName(String filedName){
        char[] chars = filedName.toCharArray();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < chars.length; i++){
            char c = chars[i];
            if (c > 96 && c < 123){
                builder.append((char) (((int)c) - 32));
            }else if (i != 0 && c > 63 && c < 91){
                builder.append('_');
                builder.append(c);
            }
        }
        return builder.toString();
    }
}
