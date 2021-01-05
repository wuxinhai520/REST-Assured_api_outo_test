package com.test.lemon.base;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.lemon.data.Constants;
import com.test.lemon.data.GlobalEnvironment;
import com.test.lemon.pojo.CaseInfo;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.test.lemon.util.JDBCUtils.querySingle;
import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

/**
 * @author 小鱼干
 * @description:所有测试类的父类
 * @date 2020/12/22 - 15:13
 */
public class BaseCase {
    /**
     * 整体全局性的前置配置/初始化
     */
    @BeforeTest
    public void globaSetUp() throws Exception {
        //1.设置项目的baseUrl
        RestAssured.baseURI = "http://api.lemonban.com/futureloan";
        //2.设置接口响应结果如果是json返回的小数类型，使用BigDecimal类型来存储
        RestAssured.config = RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
    }

    /**
     * 将日志重定向单独的文件中
     * @param interfaceName 接口模块名
     * @param caseId 用例id
     */
    public String addLogToFile(String interfaceName,int caseId){
        String logFilePath = "";
        if (!Constants.IS_DEBUG) {
            //提前创建好目录层级
            String dirPath = "target/log/" + interfaceName;
            File dirFile = new File(dirPath);
            //判断这个目录是否存在
            if (!dirFile.isDirectory()) {
                //创建目录
                dirFile.mkdirs();
            }
            logFilePath = dirPath + "/" + interfaceName + "_" + caseId + ".log";
            //请求之前对日志做配置，输出到对应的文件中
            PrintStream fileOutPutStream = null;
            try {
                fileOutPutStream = new PrintStream(new File(logFilePath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            RestAssured.config =
                    RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));
        }
        return logFilePath;
    }
    /**
     * 读取excel中的测试用例
     * @param sheetIndex 读取sheet的起始索引
     * @return
     */
    public List<CaseInfo> getCaseDataFromExcel(int sheetIndex) {
        //1.初始化读取配置对象
        ImportParams params = new ImportParams();
        //2.设置读取excel的起始索引
        params.setStartSheetIndex(sheetIndex);
        //3.初始化File对象
        File file = new File(Constants.EXCEL_URL);
        List<CaseInfo> caseInfoList = ExcelImportUtil.importExcel(file, CaseInfo.class, params);
        return caseInfoList;
    }

    /**
     * 对所有的case进行参数化替换方法
     * @param caseInfoList 当前类中的所有测试用例数据
     * @return 参数化替换之后的用例数据
     */
    public List<CaseInfo> paramsReplace(List<CaseInfo> caseInfoList) {
        //对请求头，接口地址，参数输入，期望结果进行参数化处理
        for (CaseInfo caseInfo : caseInfoList) {
            //参数化替换请求头
            String requestHeader = regexReplace(caseInfo.getRequestHeader());
            caseInfo.setRequestHeader(requestHeader);
            //参数化替换接口地址
            String url = regexReplace(caseInfo.getUrl());
            caseInfo.setUrl(url);
            //参数化替换参数输入
            String params = regexReplace(caseInfo.getParams());
            caseInfo.setParams(params);
            //参数化替换期望结果
            String expected = regexReplace(caseInfo.getExpected());
            caseInfo.setExpected(expected);
            //参数化替换数据库校验
            String checkSQL = regexReplace(caseInfo.getCheckSQL());
            caseInfo.setCheckSQL(checkSQL);
        }
        return caseInfoList;
    }

    /**
     * 对单条case进行参数化替换方法
     *
     * @param caseInfo 单条测试用例
     * @return 参数化替换之后的用例数据
     */
    public CaseInfo paramsReplaceCaseInfo(CaseInfo caseInfo) {
        //参数化替换请求头
        String requestHeader = regexReplace(caseInfo.getRequestHeader());
        caseInfo.setRequestHeader(requestHeader);
        //参数化替换接口地址
        String url = regexReplace(caseInfo.getUrl());
        caseInfo.setUrl(url);
        //参数化替换参数输入
        String params = regexReplace(caseInfo.getParams());
        caseInfo.setParams(params);
        //参数化替换期望结果
        String expected = regexReplace(caseInfo.getExpected());
        caseInfo.setExpected(expected);
        //参数化替换数据库校验
        String checkSQL = regexReplace(caseInfo.getCheckSQL());
        caseInfo.setCheckSQL(checkSQL);

        return caseInfo;
    }


    /**
     * 正则替换方法
     * @param sourceStr 原始的字符串
     * @return 查找匹配到的内容
     */
    public static String regexReplace(String sourceStr) {
        //如果参数化的原字符串为null，不需要进行参数化替换，直接返回null
        if (sourceStr == null) {
            return sourceStr;
        }
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
        while (matcher.find()) {
            //输出找到的匹配的结果,matcher.group(0)表示匹配整个表达式结果；matcher.group(1)表示匹配分组的结果也就是正则表达式小括号里面的结果
            finStr = matcher.group(0);
            singleStr = matcher.group(1);
            //5.通过小括号的内容，找到环境变量里面对应的值
            Object replaceStr = GlobalEnvironment.env.get(singleStr);
            //6.替换原始字符串中的内容
            sourceStr = sourceStr.replace(finStr, replaceStr + "");
        }
        //没有匹配项，返回原字符串
        return sourceStr;
    }

    /**
     * 用例公共的接口响应断言方法，断言期望值和实际值
     * @param cases 用例信息
     * @param res   接口的响应结果
     */
    public void assertExpected(CaseInfo cases, Response res) {
        //接口响应断言
        //将期望结果转换成map
        Map expectedMap = fromJsonToMap(cases.getExpected());
        Set<Map.Entry<String, Object>> entrySet = expectedMap.entrySet();
        //7.3遍历取到map里面每一组键值对
        for (Map.Entry<String, Object> map : entrySet) {
            //7.4通过Gpath获取实际接口响应对应字段的值
            //在Excel里面写用例的期望结果时，期望结果里面键名设置成Gpath表达式，键值就是期望值
            //把期望结果中的小数转换成BigDecimal类型
            Object expected = map.getValue();
            if ((expected instanceof Float) || (expected instanceof Double)) {
                BigDecimal bigDecimal = new BigDecimal(expected.toString());
                Assert.assertEquals(res.path(map.getKey()), bigDecimal,"接口响应断言失败");
            } else {
                Assert.assertEquals(res.path(map.getKey()), map.getValue(),"接口响应断言失败");
            }
        }
    }

    /**
     * 数据库断言方法
     * @param cases
     */
    public void assertSQL(CaseInfo cases){
        String checkSQL = cases.getCheckSQL();
        if (checkSQL!=null) {
            Map checkSQLMap = fromJsonToMap(checkSQL);
            Set<Map.Entry<String, Object>> set = checkSQLMap.entrySet();
            for (Map.Entry<String, Object> mapEntry : set) {
                //查询数据库
                Object actual = querySingle(mapEntry.getKey());
                //1.数据库查询的返回结果为Long类型，Excel读取期望值结果是Integer
                if (actual instanceof Long) {
                    //把expected转成Long类型
                    Long expected = new Long(mapEntry.getValue().toString());
                    Assert.assertEquals(actual, expected,"数据库断言失败");
                }else if (actual instanceof BigDecimal) {
                    //2.数据库查询的返回结果为BigDecimal类型，Excel读取期望值结果是Double
                    BigDecimal expected = new BigDecimal(mapEntry.getValue().toString());
                    Assert.assertEquals(actual,expected,"数据库断言失败");
                }else {
                    Assert.assertEquals(actual,mapEntry.getValue(),"数据库断言失败");
                }
            }
        }
    }

    /**
     * 把json字符串转成map类型
     * @param jsonStr json字符串
     */
    public Map fromJsonToMap(String jsonStr){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonStr, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
