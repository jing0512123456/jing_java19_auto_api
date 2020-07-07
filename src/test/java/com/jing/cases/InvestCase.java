package com.jing.cases;

import com.alibaba.fastjson.JSONPath;
import com.jing.constant.Constant;
import com.jing.pojo.CaseInfo;
import com.jing.utils.ExcelUtils;
import com.jing.utils.HttpUtils1;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.Map;

public class InvestCase extends BaseCase {

    /**
     * 数据驱动传参
     * @param caseInfo
     */
    @Test(dataProvider = "datas")
    public void test(CaseInfo caseInfo){
        //1.参数化替换
        paramsReplace(caseInfo);
        //2.1获取带token的请求头
        Map<String, String> headers = BaseCase.getTokenMap();
        //3.调用接口
        HttpResponse response = HttpUtils1.call(caseInfo.getMethod(),caseInfo.getContentType(),caseInfo.getUrl(),caseInfo.getParams(),headers);
        String requestBody = HttpUtils1.printResponse(response);
        //4.断言响应结果
        boolean assertResponse = assertResponse(caseInfo.getExpectResult(), requestBody);
        //5.响应回写
        addWriteBackData(caseInfo.getId(),Constant.WRITE_BACK_CELLNUM,requestBody);

        //8.添加断言回写内容
        String assertResult = assertResponse? "pass":"fail";
        addWriteBackData(caseInfo.getId(),Constant.ASSERT_WRITE_BACK_CELLNUM,assertResult);
        //9.添加日志
        //10.报表断言
        Assert.assertEquals(assertResult,"pass");
    }
    /**
     * 数据库断言
     * @param caseInfo              caseInfo对象
     * @param beforeAssertResult  前置查询结果
     * @param afterAssertResult   后置查询结果
     * @return                     断言结果
     */
    public Boolean sqlAssert(CaseInfo caseInfo, Object beforeAssertResult, Object afterAssertResult) {
        Boolean flag = false;
        if(StringUtils.isNotBlank(caseInfo.getSql())){
            if(beforeAssertResult==null||afterAssertResult==null){
                System.out.println("数据库断言失败");
            }
            else {
                BigDecimal l1 = (BigDecimal)beforeAssertResult;
                BigDecimal l2 = (BigDecimal)afterAssertResult;
                BigDecimal result1 = l2.subtract(l1);
                Object object = JSONPath.read(caseInfo.getParams(), "$.amount");
                BigDecimal result2 = new BigDecimal(object.toString());
                System.out.println(l1);
                System.out.println(l2);
                System.out.println(result1);
                System.out.println(result2);
                if(result1.compareTo(result2)==0){
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
