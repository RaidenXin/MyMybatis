package test;

import com.huihuang.annotation.MySelect;
import com.huihuang.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User> {

    @MySelect("SELECT * FROM user WHERE AGE = #{age} AND NAME = #{name}")
    public List<User> findAll(Map<String, Object> map);

    @MySelect("SELECT * FROM user WHERE AGE = #{age} AND NAME = #{name} LIMIT 0,1")
    public User findOne(Map<String, Object> map);
}
