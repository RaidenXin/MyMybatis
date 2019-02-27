package com;

import java.util.List;

public interface Executor {

    public <T> List<T> selectAll(String sql);
}
