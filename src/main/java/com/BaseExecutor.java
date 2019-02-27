package com;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BaseExecutor implements Executor{

    @Override
    public <T> List<T> selectAll(String sql) {
        Statement statement = new StatementHandler();
        return statement.<T>query(sql);
    }
}
