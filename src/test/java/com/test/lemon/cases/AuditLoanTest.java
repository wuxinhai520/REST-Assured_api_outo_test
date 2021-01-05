package com.test.lemon.cases;

import com.test.lemon.base.BaseCase;
import com.test.lemon.pojo.CaseInfo;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

/**
 * @author 小鱼干
 * @description:项目审核
 * @date 2021/1/2 - 16:26
 */
public class AuditLoanTest extends BaseCase {
    List<CaseInfo> caseInfoList;
    @BeforeClass
    public void setUp(){
        //1.用例数据初始化
        caseInfoList = getCaseDataFromExcel(5);
        //2.参数化替换
        caseInfoList = paramsReplace(caseInfoList);
    }

    @Test(dataProvider = "getAuditLoanData")
    public void testAuditLoan(CaseInfo cases) throws Exception{
        //1.将请求头转换成map
        Map headersMap = fromJsonToMap(cases.getRequestHeader());
        //2.添加日志
        String logFilePath = addLogToFile(cases.getInterfaceName(), cases.getCaseId());
        Response res =
        given().log().all().
                //添加请求头和请求参数
                headers(headersMap).
                body(cases.getParams()).
        when().
                //添加请求地址
                patch(cases.getUrl()).
        then().log().all().
                extract().response();
        //把接口请求和响应信息添加到Allure中
        Allure.addAttachment("接口请求响应信息",new FileInputStream(logFilePath));
        //响应结果断言
        assertExpected(cases,res);
        //数据库断言
        assertSQL(cases);

    }

    @DataProvider
    public Object[] getAuditLoanData(){
        return caseInfoList.toArray();
    }
}
