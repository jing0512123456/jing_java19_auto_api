package com.jing.constant;

import com.jing.utils.ExcelUtils;

import java.util.HashMap;
import java.util.Map;

public class Constant {
    //常量类
    //数据驱动excle路径
    //public static final String Excel_PATH= Constant.class.getClassLoader().getResource("./cases_v3.xlsx").getPath();
    public static final String Excel_PATH="F:/JAVA/code/IDEA/java_api/java19_auto_api_v8/src/test/resources/cases_v3.xlsx";
    //默认请求头
    public static final Map<String,String> HEADERS = new HashMap<>();
    //excel响应回写列
    public static final int WRITE_BACK_CELLNUM = 8;
    public static final int ASSERT_WRITE_BACK_CELLNUM = 10;
   //数据库连接url                           jdbc：数据库名称：ip地址：端口号：数据库名称
    public static final String JDBC_URL = "jdbc:mysql://api.lemonban.com:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
    //数据库用户名
    public static final String JDBC_USERNAME = "future";
    //数据库用户密码
    public static final String JDBC_PASSWORD = "123456";
}
