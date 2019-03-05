package com.huihuang.build;

import com.huihuang.util.ObjectUtils;

import java.util.Map;

public final class SelectSqlBuilder {

    private static final String BLANK_SPACE = " ";
    private static final String AND = "AND";
    private static final String WHERE = "WHERE";

    public static final String createSql(Map<String, Object> param,String sql){
        return sql;
    }


    public static final String createSql(Object o){
        Map<String, Object> map = ObjectUtils.object2Map(o);
        return createSelectSql(map);
    }

    public static final String createSql(Map<String, Object> map,Object o){
        Map<String, Object> objectMap = ObjectUtils.object2Map(o);
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            objectMap.put(entry.getKey(), entry.getValue());
        }
        return createSelectSql(objectMap);
    }

    private static String createSelectSql(Map<String, Object> map){
        StringBuilder builder = new StringBuilder("SELECT");
        StringBuilder condition = new StringBuilder(WHERE);
        condition.append(BLANK_SPACE);
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
                condition.append(ObjectUtils.object2String(object));
                condition.append("'");
            }
        }
        builder.append(BLANK_SPACE);
        if (condition.lastIndexOf(BLANK_SPACE) > 5){
            builder.append(condition);
        }
        return builder.toString();
    }
}
