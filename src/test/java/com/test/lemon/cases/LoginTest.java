package com.test.lemon.cases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.lemon.base.BaseCase;
import com.test.lemon.data.Constants;
import com.test.lemon.data.GlobalEnvironment;
import com.test.lemon.pojo.CaseInfo;
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

import static io.restassured.RestAssured.*;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @author 小鱼干
 * @description:登录接口测试
 * @date 2020/12/15 - 16:36
 */
public class LoginTest extends BaseCase {
    List<CaseInfo> caseInfoList;
    /**
     * 数据初始化
     */
    @BeforeClass
    public void setUp(){
        //从Excel读取登录接口模块所需要的用例数据
        caseInfoList = getCaseDataFromExcel(1);
        //参数化替换
        caseInfoList = paramsReplace(caseInfoList);
    }
    /**
     * 登录接口
     */
    @Test(dataProvider = "getLoginDatas")
    public void testLogin(CaseInfo cases) throws Exception {
        //jackson json字符串-->Map
        //1.实例化objectMapper对象
        //2.第一个参数：json字符串，第二个参数：转成的类型（Map）的字节码
        Map headersMap = fromJsonToMap(cases.getRequestHeader());
        //添加日志
        String logFilePath = addLogToFile(cases.getInterfaceName(),cases.getCaseId());
        Response res =
        given().log().all().
                //3.添加请求头
                headers(headersMap).
                //4.添加请求参数
                body(cases.getParams()).
        when().
                //5.添加请求url
                post(cases.getUrl()).
        then().
                //6.获取响应内容
                log().all().
                extract().response();
        //把接口请求和响应的信息添加到Allure中
        Allure.addAttachment("接口请求响应信息",new FileInputStream(logFilePath));
        //7.断言
        assertExpected(cases,res);

        if (cases.getCaseId()==1){
            //管理员的token
            GlobalEnvironment.env.put("token1",res.path("data.token_info.token"));
        }else if (cases.getCaseId()==2){
            //投资人token
            GlobalEnvironment.env.put("token2",res.path("data.token_info.token"));
        }else if (cases.getCaseId()==3){
            //借款人token
            GlobalEnvironment.env.put("token3",res.path("data.token_info.token"));
        }
    }

    /**
     * easypoi读取excel测试用例数据
     * @return
     */
    @DataProvider
    public Object[] getLoginDatas(){
        //toArr     ay()将list集合转成一维数组
        //datas保存的是所有的CaseInfo对象
        return caseInfoList.toArray();
    }

}
