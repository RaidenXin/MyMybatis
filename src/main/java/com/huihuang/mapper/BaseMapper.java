package com.huihuang.mapper;

import com.huihuang.annotation.MyParam;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseMapper<T> {

    public T selectById(@MyParam("id") Serializable id);

    public List<T> selectByMap(Map<String, Object> map);

}
