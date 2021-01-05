package com.test.day03.cases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.day03.pojo.CaseInfo;
import com.test.day03.utils.GlobalEnvironmentUtils;
import io.restassured.response.Response;
import io.restassured.response.ResponseBody;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.*;

/**
 * @author 小鱼干
 * @description:登录接口测试
 * @date 2020/12/15 - 16:36
 */
public class LoginTest {
    List<CaseInfo> caseInfoList;
    /**
     * 数据初始化
     */
    @BeforeClass
    public void setUp(){
        //从Excel读取登录接口模块所需要的用例数据
        caseInfoList = getCaseDataFromExcel(1);
    }
    /**
     * 登录接口
     */
    @Test(dataProvider = "getLoginDatas")
    public void testLogin(CaseInfo cases) throws Exception {
        //jackson json字符串-->Map
        //1.实例化objectMapper对象
        ObjectMapper objectMapper = new ObjectMapper();
        //2.第一个参数：json字符串，第二个参数：转成的类型（Map）的字节码
        Map headersMap = objectMapper.readValue(cases.getRequestHeader(), Map.class);
        Response res =
        given().
                //3.添加请求头
                headers(headersMap).
                //4.添加请求参数
                body(cases.getParams()).
        when().
                //5.添加请求url
                post("http://api.lemonban.com/futureloan"+cases.getUrl()).
        then().
                //6.获取响应内容
                log().all().
                extract().response();
        //7.断言
        //7.1获取到断言信息
        //7.2把期望响应结果转换为map
        ObjectMapper objectMapper2 = new ObjectMapper();
        Map expectedMap = objectMapper2.readValue(cases.getExpected(), Map.class);
        //7.3遍历取到map里面每一组键值对
        Set<Map.Entry<String,Object>> entrySet = expectedMap.entrySet();
        for (Map.Entry<String,Object> map:entrySet){
            //7.4通过Gpath获取实际接口响应对应字段的值
            //在Excel里面写用例的期望结果时，期望结果里面键名设置成Gpath表达式，键值就是期望值
            Assert.assertEquals(res.path(map.getKey()),map.getValue());
        }
        //7.4在登录模块用例执行结束后将memberId保存到环境变量中
        //拿到正常用例返回的响应信息里面的memberId
        Integer memberId = res.path("data.id");
        if (memberId!=null){
            //7.5保存到环境变量中
            GlobalEnvironmentUtils.env.put("member_id",memberId);
            //7.6保存token到环境变量中
            String token = res.path("data.token_info.token");
            GlobalEnvironmentUtils.env.put("token",token);
        }

    }

    /**
     * 从Excel读取指定的用例数据
     * @param sheetIndex sheet的索引
     */
    public List<CaseInfo> getCaseDataFromExcel(int sheetIndex){
        //初始化读取配置对象
        ImportParams importParams = new ImportParams();
        //读取sheet的起始索引
        importParams.setStartSheetIndex(sheetIndex);
        //需要读取sheet的数量
        importParams.setSheetNum(1);
        //读取excel测试用例数据
        //初始化File对象
        File excelFile = new File("src/test/resources/api_testcases_futureloan_v2.xls");
        caseInfoList = ExcelImportUtil.importExcel(excelFile, CaseInfo.class, importParams);
        return caseInfoList;
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
