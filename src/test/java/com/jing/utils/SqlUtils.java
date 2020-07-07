package com.jing.utils;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public class SqlUtils {

    /**
     *查询数据库单行单列结果集
     * @param sql  sql语句
     * @return     查询结果
     */
    public static Object getScalarHandler(String sql) {
        if (sql==null){
            System.out.println("sql为空");
            return null;
        }
        Object result = null;
        QueryRunner runner = new QueryRunner();
        //建立数据库连接
        Connection connection = JDBCUtils.getConnection();
        try {
            //创建结果集对象
            ScalarHandler handler = new ScalarHandler();
            //执行查询
            result = runner.query(connection, sql, handler);
            System.out.println(result);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(connection);
        }
        return result;
    }

    public static void mapHandler() {
        QueryRunner runner = new QueryRunner();
        Connection connection = JDBCUtils.getConnection();
        try {
            String sql = "SELECT * FROM member a where a.mobile_phone = '18301239523'";
            MapHandler handler = new MapHandler();
            Map<String, Object> query = runner.query(connection, sql, handler);
            System.out.println(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }finally {
            JDBCUtils.close(connection);
        }
    }
}
