package com.test.day01.work;

import io.restassured.response.Response;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
/**
 * @author 小鱼干
 * @description:
 * @date 2020/12/15 - 20:26
 */
public class TestLemonApi {
    String jsonStr = "{\"mobile_phone\": \"13365327834\",\"pwd\": \"12345678\",\"type\":0}";
    String tokenValue = "";
    int memberId = 0;
    //保存member_id、amount、approved_or_not、loan_id
    Map<String,String> rechargeMap = new HashMap<>();
    //保存新增项目接口的请求参数
    Map<String,String> addMap = new HashMap<>();
    //保存请求头
    Map<String,String> heMap = new HashMap<>();

    /**
     * 注册接口
     */
    @Test
    public void testRegister01(){
        heMap.put("Content-Type","application/json;charset=utf-8");
        heMap.put("X-Lemonban-Media-Type", "lemonban.v2");
        given().
                //请求头
                headers(heMap).
                //请求参数
                body(jsonStr).
        when().
                post("http://api.lemonban.com/futureloan/member/register").
        then().
                log().all();
    }

    /**
     * 登录接口
     */
    @Test
    public void testLogin02(){
        Response res=
        given().
                //请求头
                headers(heMap).
                //请求参数
                body(jsonStr).
        when().
                //请求url
                post("http://api.lemonban.com/futureloan/member/login").
        then().
                //提取响应数据
                log().body().
                extract().response();
        //提取token
        tokenValue = res.path("data.token_info.token");
        //提取用户id
        memberId = res.path("data.id");
        //memberId和充值金额存到rechargeMap集合
        rechargeMap.put("member_id",memberId+"");
        rechargeMap.put("amount",10000+"");
    }

    /**
     * 充值接口
     */
    @Test
    public void testRecharge03(){
        given().
                //请求头
                headers(heMap).
                header("Authorization","Bearer "+tokenValue).
                //请求参数
                body(rechargeMap).
        when().
                //请求url
                post("http://api.lemonban.com/futureloan/member/recharge").
        then().
                log().body();


    }

    /**
     * 新增项目接口
     */
    @Test
    public void testAdd04(){
        addMap.put("member_id",memberId+"");
        addMap.put("title","小鱼干的投资项目");
        addMap.put("amount","200000");
        addMap.put("loan_rate","12.0");
        addMap.put("loan_term","6");
        addMap.put("loan_date_type","1");
        addMap.put("bidding_days","5");
        Response res =
        given().
                //请求头
                headers(heMap).
                header("Authorization","Bearer "+tokenValue).
                //请求参数
                body(addMap).
        when().
                post("http://api.lemonban.com/futureloan/loan/add").
        then().
                log().all().
                extract().response();
        //提取项目id
            int loanId = res.path("data.id");
        //把项目id存到rechargeMap集合中
        rechargeMap.put("loan_id",loanId+"");
    }

    /**
     * 审核接口
     */
    @Test
    public void testAudit05(){
        rechargeMap.put("approved_or_not",true+"");
        given().
                //请求头
                headers(heMap).
                header("Authorization","Bearer "+tokenValue).
                //请求参数
                body(rechargeMap).
        when().
                patch("http://api.lemonban.com/futureloan/loan/audit").
        then().
                log().body();
    }

    /**
     * 投资接口
     */
    @Test
    public void testInvest06(){
        given().
                //请求头
                headers(heMap).
                header("Authorization","Bearer "+tokenValue).
                //请求参数
                body(rechargeMap).
        when().
                post("http://api.lemonban.com/futureloan/member/invest").
        then().
                log().body();
    }
}
