package com.test.lemon.cases;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.lemon.base.BaseCase;
import com.test.lemon.data.Constants;
import com.test.lemon.pojo.CaseInfo;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;


/**
 * @author 小鱼干
 * @description:充值接口用例
 * @date 2020/12/27 - 17:47
 */
public class RechargeTest extends BaseCase {
    List<CaseInfo> caseInfoList;
    @BeforeClass
    public void setUp(){
        //初始化用例数据
        caseInfoList = getCaseDataFromExcel(3);
        //参数化替换
        caseInfoList = paramsReplace(caseInfoList);
    }
    @Test(dataProvider = "getRechargeData")
    public void testRecgargeCases(CaseInfo cases) throws Exception {
        //json字符串转换成map
        Map headersMap = fromJsonToMap(cases.getRequestHeader());
        //添加日志
        String logFilePath = addLogToFile(cases.getInterfaceName(),cases.getCaseId());
        Response res =
        given().log().all().
                //解决REST-Assured返回json小数的时候，使用BigDecimal接收小数
                config(RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL))).
                //添加请求头和请求参数
                headers(headersMap).
                body(cases.getParams()).
        when().
                post(cases.getUrl()).
        then().log().all().
                extract().response();
        //把接口请求和响应的信息添加到Allure中
        Allure.addAttachment("接口请求响应信息",new FileInputStream(logFilePath));
        //响应结果断言
        assertExpected(cases,res);
        //数据库都都断言
        assertSQL(cases);
    }

    @DataProvider
    public Object[] getRechargeData(){
        Object[] datas = caseInfoList.toArray();
        return datas;
    }

}
