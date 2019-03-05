package com.huihuang.connectionpool;

import com.huihuang.config.Configration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *数据库连接池实现类
 */
public class DefaultConnectionPool implements ConnectionPool {


    //空闲连接池
    private final Queue<Connection> freeConnectionQueue;
    //工作连接池
    private final Queue<Connection> activeConnection;
    //总连接数
    private AtomicInteger totleConnection;
    private Configration configration;

    public DefaultConnectionPool(Configration message){
        this.freeConnectionQueue = new ConcurrentLinkedDeque<Connection>();
        this.activeConnection = new ConcurrentLinkedDeque<Connection>();
        totleConnection = new AtomicInteger(0);
        this.configration = message;
    }

    private void init(Configration message){
        int initConnections = message.getInitConnections();
        for (int i = initConnections; i >= 0;){
            try {
                Connection connection = createConnection();
                freeConnectionQueue.add(connection);
                i--;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取连接
     * @return
     */
    public Connection getConnection() {
        Connection connection = null;
        try{
            //如果空闲连接池不存在空闲线程，查看是否大于最大允许连接数，小于则创建新的连接
            if (freeConnectionQueue.size() == 0 && totleConnection.get() < configration.getMaxActiveConnections()){
                connection = createConnection();
                totleConnection.getAndIncrement();
            }else {
                connection = freeConnectionQueue.poll();
                if (!isAvailability(connection)){
                    connection =  createConnection();
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
            try {
                Thread.sleep(configration.getConnTimeOut());
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            connection = getConnection();
        }
        return connection;
    }

    /**
     * 创建一个新的Connection
     * @return
     * @throws SQLException
     */
    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(configration.getUrl(), configration.getUserName(), configration.getPassword());
    }

    /**
     * 释放连接(可回收机制)
     * @param connection
     */
    public void releaseConnection(Connection connection) {
        try{
            if (freeConnectionQueue.size() < configration.getMaxConnections() && isAvailability(connection)){
                freeConnectionQueue.add(connection);
            }else {
                if (null != connection){
                    connection.close();
                }
                totleConnection.getAndDecrement();
            }
        }catch (SQLException e){
        }
    }

    /**
     * 判断是否是可用连接
     * @param connection
     * @return
     * @throws SQLException
     */
    private boolean isAvailability(Connection connection) throws SQLException {
        if (null != connection && !connection.isClosed()){
            return true;
        }
        return false;
    }
}
