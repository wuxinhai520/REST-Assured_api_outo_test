package com.test.lemon.cases;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.lemon.base.BaseCase;
import com.test.lemon.data.Constants;
import com.test.lemon.data.GlobalEnvironment;
import com.test.lemon.pojo.CaseInfo;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @author 小鱼干
 * @description:新增项目用例
 * @date 2020/12/27 - 20:59
 */
public class AddLoanTest extends BaseCase {
    List<CaseInfo> caseInfoList;
    @BeforeClass
    public void setUp(){
        //用例数据初始化
        caseInfoList = getCaseDataFromExcel(4);
        //参数化替换
        caseInfoList = paramsReplace(caseInfoList);
    }

    @Test(dataProvider = "getAddLoanData")
    public void testAddLoan(CaseInfo cases) throws Exception {
        //将header的json字符串转成map
        Map headersMap = fromJsonToMap(cases.getRequestHeader());
        //添加日志
        String logFilePath = addLogToFile(cases.getInterfaceName(),cases.getCaseId());
        Response res =
        given().log().all().
                //添加请求头和参数
                headers(headersMap).
                body(cases.getParams()).
        when().
                //添加请求地址
                post(cases.getUrl()).
        then().log().all().
                extract().response();
        //把接口请求和响应的信息添加到Allure中
        Allure.addAttachment("接口请求响应信息",new FileInputStream(logFilePath));
        //获取项目id，保存到环境变量中
        if (res.path("data.id")!=null) {
            if (cases.getCaseId()==1) {
                GlobalEnvironment.env.put("loan_id1", res.path("data.id"));
            }else if (cases.getCaseId()==2){
                GlobalEnvironment.env.put("loan_id2", res.path("data.id"));
            }else if (cases.getCaseId()==3){
                GlobalEnvironment.env.put("loan_id3", res.path("data.id"));
            }else if (cases.getCaseId()==4){
                GlobalEnvironment.env.put("loan_id4", res.path("data.id"));
            }
        }
        //响应结果断言
        assertExpected(cases,res);
        //数据库断言
        assertSQL(cases);

    }

    @DataProvider
    public Object[] getAddLoanData(){
        Object[] datas = caseInfoList.toArray();
        return datas;
    }
}
