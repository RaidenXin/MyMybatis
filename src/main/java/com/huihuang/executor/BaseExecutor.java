package com.huihuang.executor;

import com.huihuang.connectionpool.ConnectionPool;
import com.huihuang.factory.ConnectionPoolFactrory;
import com.huihuang.mapping.BoundSql;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BaseExecutor implements Executor{

    private ConnectionPool connectionPool = ConnectionPoolFactrory.newDefaultConnectionPool();

    @Override
    public <T> T doQuery(BoundSql boundSql) throws Throwable{
        String sql = boundSql.getSql();
        String className = boundSql.getClassName();
        Connection connection = connectionPool.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        int index = 1;
        for (Object param : boundSql.getParams()) {
            statement.setObject(index, param);
        }
        ResultSet resultSet = statement.executeQuery();
        if (boundSql.getReturnType().equals(List.class)){
            List<Object> list = new ArrayList<>();
            while (resultSet.next()){
                Object instance = createInstance(resultSet, className);
                list.add(instance);
            }
            return (T) list;
        }
        Object instance = null;
        while (resultSet.next()){
            instance = createInstance(resultSet, className);
        }
        return (T) instance;
    }

    private Object createInstance(ResultSet resultSet,String className) throws Throwable{
        Class<?> clazz = Class.forName(className);
        Object instance = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            field.set(instance, resultSet.getObject(field.getName()));
        }
        return instance;
    }

    @Override
    public Integer doInsert(BoundSql boundSql) throws Throwable{
        String sql = boundSql.getSql();
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    @Override
    public Integer doUpdate(BoundSql boundSql) throws Throwable{
        String sql = boundSql.getSql();
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    @Override
    public void doDelete(BoundSql boundSql) throws Throwable{
        String sql = boundSql.getSql();
        Connection connection = connectionPool.getConnection();
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }
}
