package com.huihuang.factory;

import com.huihuang.session.MyDefaultSqlSession;
import com.huihuang.session.MySqlSession;

public class MySqlSessionFactory {

    private static final MySqlSession mySqlSession = new MyDefaultSqlSession();

    public static final MySqlSession openSession(){
        return mySqlSession;
    }
}
