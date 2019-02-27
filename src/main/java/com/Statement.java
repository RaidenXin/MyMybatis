package com;

import java.util.ArrayList;
import java.util.List;

public interface Statement {

    public <T> List<T> query(String sql);
}
