package com.huihuang.test;

import com.huihuang.annotation.MyParam;
import com.huihuang.build.SelectSqlBuilder;
import com.huihuang.mapper.BaseMapper;
import test.User;
import com.huihuang.session.SqlSessionManager;
import org.junit.jupiter.api.Test;
import test.UserMapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestJdbcUtils {

    @Test
    public void testUser(){
        Class clazz = null;
        Method[] methods = clazz.getMethods();
        for (Method m : methods) {
            if (m.getName().equals("selectAll")){
                Class returnClass = m.getReturnType();
                System.err.println(returnClass.getName() + "1");
                System.err.println(m.getName());
                Type type = m.getGenericReturnType();
                if (type instanceof ParameterizedType){
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type[] types = parameterizedType.getActualTypeArguments();
                    for (Type t : types){
                        System.err.println(t.toString() + "2");
                    }
                }
            }
        }
    }

    @Test
    public void testList() {
        Class clazz = BaseMapper.class;
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            System.err.println(method.getName());
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            System.err.println(parameterAnnotations.length);
            for (Annotation[] as : parameterAnnotations) {
                for (Annotation a : as) {
                    MyParam param = (MyParam) a;
                    System.err.println(param.value());
                }
            }
            System.err.println();
        }
    }

    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {
        Integer a = 1,b = 2;
        System.err.println("a:" + a + " " + "b:" + b);
        swap(a,b);
        System.err.println("a:" + a + " " + "b:" + b);
    }

    private void swap(Integer a, Integer b) throws NoSuchFieldException, IllegalAccessException {
        Field field = Integer.class.getDeclaredField("value");
        field.setAccessible(true);
        int temp = a.intValue();
        field.set(a, b.intValue());
        field.set(b, temp);
    }

    @Test
    public void testBuilder(){
        User user  = new User();
        Map<String, Object> map = new HashMap<>();
        map.put("AGE", "12");
        map.put("NAME", "张三");
        System.err.println(SelectSqlBuilder.createSql(map, user).getSql());
    }


    @Test
    public void testSql(){
        String sql = "SELECT * FROM user WHERE age = #{age} AND name = #{name}";
        String[] strings = sql.split("\\#\\{|\\}");
        for (String s : strings) {
            System.err.println(s);
        }
    }

    @Test
    public void testSelect(){
        UserMapper userMapper = SqlSessionManager.getMapper(UserMapper.class);
        Map<String, Object> map = new HashMap<>();
        map.put("AGE", "12");
        map.put("NAME", "张三");
        List<User> list = userMapper.selectByMap(map);
        System.err.println(list);
        Map<String, Object> map2 = new HashMap<>();
        map2.put("AGE", "12");
        List<User> list2 = userMapper.selectByMap(map2);
        System.err.println(list2);
    }
}
