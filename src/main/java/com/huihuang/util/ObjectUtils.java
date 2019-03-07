package com.huihuang.util;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class ObjectUtils {

    private ObjectUtils(){}

    public static String object2String(Object o){
        if (o instanceof Date){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return simpleDateFormat.format((Date) o);
        }else if (o instanceof BigDecimal){
            return o.toString();
        }
        return String.valueOf(o);
    }

    public static Map<String, Object> object2Map(Object o){
        Class clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Object> result = new HashMap<>();
        for (Field field : fields) {
            field.setAccessible(true);
            String key = field.getName();
            try{
                result.put(key, field.get(o));
            }catch (Exception e){
                return result;
            }
        }
        return result;
    }
}
