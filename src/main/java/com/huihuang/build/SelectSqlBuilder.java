package com.huihuang.build;

import com.huihuang.util.ObjectUtils;
import com.huihuang.util.SqlUtils;
import com.huihuang.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

public final class SelectSqlBuilder extends  SqlBuilder{

    private static final Map<String, Map<String, Object>> classMappingMaps = new HashMap<>();

    public static final String createSql(Object o){
        Map<String, Object> map = ObjectUtils.object2Map(o);
        return createSelectSql(map);
    }

    public static final String createSql(Map<String, Object> map,Object o){
        Map<String, Object> objectMap = ObjectUtils.object2Map(o);
        StringBuilder builder = new StringBuilder(createSelectSql(objectMap));
        if (!map.isEmpty()){
            builder.append(WHERE);
            int i = 0;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (null != entry.getValue()){
                    if (i != 0){
                        builder.append(BLANK_SPACE);
                        builder.append(AND);
                        i++;
                    }
                    builder.append(BLANK_SPACE);
                    builder.append(entry.getKey());
                    builder.append(BLANK_SPACE);
                    builder.append("=");
                    builder.append(BLANK_SPACE);
                    builder.append("'");
                    builder.append(ObjectUtils.object2String(entry.getValue()));
                    builder.append("'");
                }
            }
        }
        return builder.toString();
    }


    private static String createSelectSql(Map<String, Object> map){
        StringBuilder builder = new StringBuilder("SELECT");
        StringBuilder condition = new StringBuilder(WHERE);
        int i = 0,j = 0;
        for (Map.Entry<String, Object> e : map.entrySet()) {
            if (i != 0){
                builder.append(",");
                i++;
            }
            Object object = e.getValue();
            String key = SqlUtils.transformationOfFieldName(e.getKey());
            builder.append(key);
            if (null != object){
                if (j != 0){
                    condition.append(BLANK_SPACE);
                    condition.append(AND);
                    j++;
                }
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
