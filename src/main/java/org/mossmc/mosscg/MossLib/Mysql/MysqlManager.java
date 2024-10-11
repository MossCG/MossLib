package org.mossmc.mosscg.MossLib.Mysql;

import org.mossmc.mosscg.MossLib.BasicInfo;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;
import org.mossmc.mosscg.MossLib.Object.ObjectMysqlInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Mysql数据库管理类
 * 包含基础的Mysql操作
 * 我可不想每写一个项目搓一次Mysql类
 */
public class MysqlManager {
    /**
     * 初始化方法
     * 需要ObjectMysqlInfo
     */
    public void initMysql(ObjectMysqlInfo info, ObjectLogger loggerIn, boolean display) {
        mysqlInfo = info;
        logger = loggerIn;
        displayLog = display;
        random = new Random();
        createPool();
        logger.sendInfo("MossLib "+ BasicInfo.versionFull+" Mysql Module By "+ BasicInfo.author);
    }

    /**
     * 关闭方法
     * 调用该方法后会关闭链接并作废对象
     */
    public void close() {
        closePool();
        logger = null;
        mysqlInfo = null;
        connectionPool = null;
    }

    /**
     * Mysql基础信息
     * 初始化的时候就固定下来的
     */
    private ObjectMysqlInfo mysqlInfo;
    private ObjectLogger logger;
    private boolean displayLog;
    private String getJDBCAddress() {
        return "jdbc:mysql://"+mysqlInfo.address+":"+mysqlInfo.port+"/"+mysqlInfo.database+"?autoReconnect=true&characterEncoding=utf8";
    }

    /**
     * Mysql线程池
     * 及其初始化方法
     */
    private List<Connection> connectionPool;
    private void createPool() {
        connectionPool = new ArrayList<>();
        String address = getJDBCAddress();
        if (displayLog) logger.sendInfo("Mysql链接地址："+address);
        int poolSize = mysqlInfo.poolSize;
        for (int i = 0; i < poolSize; i++) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection(address,mysqlInfo.username, mysqlInfo.password);
                connectionPool.add(connection);
                if (displayLog) logger.sendInfo("已建立Mysql连接#"+i);
            } catch (Exception e) {
                logger.sendException(e);
                logger.sendWarn("建立Mysql连接#"+i+"出现错误！");
            }
        }
    }

    /**
     * Mysql线程池关闭方法
     */
    private void closePool() {
        int poolSize = mysqlInfo.poolSize;
        for (int i = 0; i < poolSize; i++) {
            try {
                if (connectionPool.get(i)==null) continue;
                if (connectionPool.get(i).isClosed()) continue;
                connectionPool.get(i).close();
                if (displayLog) logger.sendInfo("已关闭Mysql连接#"+i);
            } catch (Exception e) {
                logger.sendException(e);
                logger.sendWarn("关闭Mysql连接#"+i+"出现错误！");
            }
        }
    }

    /**
     * Mysql连接修复线程
     */
    private void executeFix() {
        Thread fixThread = new Thread(this::fixConnection);
        fixThread.start();
    }
    private synchronized void fixConnection() {
        for (int i = 0; i < mysqlInfo.poolSize; i++) {
            try {
                Connection c = connectionPool.get(i);
                if (c == null || c.isClosed()) {
                    String address = getJDBCAddress();
                    connectionPool.set(i,DriverManager.getConnection(address,mysqlInfo.username, mysqlInfo.password));
                }
            } catch (Exception e) {
                logger.sendException(e);
            }
        }
    }

    /**
     * 获取连接对象
     * 含判断检查连接是否可用
     */
    private Random random;
    @SuppressWarnings("BusyWait")
    public Connection getConnection() {
        while (true) {
            //随机获取连接
            Connection connection = connectionPool.get(random.nextInt(mysqlInfo.poolSize));
            try {
                //连接空或断开则判定执行修复
                if (connection == null || connection.isClosed()) {
                    executeFix();
                    continue;
                }
                //连接没问题则返回
                return connection;
            } catch (Exception e) {
                logger.sendException(e);
            } finally {
                //出现问题休息200ms再retry
                try {
                    Thread.sleep(200);
                } catch (Exception e) {
                    logger.sendException(e);
                }
            }
        }
    }
}
