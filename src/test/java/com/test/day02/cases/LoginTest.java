package com.test.day02.cases;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.test.day02.pojo.CaseInfo;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.*;

/**
 * @author 小鱼干
 * @description:
 * @date 2020/12/15 - 16:36
 */
public class LoginTest {

    /**
     * 登录接口
     */
    @Test(dataProvider = "getLoginDatas")
    public void testLogin(CaseInfo cases){
        String header = cases.getRequestHeader();
        System.out.println(header);
        given().
                //header("Content-Type",head1).
               //header("X-Lemonban-Media-Type",head2).
                body(cases.getParams()).
        when().
                post(cases.getUrl()).
        then().
                log().body();
    }

/*    @DataProvider
    public Object[][] getLoginDatas(){
        //1.请求接口地址 2.请求方式 3.请求头 4.请求数据
        Object[][] datas = {
                {"http://api.lemonban.com/futureloan/member/login","post","application/json","lemonban.v1","{\"mobile_phone\": \"13543436568\",\"pwd\": \"12345678\"}"},
                {"http://api.lemonban.com/futureloan/member/login","post","application/json","lemonban.v1","{\"mobile_phone\": \"135434365682\",\"pwd\": \"12345678\"}"},
                {"http://api.lemonban.com/futureloan/member/login","post","application/json","lemonban.v1","{\"mobile_phone\": \"1354343656@\",\"pwd\": \"12345678\"}"},
                {"http://api.lemonban.com/futureloan/member/login","post","application/json","lemonban.v1","{\"mobile_phone\": \"135434365\",\"pwd\": \"12345678\"}"}
        };
        return datas;
    }*/

    /**
     * easypoi读取excel测试用例数据
     * @return
     */
    @DataProvider
    public Object[] getLoginDatas(){
        //初始化读取配置对象
        ImportParams importParams = new ImportParams();
        //读取sheet的起始索引
        importParams.setStartSheetIndex(1);
        //需要读取sheet的数量
        importParams.setSheetNum(1);
        //读取excel测试用例数据
        //初始化File对象
        File excelFile = new File("src/test/resources/api_testcases_futureloan_v1.xls");
        List<CaseInfo> list = ExcelImportUtil.importExcel(excelFile, CaseInfo.class, importParams);
        //toArray()将list集合转成一维数组
        Object[] datas = list.toArray();
        //datas保存的是所有的CaseInfo对象
        return datas;
    }




/*    public static void main(String[] args) {
        //初始化读取配置对象
        ImportParams importParams = new ImportParams();
        //读取sheet的起始索引
        importParams.setStartSheetIndex(1);
        //需要读取sheet的数量
        importParams.setSheetNum(1);
        //读取excel测试用例数据
        //初始化File对象
        File excelFile = new File("src/test/resources/api_testcases_futureloan_v1.xls");
        List<CaseInfo> list = ExcelImportUtil.importExcel(excelFile, CaseInfo.class, importParams);//第一个参数：File对象,第二个参数：映射的实体类,第三个参数：读取配置对象
        for (CaseInfo cases:list){
            System.out.println(cases);
        }
    }*/



}
