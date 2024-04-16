package org.mossmc.mosscg.MossLib.SQLite;

import org.mossmc.mosscg.MossLib.BasicInfo;
import org.mossmc.mosscg.MossLib.Object.ObjectLogger;
import org.mossmc.mosscg.MossLib.Object.ObjectSQLiteInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SQLiteManager {
    /**
     * 初始化方法
     * 需要ObjectSQLiteInfo
     */
    public void initSQLite(ObjectSQLiteInfo info, ObjectLogger loggerIn, boolean display) {
        sqliteInfo = info;
        logger = loggerIn;
        displayLog = display;
        random = new Random();
        createPool();
        logger.sendInfo("MossLib "+ BasicInfo.versionFull+" SQLite Module By "+ BasicInfo.author);
    }

    /**
     * 关闭方法
     * 调用该方法后会关闭链接并作废对象
     */
    public void close() {
        closePool();
        logger = null;
        sqliteInfo = null;
        connectionPool = null;
    }

    /**
     * SQLite基础信息
     * 初始化的时候就固定下来的
     */
    private ObjectSQLiteInfo sqliteInfo;
    private ObjectLogger logger;
    private boolean displayLog;
    private String getJDBCAddress() {
        return "jdbc:sqlite:"+sqliteInfo.filePath;
    }

    /**
     * SQLite线程池
     * 及其初始化方法
     */
    private List<Connection> connectionPool;
    private void createPool() {
        connectionPool = new ArrayList<>();
        String address = getJDBCAddress();
        if (displayLog) logger.sendInfo("SQLite链接地址："+address);
        int poolSize = sqliteInfo.poolSize;
        for (int i = 0; i < poolSize; i++) {
            try {
                Class.forName("org.sqlite.JDBC");
                Connection connection = DriverManager.getConnection(address);
                connectionPool.add(connection);
                if (displayLog) logger.sendInfo("已建立SQLite连接#"+i);
            } catch (Exception e) {
                logger.sendException(e);
                logger.sendWarn("建立SQLite连接#"+i+"出现错误！");
            }
        }
    }

    /**
     * SQLite线程池关闭方法
     */
    private void closePool() {
        int poolSize = sqliteInfo.poolSize;
        for (int i = 0; i < poolSize; i++) {
            try {
                if (connectionPool.get(i)==null) continue;
                if (connectionPool.get(i).isClosed()) continue;
                connectionPool.get(i).close();
                if (displayLog) logger.sendInfo("已关闭SQLite连接#"+i);
            } catch (Exception e) {
                logger.sendException(e);
                logger.sendWarn("关闭SQLite连接#"+i+"出现错误！");
            }
        }
    }

    /**
     * SQLite连接修复线程
     */
    private void executeFix() {
        Thread fixThread = new Thread(this::fixConnection);
        fixThread.start();
    }
    private synchronized void fixConnection() {
        for (int i = 0; i < sqliteInfo.poolSize; i++) {
            try {
                Connection c = connectionPool.get(i);
                if (c == null || c.isClosed()) {
                    String address = getJDBCAddress();
                    connectionPool.set(i,DriverManager.getConnection(address));
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
            Connection connection = connectionPool.get(random.nextInt(sqliteInfo.poolSize));
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
