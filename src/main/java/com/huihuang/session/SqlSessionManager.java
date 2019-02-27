package com.huihuang.session;

import com.huihuang.mapper.BaseMapper;
import com.huihuang.mapper.UserMapper;
import com.huihuang.session.factory.MySqlSessionFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SqlSessionManager {

    public static BaseMapper getMapper(){
        Class[] classes = {UserMapper.class};
        return (BaseMapper) Proxy.newProxyInstance(MySqlSessionFactory.class.getClassLoader(), classes, new SqlSessionInterceptor());
    }

    private static class SqlSessionInterceptor implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            MySqlSession mySqlSession = MySqlSessionFactory.openSession();
            return method.invoke(mySqlSession, args);
        }
    }
}
