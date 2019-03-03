package com.huihuang.session;

import com.huihuang.annotation.MyParam;
import com.huihuang.annotation.Myinsert;
import com.huihuang.mapper.BaseMapper;
import com.huihuang.factory.MySqlSessionFactory;
import com.huihuang.util.StringUtils;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

public class SqlSessionManager {

    public static BaseMapper getMapper(Class<?> clazz){
        return (BaseMapper) getInstance(clazz);
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

            }
            if (args.length == 1){
                Object param = args[0];
                if (param instanceof Map){
                    return session.execute(returnType, clazzName, (Map) param);
                }else if (clazzName.equals(param.getClass().getName())){
                    return session.execute(returnType, clazzName, param);
                }
            }else if (args.length > 1){
                return session.execute(returnType, clazzName, parameterMapping2Map(method, args));
            }
            return session.execute(returnType, clazzName, args[0]);
        }

        private String createSql(Method method){
            Myinsert annotation = method.getAnnotation(Myinsert.class);
            return null;
        }

        private Map<String, Object> parameterMapping2Map(Method method,Object[] args){
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            Map<String, Object> paramMap = new HashMap<>();
            int index = 0;
            for (Annotation[] annotations : parameterAnnotations) {
                for (Annotation annotation : annotations) {
                    MyParam param = (MyParam) annotation;
                    paramMap.put(StringUtils.transformationOfFieldName(param.value()), args[++index]);
                }
            }
            return paramMap;
        }
    }
}
