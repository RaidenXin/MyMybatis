package com.huihuang.executor;

import java.util.ArrayList;
import java.util.List;

public interface Statement {

    public <T> T query(Class<?> returnType,String sql,String className) throws Throwable;
}
