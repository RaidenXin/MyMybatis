package com.huihuang.session;

import com.huihuang.annotation.MyParam;
import com.huihuang.annotation.MySelect;
import com.huihuang.annotation.Myinsert;
import com.huihuang.mapper.BaseMapper;
import com.huihuang.factory.MySqlSessionFactory;
import com.huihuang.util.SqlUtils;
import com.huihuang.util.StringUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class SqlSessionManager {

    public static <T> T getMapper(Class<T> clazz){
        return (T) getInstance(clazz);
    }

    private static Object getInstance(Class<?> clazz) {
        // 操作字节码 生成虚拟子类
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        ParameterizedType type = (ParameterizedType) clazz.getGenericInterfaces()[0];
        Type[] types = type.getActualTypeArguments();
        enhancer.setCallback(new SqlSessionInterceptor(types[0].getTypeName()));
        return enhancer.create();
    }

    private static class SqlSessionInterceptor implements MethodInterceptor {

        private final String clazzName;

        public SqlSessionInterceptor(String className){
            this.clazzName = className;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            MySqlSession session = MySqlSessionFactory.openSession();
            Class<?> returnType = method.getReturnType();
            if (method.isAnnotationPresent(Myinsert.class)){
                //TODO 还未完成
            }else if (method.isAnnotationPresent(MySelect.class)){
                MySelect mySelect = method.getDeclaredAnnotation(MySelect.class);
                if (args.length == 1){
                    return session.doQuery(returnType, clazzName, mySelect.value(), args[0]);
                }else {
                    return session.doQuery(returnType, clazzName, mySelect.value(), parameterMapping2Map(method, args));
                }
            }
            return new RuntimeException();
        }

        private Map<String, Object> parameterMapping2Map(Method method,Object[] args){
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            Map<String, Object> paramMap = new HashMap<>();
            int index = 0;
            for (Annotation[] annotations : parameterAnnotations) {
                for (Annotation annotation : annotations) {
                    MyParam param = (MyParam) annotation;
                    paramMap.put(SqlUtils.transformationOfFieldName(param.value()), args[++index]);
                }
            }
            return paramMap;
        }
    }
}
