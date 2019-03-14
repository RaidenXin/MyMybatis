package com.huihuang.build;

import com.huihuang.mapping.BoundSql;
import com.huihuang.util.ObjectUtils;

import java.util.*;

public abstract class SqlBuilder {

    protected static final String BLANK_SPACE = " ";
    protected static final String AND = "AND";
    protected static final String WHERE = "WHERE";
    protected static final String FROM = "FROM";



    public static final BoundSql setParam(Object object, String sql){
        Map<String, Object> param = ObjectUtils.object2Map(object);
        return setParam(param, sql);
    }

    public static final BoundSql setParam(Map<String, Object> param,String sql){
        BoundSql boundSql = new BoundSql();
        Map<Integer, Object> map = new HashMap<>();
        List<Integer> indexs = new ArrayList<>();
        int index;
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            String key = entry.getKey();
            if (sql.indexOf("${" + key + "}") > 0){
                sql = sql.replace("${" + key + "}", (String) entry.getValue());
            }else if ((index = sql.indexOf("#{" + key + "}")) > 0){
                map.put(index, entry.getValue());
                indexs.add(index);
                sql = sql.replace("#{" + key + "}", "?");
            }
        }
        Object[] params = new Object[map.size()];
        Collections.sort(indexs);
        for (int i = 0; i < params.length; i++){
            Integer key = indexs.get(i);
            params[i] = map.get(key);
        }
        boundSql.setSql(sql);
        boundSql.setParams(params);
        return boundSql;
    }
}
