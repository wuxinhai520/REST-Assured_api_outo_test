package com.test.day01;

import org.testng.annotations.Test;
import static io.restassured.RestAssured.*;

/**
 * @author 小鱼干
 * @description:
 * @date 2020/12/15 - 16:36
 */
public class FutureloanTest {
    /**
     * 注册接口
     */
    @Test
    public void testRegister(){
        String jsonStr = "{\"mobile_phone\": \"13543436568\",\"pwd\": \"12345678\"}";
        given().
                //请求头
                header("Content-Type","application/json;charset=utf-8").
                header("X-Lemonban-Media-Type","lemonban.v1").
                //json请求参数
                body(jsonStr).
        when().
                //请求url
                post("http://api.lemonban.com/futureloan/member/register").
        then().
                //输出响应体
                log().body();
    }

    /**
     * 登录接口
     */
    @Test
    public void testLogin(){
        String jsonStr = "{\"mobile_phone\": \"13543436568\",\"pwd\": \"12345678\"}";
        given().
                header("Content-Type","application/json").
                header("X-Lemonban-Media-Type","lemonban.v1").
                body(jsonStr).
        when().
                post("http://api.lemonban.com/futureloan/member/login").
        then().
                log().body();
    }


}
