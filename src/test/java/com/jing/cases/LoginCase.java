package com.jing.cases;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.jing.constant.Constant;
import com.jing.pojo.CaseInfo;
import com.jing.pojo.WriteBackData;
import com.jing.utils.Authentication;
import com.jing.utils.ExcelUtils;
import com.jing.utils.HttpUtils1;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.Map;
import java.util.Set;

public class LoginCase extends BaseCase {

    /**
     * 数据驱动传参
     * @param caseInfo
     */
    @Test(dataProvider = "datas",description = "登录测试")
    @Description(".....")
    @Step("caseInfo.url")
    public void test(CaseInfo caseInfo){
        //1.参数化替换
        paramsReplace(caseInfo);
        //2.数据库前置查询结果
        //3.调用接口
        HttpResponse response = HttpUtils1.call(caseInfo.getMethod(),caseInfo.getContentType(),caseInfo.getUrl(),caseInfo.getParams(),Constant.HEADERS);
        //获得响应体
        String requestBody = HttpUtils1.printResponse(response);
        //从响应体中获取token并存入vars变量中
        Authentication.jsonVars(requestBody,"$.data.token_info.token","${token}");
        //从响应体中获取member_id并存入vars变量中
        Authentication.jsonVars(requestBody,"$.data.id","${member_id}");
        //4.断言响应结果
        boolean assertResponse = assertResponse(caseInfo.getExpectResult(), requestBody);
        //5.响应回写
        addWriteBackData(caseInfo.getId(),Constant.WRITE_BACK_CELLNUM,requestBody);
        //6.数据库后置查询结果
        //7.数据库断言
        //8.添加断言回写内容
        String assertResult = assertResponse ? "pass":"fail";
        addWriteBackData(caseInfo.getId(),Constant.ASSERT_WRITE_BACK_CELLNUM,assertResult);
        //9.添加日志
        //10.报表断言
        Assert.assertEquals(assertResult,"pass");
    }

    /**
     * 数据驱动
     * @return
     */
    @DataProvider
    public Object[] datas(){
        //调用getdatas方法来定义读取表格
        Object[] getdatas = ExcelUtils.getdatas(sheetindex, 1, CaseInfo.class);
        return getdatas;
    }
}
