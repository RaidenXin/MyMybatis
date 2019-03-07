package com.huihuang.util;

public class SqlUtils {

    public static final String transformationOfFieldName(String filedName){
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
