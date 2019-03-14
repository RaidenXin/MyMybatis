package com.huihuang.session;

import com.huihuang.annotation.MyParam;
import com.huihuang.annotation.MySelect;
import com.huihuang.annotation.Myinsert;

import com.huihuang.factory.MySqlSessionFactory;
import com.huihuang.util.SqlUtils;
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
        private final Map<String, Method> methodProxyMap;
        private final MySqlSession session;

        public SqlSessionInterceptor(String className){
            this.clazzName = className;
            this.session = MySqlSessionFactory.openSession();
            this.methodProxyMap = new HashMap<>();
            init();
        }

        private void init(){
            Class<?> clazz = MySqlSession.class;
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                methodProxyMap.put(method.getName(), method);
            }
        }

        @Override
        public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            Class<?> returnType = method.getReturnType();
            if (method.isAnnotationPresent(Myinsert.class)){
                //TODO 还未完成
            }else if (method.isAnnotationPresent(MySelect.class)){
                return doSelect(returnType, method, args);
            }
            //获取通用的代理方法
            Method proxy = methodProxyMap.get(method.getName());
            if (null == proxy){
                throw new RuntimeException();
            }
            //设置参数
            Object[] param = new Object[3];
            param[0] = returnType;
            param[1] = clazzName;
            param[2] = args[0];
            return proxy.invoke(session, param);
        }

        private Object doSelect(Class<?> returnType,Method method, Object[] args)throws Throwable {
            MySelect mySelect = method.getDeclaredAnnotation(MySelect.class);
            if (args.length == 1){
                Object param = args[0];
                if (param instanceof Map){
                    return session.doQuery(returnType, clazzName, mySelect.value(), (Map<String, Object>) param);
                }else if (param.getClass().getName().equals(clazzName)){
                    return session.doQuery(returnType, clazzName, mySelect.value(), parameterMapping2Map(param));
                }
            }
            return session.doQuery(returnType, clazzName, mySelect.value(), parameterMapping2Map(method, args));
        }

        private Map<String, Object> parameterMapping2Map(Object param) throws Exception{
            Map<String, Object> paramMap = new HashMap<>();
            Field[] fields = param.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(param);
                if (null != value){
                    paramMap.put(field.getName(), value);
                }
            }
            return paramMap;
        }

        private Map<String, Object> parameterMapping2Map(Method method,Object[] args){
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            Map<String, Object> paramMap = new HashMap<>();
            int index = 0;
            for (Annotation[] annotations : parameterAnnotations) {
                for (Annotation annotation : annotations) {
                    if (annotation instanceof MyParam){
                        MyParam param = (MyParam) annotation;
                        paramMap.put(SqlUtils.transformationOfFieldName(param.value()), args[++index]);
                    }
                }
            }
            return paramMap;
        }
    }
}
