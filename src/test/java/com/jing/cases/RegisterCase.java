package com.jing.cases;

import com.jing.constant.Constant;
import com.jing.pojo.CaseInfo;
import com.jing.pojo.WriteBackData;
import com.jing.utils.ExcelUtils;
import com.jing.utils.HttpUtils1;
import com.jing.utils.SqlUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.*;

public class RegisterCase extends BaseCase {

    /**
     * 数据驱动传参
     * @param caseInfo
     */
    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo){
        //1.参数化替换
        paramsReplace(caseInfo);
        //2.数据库前置查询结果
        Object beforeResult = SqlUtils.getScalarHandler(caseInfo.getSql());
        //3.调用call方法发送请求
        HttpResponse response = HttpUtils1.call(caseInfo.getMethod(),caseInfo.getContentType(),caseInfo.getUrl(),caseInfo.getParams(),Constant.HEADERS);
        String requestBody = HttpUtils1.printResponse(response);
        //4.断言响应结果
        boolean assertResponse = assertResponse(caseInfo.getExpectResult(), requestBody);
        //5.响应回写
        addWriteBackData(caseInfo.getId(),Constant.WRITE_BACK_CELLNUM,requestBody);
        //6.数据库后置查询结果
        Object afterResult = SqlUtils.getScalarHandler(caseInfo.getSql());
        //7.数据库断言
        Boolean sqlAssertflag = sqlAssert(caseInfo, beforeResult, afterResult);
        //8.添加断言回写内容
        String assertResult = assertResponse&&sqlAssertflag ? "pass":"fail";
        addWriteBackData(caseInfo.getId(),Constant.ASSERT_WRITE_BACK_CELLNUM,assertResult);
        //9.添加日志
        //10.报表断言
        Assert.assertEquals(assertResult,"pass");
    }

    /**
     * 数据库断言
     * @param caseInfo  caseInfo对象
     * @param beforeResult   前置查询结果
     * @param afterResult    后置查询结果
     * @return                 断言结果
     */
    public Boolean sqlAssert(CaseInfo caseInfo, Object beforeResult, Object afterResult) {
        Boolean flag = false;
        if(StringUtils.isNotBlank( caseInfo.getSql())){
            if(beforeResult==null||afterResult==null){
            System.out.println("数据库断言失败");
        }
        else {
            Long l1 = (Long)beforeResult;
            Long l2 = (Long)afterResult;
            if(l1==0&&l2==1){
                flag = true;
                System.out.println("数据库断言成功");
            }else {
                System.out.println("数据库断言失败");
            }
          }
        }else {
            System.out.println("sql为空，不需要断言");
        }
        return flag;
    }


    @DataProvider
    public Object[] datas(){
        //调用getdatas方法来定义读取表格
        Object[] getdatas = ExcelUtils.getdatas(sheetindex, 1, CaseInfo.class);
        return getdatas;
    }
}
