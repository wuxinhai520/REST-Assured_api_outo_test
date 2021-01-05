package com.test.lemon.cases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.lemon.base.BaseCase;
import com.test.lemon.data.Constants;
import com.test.lemon.data.GlobalEnvironment;
import com.test.lemon.pojo.CaseInfo;
import com.test.lemon.util.JDBCUtils;
import com.test.lemon.util.PhoneRandom;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.test.lemon.util.JDBCUtils.querySingle;
import static io.restassured.RestAssured.*;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @author 小鱼干
 * @description:注册接口测试
 * @date 2020/12/21 - 17:29
 */
public class RegisterTest extends BaseCase {
    List<CaseInfo> caseInfoList;

    /**
     * 数据初始化
     */
    @BeforeClass
    public void setUp() {
        caseInfoList = getCaseDataFromExcel(0);
    }


    @Test(dataProvider = "getRegisterData")
    public void testRegister(CaseInfo cases) throws Exception {
        //将随机生成的手机号码保存到环境变量
        //根据caseId进行判断
        if (cases.getCaseId()==1){
            GlobalEnvironment.env.put("mobile_phone1", PhoneRandom.getRandomPhone());

        }else if (cases.getCaseId()==2){
            GlobalEnvironment.env.put("mobile_phone2", PhoneRandom.getRandomPhone());

        }else if (cases.getCaseId()==3){
            GlobalEnvironment.env.put("mobile_phone3", PhoneRandom.getRandomPhone());

        }
        //对单条用例进行参数化替换
        cases = paramsReplaceCaseInfo(cases);
        //1.实例化objectMapper对象
        //2.将json字符串转换成map，第一参数json字符串，第二个参数Map.class字节码
        Map headersMap = fromJsonToMap(cases.getRequestHeader());
        //3.添加日志
        String logFilePath = addLogToFile(cases.getInterfaceName(),cases.getCaseId());
        Response res =
        given().log().all().
                //1.添加请求头
                headers(headersMap).
                //2.添加请求参数
                body(cases.getParams()).
        when().
                //3.添加请求url
                post(cases.getUrl()).

        then().log().all().
                extract().response();
        //把接口请求和响应的信息添加到Allure中
        Allure.addAttachment("接口请求响应信息",new FileInputStream(logFilePath)); //第一个参数：附件的名字；第二个参数：FileInputStream
        //4.断言响应结果
        assertExpected(cases,res);
        //5.数据库断言
        assertSQL(cases);
        //5.1注册成功的密码从用例数据中直接获取
        ObjectMapper objectMapper1 = new ObjectMapper();
        Map paramsMap = objectMapper1.readValue(cases.getParams(),Map.class);
        //5.2将用例数据中的pwd保存到环境变量中
        Object pwd = paramsMap.get("pwd");
        if (cases.getCaseId()==1){
            //5.将管理员手机号码保存到环境变量
            GlobalEnvironment.env.put("mobile_phone1",res.path("data.mobile_phone"));
            //将管理员memberid保存到环境变量
            GlobalEnvironment.env.put("member_id1",res.path("data.id"));
            //将管理员密码保存到环境变量
            GlobalEnvironment.env.put("pwd1",pwd+"");

        }else if (cases.getCaseId()==2){
            //5.将投资人手机号码保存到环境变量
            GlobalEnvironment.env.put("mobile_phone2",res.path("data.mobile_phone"));
            //将投资人memberid保存到环境变量
            GlobalEnvironment.env.put("member_id2",res.path("data.id"));
            //将投资人密码保存到环境变量
            GlobalEnvironment.env.put("pwd2",pwd+"");

        }else if (cases.getCaseId()==3){
            //5.将借款人保存到环境变量
            GlobalEnvironment.env.put("mobile_phone3",res.path("data.mobile_phone"));
            //将借款人memberid保存到环境变量
            GlobalEnvironment.env.put("member_id3",res.path("data.id"));
            //将借款人密码保存到环境变量
            GlobalEnvironment.env.put("pwd3",pwd+"");
        }
    }

    /**
     * 将caseInfoList中的数据转换成一维数组
     * @return
     */
    @DataProvider
    public Object[] getRegisterData(){
        return caseInfoList.toArray();
    }
}
