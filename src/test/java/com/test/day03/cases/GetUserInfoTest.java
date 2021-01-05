package com.test.day03.cases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.day03.pojo.CaseInfo;
import com.test.day03.utils.GlobalEnvironmentUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 小鱼干
 * @description:获取用户信息接口测试
 * @date 2020/12/20 - 21:40
 */
public class GetUserInfoTest {
    List<CaseInfo> caseInfoList;

    /**
     * 初始化用例数据
     */
    @BeforeClass
    public void setUp(){
        //excel读取的用例数据
        caseInfoList = getUserInfoCaseFromExcel(2);
        //参数化替换
        caseInfoList = paramsReplace(caseInfoList);
    }

    @Test(dataProvider = "getUserInfoData")
    public void testGetUserInfo(CaseInfo cases) throws Exception {
        //1.实例化objectMapper对象
        ObjectMapper objectMapper = new ObjectMapper();
        //2.将json字符串转换成map对象
        Map heardersMap = objectMapper.readValue(cases.getRequestHeader(), Map.class);
        Response res =
        given().
                //1.添加请求头
                headers(heardersMap).
        when().
                //3.添加请求url
                get("http://api.lemonban.com/futureloan"+cases.getUrl()).
        then().
                //4.获取响应内容
                extract().response();
        //5.断言
        //5.1把断言数据转换为map
        ObjectMapper objectMapper2 = new ObjectMapper();
        Map expectedMap = objectMapper2.readValue(cases.getExpected(), Map.class);
        //5.2循环遍历map里面的每一组键值对
        Set<Map.Entry<String, Object>> entrySet = expectedMap.entrySet();
        for (Map.Entry<String, Object> map:entrySet){
            Assert.assertEquals(res.path(map.getKey()),map.getValue());
        }
    }

    /**
     * 读取excel中的测试用例
     * @param sheetIndex 读取sheet的起始索引
     * @return
     */
    public List<CaseInfo> getUserInfoCaseFromExcel(int sheetIndex){
        //1.初始化读取配置对象
        ImportParams params = new ImportParams();
        //2.设置读取excel的起始索引
        params.setStartSheetIndex(sheetIndex);
        //3.初始化File对象
        File file = new File("src/test/resources/api_testcases_futureloan_v2.xls");
        caseInfoList = ExcelImportUtil.importExcel(file, CaseInfo.class, params);
        return caseInfoList;
    }

    /**
     * 将用例数据转换成一维数组
     * @return
     */
    @DataProvider
    public Object[] getUserInfoData(){
        return caseInfoList.toArray();
    }

    public List<CaseInfo> paramsReplace(List<CaseInfo> caseInfoList){
        //对请求头，接口地址，参数输入，期望结果进行参数化处理
        for (CaseInfo caseInfo:caseInfoList){
            if (caseInfo.getRequestHeader()!=null){
                String requestHeader = regexReplace(caseInfo.getRequestHeader());
                caseInfo.setRequestHeader(requestHeader);
            }
            if (caseInfo.getUrl()!=null){
                String url = regexReplace(caseInfo.getUrl());
                caseInfo.setUrl(url);
            }
            if (caseInfo.getParams()!=null){
                String params = regexReplace(caseInfo.getParams());
                caseInfo.setParams(params);
            }
            if (caseInfo.getExpected()!=null){
                String expected = regexReplace(caseInfo.getExpected());
                caseInfo.setExpected(expected);
            }
        }
        return caseInfoList;
    }
    /**
     * 正则替换方法
     * @param sourceStr 原始的字符串
     * @return 查找匹配到的内容
     */
    public static String regexReplace(String sourceStr) {
        //1.定义正则表达式
        String regex = "\\{\\{(.*?)}}";
        //2.通过正则表达式编译出来一个匹配器
        Pattern pattern = Pattern.compile(regex);
        //3.开始进行匹配,参数为需要匹配的字符串
        Matcher matcher = pattern.matcher(sourceStr);
        //保存匹配到的整个表达式，比如{{member_id}}
        String finStr = "";
        //保存到小括号（）的内容，比如：member_id
        String singleStr = "";
        //4.连续查找，连续匹配
        while(matcher.find()){
            //输出找到的匹配的结果,matcher.group(0)表示匹配整个表达式结果；matcher.group(1)表示匹配分组的结果也就是正则表达式小括号里面的结果
            finStr = matcher.group(0);
            singleStr = matcher.group(1);
        }
        //5.通过小括号的内容，找到环境变量里面对应的值
        Object replaceStr = GlobalEnvironmentUtils.env.get(singleStr);
        //6.替换原始字符串中的内容
        return sourceStr.replace(finStr,replaceStr+"");
    }
}
