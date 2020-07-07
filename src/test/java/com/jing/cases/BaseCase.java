package com.jing.cases;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.jing.constant.Constant;
import com.jing.pojo.CaseInfo;
import com.jing.pojo.WriteBackData;
import com.jing.utils.Authentication;
import com.jing.utils.ExcelUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static com.jing.utils.Authentication.VARS;

public class BaseCase {
    private static Logger logger = Logger.getLogger(BaseCase.class);
    //定义成员变量sheetindex读取testng_v5.xml中sheetindex参数
    public int sheetindex;

    @BeforeSuite
    public void defaultRequestHeader() throws Exception {
        logger.info("..........");
        Constant.HEADERS.put("X-Lemonban-Media-Type","lemonban.v2");
        Constant.HEADERS.put("Content-Type","application/json");
        //存入参数的变量
        //创建properties对象
        Properties pro = new Properties();
        //获取配置路径
        String path = BaseCase.class.getClassLoader().getResource("params.properties").getPath();
        FileInputStream fis = new FileInputStream(path);
        //读取配置文件中的内容并存入pro中
        pro.load(fis);
        fis.close();
        //把pro中内容一次性放入 vars中
        Authentication.VARS.putAll((Map)pro);
    }
    @AfterSuite
    public void finish(){
        ExcelUtils.batchWrite();
    }

    @BeforeClass
    @Parameters({"sheetindex"})
    public void Sheetindex(int sheetindex){
        this.sheetindex = sheetindex;
    }

    /**
     * 创建回写对象并添加到批量回写集合中
     * @param rowNum
     * @param cellNum
     * @param content
     */
    public void addWriteBackData(int rowNum,int cellNum,String content){
        //创建回写对象
        WriteBackData wdata= new WriteBackData(sheetindex,rowNum,cellNum,content);
        //添加到回写集合
        ExcelUtils.list.add(wdata);
    }

    /**
     * 接口响应断言
     * @param expectResult   期望值
     * @param requestBody    接口响应字符串
     * return                响应结果
     */
    public boolean assertResponse(String expectResult, String requestBody) {
        //Json转成map
        Map<String,Object> map = JSONObject.parseObject(expectResult, Map.class);
        //取出key
        Set<String> keySet = map.keySet();
        boolean assertResponseFlag = true;
        for (String expression : keySet) {
            //获取期望值
            Object expectValue = map.get(expression);
            //通过jsonpath找到实际值
            Object actualValue = JSONPath.read(requestBody, expression);
            //比较期望值和实际值
            if (expectResult==null&&actualValue!=null){
                assertResponseFlag = false;
                break;
            }
            if (expectResult==null&&actualValue==null){
                continue;
            }
            if(!expectValue.equals(actualValue)){
                assertResponseFlag = false;
                break;
            }
        }
        System.out.println("响应断言结果"+assertResponseFlag);
        return assertResponseFlag;
    }

    /**
     * 获取token，添加至请求头
     * @return
     */
    public static Map<String, String> getTokenMap() {
        //从vars中获取token
        Object token = Authentication.VARS.get("${token}");
        //添加请求头
        Map<String,String> headers = new HashMap<>();
        headers.put("Authorization","Bearer " + token);
        headers.putAll(Constant.HEADERS);
        return headers;
    }

    /**
     * 参数化替换方法
     * @param caseInfo  caseInfo对象
     */
    public void paramsReplace(CaseInfo caseInfo) {
        //获取所有的key
        Set<String> keySet = Authentication.VARS.keySet();
        //参数化替换遍历
        for (String key : keySet) {
            //通过key（占位符）获取对应被替换的值(真实的参数）
            String replacedValue = Authentication.VARS.get(key).toString();
            //替换params并将caseInfo中params值重新设置
            if (StringUtils.isNotBlank(caseInfo.getParams())){
                String params = caseInfo.getParams().replace(key, replacedValue);
                caseInfo.setParams(params);
            }
            //替换sql并将caseInfo中sql值重新设置
            if (StringUtils.isNotBlank(caseInfo.getSql())){
                String sql = caseInfo.getSql().replace(key, replacedValue);
                caseInfo.setSql(sql);
            }
            // 替换expectResult并将caseInfo中expectResult值重新设置
            if (StringUtils.isNotBlank(caseInfo.getExpectResult())){
                String expectResult = caseInfo.getExpectResult().replace(key, replacedValue);
                caseInfo.setExpectResult(expectResult);
            }
        }
    }

}
