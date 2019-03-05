package com.huihuang.factory;


import com.huihuang.config.Configration;
import com.huihuang.connectionpool.ConnectionPool;
import com.huihuang.connectionpool.DefaultConnectionPool;

public class ConnectionPoolFactrory {

    private static final Configration configration = new Configration();

    public static final ConnectionPool newDefaultConnectionPool(){
        return new DefaultConnectionPool(configration);
    }
}
