package org.mossmc.mosscg.MossLib.SQLite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * 封装好的SQLite执行方法
 * 只需要直接写语句调用就行
 * 记得先初始化SQLite
 * args一般是{<1,data><2,ab>}这样的格式
 */
public class SQLiteExecute {
    /**
     * SQLite查询
     * 写好sql语句就行
     * 下方的args是参数表
     * 参考语句：
     * 无args：select * from code
     * 有args：select * from code where node=? and number=?
     */
    public static ResultSet getResultSet(String sql,SQLiteManager manager) throws SQLException {
        Connection connection = manager.getConnection();
        return connection.prepareStatement(sql).executeQuery();
    }
    public static ResultSet getResultSet(String sql, Map<Integer,String> args,SQLiteManager manager) throws SQLException {
        Connection connection = manager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        if (args==null) return statement.executeQuery();
        for (Map.Entry<Integer, String> entry : args.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            statement.setString(key, value);
        }
        return statement.executeQuery();
    }

    /**
     * SQLite更新
     * 写好sql语句就行
     * 下方的args是参数表
     * 参考语句：
     * 更新：update ads set status=? WHERE ID=?
     * 删除：delete from ? WHERE ID=?
     */
    public static int update(String sql, Map<Integer,String> args,SQLiteManager manager) throws SQLException {
        Connection connection = manager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        if (args==null) return connection.prepareStatement(sql).executeUpdate();
        for (Map.Entry<Integer, String> entry : args.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();
            statement.setString(key, value);
        }
        return connection.prepareStatement(sql).executeUpdate();
    }

    /**
     * SQLite插入
     * 写好sql语句就行
     * 下方的args是参数表
     * 参考语句：
     * 有args：insert into users (?,?,?,?,?) values (?,?,?,?,?)
     * 此处argsMap为key+value格式，建议使用LinkedHashMap！
     */
    public static int insert(String sql, Map<String,String> args,SQLiteManager manager) throws SQLException {
        Connection connection = manager.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql);
        if (args==null) return connection.prepareStatement(sql).executeUpdate();
        int loc = 1;
        for (Map.Entry<String, String> e : args.entrySet()) {
            statement.setString(loc, e.getKey());
            loc++;
        }
        for (Map.Entry<String, String> e : args.entrySet()) {
            statement.setString(loc, e.getValue());
            loc++;
        }
        return connection.prepareStatement(sql).executeUpdate();
    }
}
