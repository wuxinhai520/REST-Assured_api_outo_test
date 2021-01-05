package com.test.day03.cases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.day03.pojo.CaseInfo;
import io.restassured.response.Response;
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
 * @description:注册接口测试
 * @date 2020/12/21 - 17:29
 */
public class RegisterTest {
    List<CaseInfo> caseInfoList;

    /**
     * 数据初始化
     */
    @BeforeClass
    public void setUp(){
        caseInfoList = getCaseDataFromExcel(0);
    }

    @Test(dataProvider = "getRegisterData")
    public void testRegister(CaseInfo cases) throws Exception {
        //1.实例化objectMapper对象
        ObjectMapper objectMapper = new ObjectMapper();
        //2.将json字符串转换成map，第一参数json字符串，第二个参数Map.class字节码
        Map heardersMap = objectMapper.readValue(cases.getRequestHeader(), Map.class);
        Response res =
        given().
                //1.添加请求头
                headers(heardersMap).
                //2.添加请求参数
                body(cases.getParams()).
        when().
                //3.添加请求url
                post("http://api.lemonban.com/futureloan"+cases.getUrl()).

        then().
                extract().response();
        //4.断言
        //4.1获取excel用例数据中的期望结果
        //4.2将json字符串转成map
        Map expectedMap = objectMapper.readValue(cases.getExpected(), Map.class);
        //4.3遍历集合，取出所有的键值对
        Set<Map.Entry<String,Object>> entrySet = expectedMap.entrySet();
        for (Map.Entry<String,Object> map:entrySet){
            //4.4期望结果中的键已经改成Gpath表达式，通过Gpath表达式提取响应中的实际结果，与期望结果的键值进行比较
            Assert.assertEquals(res.path(map.getKey()),map.getValue());
        }
    }

    /**
     * easypoi读取excel用例数据
     * @param sheetIndex 起始sheet索引
     * @return
     */
    public List<CaseInfo> getCaseDataFromExcel(int sheetIndex){
        //1.初始化读取配置对象
        ImportParams params = new ImportParams();
        //2.设置读取sheet的起始索引
        params.setStartSheetIndex(sheetIndex);
        //3.初始化File对象
        File file = new File("src/test/resources/api_testcases_futureloan_v2.xls");
        //4.easypoi工具类读取excel数据
        caseInfoList = ExcelImportUtil.importExcel(file, CaseInfo.class, params);
        return caseInfoList;
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
