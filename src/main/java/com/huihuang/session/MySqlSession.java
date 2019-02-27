package com.huihuang.session;

import java.util.List;
import java.util.Map;

public interface MySqlSession {

    public <T> List<T> select(Object o);
}
