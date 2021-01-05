package com.test.lemon.cases;

import com.test.lemon.base.BaseCase;
import com.test.lemon.pojo.CaseInfo;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.*;

/**
 * @author 小鱼干
 * @description:投资
 * @date 2021/1/2 - 17:55
 */
public class InvestTest extends BaseCase {
    List<CaseInfo> caseInfoList;
    @BeforeClass
    public void setUp(){
        //1.用例数据初始化
        caseInfoList = getCaseDataFromExcel(6);
        //2.参数化替换
        caseInfoList = paramsReplace(caseInfoList);
    }

    @Test(dataProvider = "getInvesData")
    public void testInvest(CaseInfo cases) throws Exception{
        //1.将请求头转换成map
        Map headerMap = fromJsonToMap(cases.getRequestHeader());
        //2.添加日志
        String logFilePath = addLogToFile(cases.getInterfaceName(), cases.getCaseId());
        Response res =
        given().log().all().
                //添加请求头和请求参数
                headers(headerMap).
                body(cases.getParams()).
        when().
                //添加请求地址
                post(cases.getUrl()).
        then().log().all().
                extract().response();
        //3.把接口请求和响应信息添加到Allure中
        Allure.addAttachment("接口请求响应信息",new FileInputStream(logFilePath));
        //4.响应结果断言
        assertExpected(cases,res);
        //5.数据库断言
        assertSQL(cases);
    }

    @DataProvider
    public Object[] getInvesData(){
        return caseInfoList.toArray();
    }
}
