package com.huihuang.build;

import com.huihuang.mapping.BoundSql;
import com.huihuang.util.ObjectUtils;
import com.huihuang.util.SqlUtils;

import java.util.*;

public final class SelectSqlBuilder extends  SqlBuilder{

    private static final String SELECT = "SELECT";

    private static final Map<String, Map<String, Object>> classMappingMaps = new HashMap<>();

    public static final BoundSql createSql(Object o){
        return createSelectSql(o);
    }

    private static String getTableName(Object object){
        return object.getClass().getSimpleName().toLowerCase();
    }

    public static final BoundSql createSql(Map<String, Object> map, Object o){
        BoundSql boundSql = new BoundSql();
        Map<String, Object> objectMap = ObjectUtils.object2Map(o);
        String sql = createSelectSql(objectMap, getTableName(o), boundSql) + splitJointParams(map.entrySet(), boundSql);
        boundSql.setSql(sql);
        return boundSql;
    }

    private static BoundSql createSelectSql(Object o){
        BoundSql boundSql = new BoundSql();
        Map<String, Object> map = ObjectUtils.object2Map(o);
        StringBuilder builder = new StringBuilder(createSelectSql(map, getTableName(o), boundSql));
        int i = 0;
        Set<Map.Entry<String, Object>> entryList = new HashSet<>();
        for (Map.Entry<String, Object> e : map.entrySet()) {
            Object object = e.getValue();
            if (null != object){
                entryList.add(e);
            }
        }
        builder.append(splitJointParams(entryList, boundSql));
        boundSql.setSql(builder.toString());
        return boundSql;
    }


    /**
     * 组装查询语句头
     * @param map
     * @param tableName
     * @param boundSql
     * @return
     */
    private static String createSelectSql(Map<String, Object> map,String tableName,BoundSql boundSql){
        StringBuilder builder = new StringBuilder(SELECT);
        builder.append(BLANK_SPACE);
        int i = 0;
        for (Map.Entry<String, Object> e : map.entrySet()) {
            if (i != 0){
                builder.append(",");
            }
            i++;;
            String key = SqlUtils.transformationOfFieldName(e.getKey());
            builder.append(key);
        }
        builder.append(BLANK_SPACE);
        builder.append(FROM);
        builder.append(BLANK_SPACE);
        builder.append(tableName);
        builder.append(BLANK_SPACE);
        return builder.toString();
    }

    /**
     * 组装参数
     * @param params
     * @param boundSql
     * @return
     */
    private static String splitJointParams(Set<Map.Entry<String, Object>> params,BoundSql boundSql){
        StringBuilder builder = new StringBuilder();
        if (params.isEmpty()){
            return builder.toString();
        }
        Object[] param = new Object[params.size()];
        builder.append(WHERE);
        int i = 0;
        for (Map.Entry<String, Object> entry : params) {
            if (null != entry.getValue()){
                if (i != 0){
                    builder.append(BLANK_SPACE);
                    builder.append(AND);
                }
                builder.append(BLANK_SPACE);
                builder.append(entry.getKey());
                builder.append(BLANK_SPACE);
                builder.append("=");
                builder.append(BLANK_SPACE);
                builder.append("?");
                param[i++] = entry.getValue();
            }
        }
        boundSql.setParams(param);
        return builder.toString();
    }
}
