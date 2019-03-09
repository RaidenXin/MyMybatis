package test;

import com.huihuang.annotation.MySelect;
import com.huihuang.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

public interface UserMapper extends BaseMapper<User> {

    @MySelect("SELECT * FROM user WHERE age = #{age} AND name = #{name}")
    public List<User> findAll(Map<String, Object> map);
}
