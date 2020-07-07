package com.jing.utils;

import com.alibaba.fastjson.JSONObject;
import com.jing.cases.BaseCase;
import com.jing.pojo.CaseInfo;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import static com.jing.constant.Constant.HEADERS;

public class HttpUtils1 {
    private static Logger logger = Logger.getLogger(HttpUtils1.class);
    /**
     * 发送http请求
     * @param method  请求方式
     * @param contentType  参数类型
     * @param url          请求地址
     * @param params        请求参数
     * @return
     */
    public static HttpResponse call(String method,String contentType,String url,String params,Map<String,String> headers ){
        try {
            if ("post".equalsIgnoreCase(method)) {
                if ("json".equalsIgnoreCase(contentType)) {
                    return HttpUtils1.jsonpost (url, params,headers);
                } else if ("form".equalsIgnoreCase(contentType)) {
                    String params1 = keyValue(params);
                    return HttpUtils1.formpost(url,params1, headers);
                } else {
                    System.out.println("没有发送请求");
                }
            } else if ("get".equalsIgnoreCase(method)) {
                return HttpUtils1.get(url,headers);
            } else if ("patch".equalsIgnoreCase(method)) {
                return HttpUtils1.patch(url,params,headers);
            } else {
                System.out.println("没有发送请求");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json转成map方法
     * @param jsonstr
     * @return
     */
    public static String keyValue(String jsonstr){
        //json转成map
        Map<String,String> map = JSONObject.parseObject(jsonstr, Map.class);
       //获取所有的key
        Set<String> keySet = map.keySet();
        String result = "";
        //遍历key
        for (String key : keySet) {
            //通过key获value
            String value = map.get(key);
            //拼接key=value&
            result+= key+"="+value+"&";
        }
        //截取去掉&
        result= result.substring(0,result.length()-1);
        return result;
    }

    /**
     * get方法
     * @param url
     * url必须带参数
     * url?key+value
     * url/futurelloan/member/register
     * @throws Exception
     */
    public static HttpResponse get(String url,Map<String,String> headers) throws Exception {
        //创建get请求并写入接口地址
        HttpGet get = new HttpGet(url);
        addHeaders(headers,get);
        //创建一个客户端
        HttpClient client = HttpClients.createDefault();
        //客户端发送请求，并返回响应对象（响应头、响应体、响应状态码）
        HttpResponse response = client.execute(get);
        return response;
    }
    /**
     *jsonpost方法
     * @param url
     * @param paramter
     * @throws Exception
     */
    public static HttpResponse jsonpost(String url,String paramter,Map<String,String> headers ) throws Exception {
        //创建一个post请求并写入接口地址
        HttpPost post = new HttpPost(url);
        //添加请求头
        addHeaders(headers,post);
        //添加参数
        StringEntity entity1 = new StringEntity(paramter,"UTF-8");
        post.setEntity(entity1);
        //创建客户端并返回响应对象
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(post);
        return response;
    }

    /**
     * formpost方法
     * @param url
     * @param paramter
     * @throws Exception
     */
    public static HttpResponse formpost(String url,String paramter,Map<String,String> headers ) throws Exception {
        //创建formpost请求并写入接口地址
        HttpPost post = new HttpPost(url);
        //添加请求头
        addHeaders(headers,post);
        //添加参数
        StringEntity entity1 = new StringEntity(paramter,"UTF-8");
        post.setEntity(entity1);
        //创建客户端并返回响应对象
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(post);
        return response;
    }
    //创建patch 方法
    /**
     * @param url
     * @param parameter
     * @throws Exception
     */
    public static HttpResponse patch(String url,String parameter,Map<String,String> headers) throws Exception {
        //创建一个patch请求并写入接口地址
        HttpPatch patch = new HttpPatch(url);
        //添加请求头
        addHeaders(headers,patch);
        //添加参数
        StringEntity stringEntity = new StringEntity(parameter,"UTF-8");
        patch.setEntity(stringEntity);
        //创建客户端并返回响应对象
        HttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(patch);
       // printResponse(response);
        return response;
    }

    /**
     * 创建获取响应对象方法
     * @param response
     * @throws Exception
     */
    public static String printResponse(HttpResponse response) {
        try {
        //获取响应头
        Header[] headers = response.getAllHeaders();
        logger.info(Arrays.toString(headers));
        //获取响应体
        HttpEntity entity = response.getEntity();
        String requestBody = null;
            requestBody = EntityUtils.toString(entity);
            logger.info(requestBody);
            //获取响应状态码
        int statusCode = response.getStatusLine().getStatusCode();
            logger.info(statusCode);
            return requestBody;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 添加请求头
     * @param headers  请求头变量值
     * @param request 请求方式
     */
    public static void addHeaders(Map<String,String> headers,HttpRequest request){
        Set<String> keySet = headers.keySet();
        for (String name : keySet) {
            String value = headers.get(name);
            request.addHeader(name,value);
        }
    }
}
