package com.jing.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import com.jing.constant.Constant;

public class JDBCUtils {

	public static Connection getConnection() {
		// 定义数据库连接对象
		Connection conn = null;
		try {
			// 你导入的数据库驱动包， mysql。
			conn = DriverManager.
					getConnection(Constant.JDBC_URL, Constant.JDBC_USERNAME, Constant.JDBC_PASSWORD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	
	
	public static void close(Connection conn) {
		try {
			if(conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}