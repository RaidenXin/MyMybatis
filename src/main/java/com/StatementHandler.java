package com;

import java.util.ArrayList;
import java.util.List;

public class StatementHandler implements Statement{
    @Override
    public List<Object> query(String sql){
        List<Object> list = new ArrayList<>();
        return list;
    }
}
