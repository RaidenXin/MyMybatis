package com.huihuang.build;

import com.huihuang.util.ObjectUtils;

import java.util.Map;

public abstract class SqlBuilder {

    protected static final String BLANK_SPACE = " ";
    protected static final String AND = "AND";
    protected static final String WHERE = "WHERE";
    protected static final String FROM = "FROM";

    public static final String setParam(Object object, String sql){
        Map<String, Object> param = ObjectUtils.object2Map(object);
        return setParam(param, sql);
    }

    public static final String setParam(Map<String, Object> param, String sql){
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String key = entry.getKey();
            if (null != entry.getValue()){
                String value = ObjectUtils.object2String(entry.getValue());
                sql = sql.replace("#{" + key + "}", "'" + value + "'")
                        .replace("${" + key + "}", value);
            }
        }
        return sql;
    }
}
