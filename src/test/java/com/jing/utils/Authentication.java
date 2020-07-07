package com.jing.utils;

import com.alibaba.fastjson.JSONPath;
import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;
import java.util.Map;
public class Authentication {
  public static Map<String,Object> VARS = new HashMap<>();
    /**
     *从请求体中获取内容并存入var变量中
     * @param json json字符串
     * @param expression   JsonPath表达式
     * @param key  存储到VARS中的key
     */
 public static void jsonVars(String json,String expression,String key){
     //如果json不是空，则继续操作
     if(StringUtils.isNotBlank(json)){
         //使用jsonpath获取内容
         Object read = JSONPath.read(json, expression);
         //如果获取内容不是空，则存入VARS，供其他接口使用
         if(read !=null){
             Authentication.VARS.put(key,read);
             System.out.println(key+":"+read);
         }
     }
 }
}
