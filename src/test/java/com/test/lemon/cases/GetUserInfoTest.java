package com.test.lemon.cases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.lemon.base.BaseCase;
import com.test.lemon.data.Constants;
import com.test.lemon.pojo.CaseInfo;
import com.test.lemon.data.GlobalEnvironment;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.*;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @author 小鱼干
 * @description:获取用户信息接口测试
 * @date 2020/12/20 - 21:40
 */
public class GetUserInfoTest extends BaseCase{
    List<CaseInfo> caseInfoList;

    /**
     * 初始化用例数据
     */
    @BeforeClass
    public void setUp(){
        //excel读取的用例数据
        caseInfoList = getCaseDataFromExcel(2);
        //参数化替换
        caseInfoList = paramsReplace(caseInfoList);
    }

    @Test(dataProvider = "getUserInfoData")
    public void testGetUserInfo(CaseInfo cases) throws Exception {
        //1.实例化objectMapper对象
        //2.将json字符串转换成map对象
        Map headersMap = fromJsonToMap(cases.getRequestHeader());
        //添加日志
        String logFilePath = addLogToFile(cases.getInterfaceName(),cases.getCaseId());
        Response res =
        given().log().all().
                //1.添加请求头
                headers(headersMap).
        when().
                //3.添加请求url
                get(cases.getUrl()).
        then().log().all().
                //4.获取响应内容
                extract().response();
        //把接口请求和响应的信息添加到Allure中
        Allure.addAttachment("接口请求响应信息",new FileInputStream(logFilePath));
        //5.断言
        assertExpected(cases,res);
    }

    /**
     * 将用例数据转换成一维数组
     * @return
     */
    @DataProvider
    public Object[] getUserInfoData(){
        return caseInfoList.toArray();
    }
}
