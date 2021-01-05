package com.test.day02;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.*;
/**
 * @author 小鱼干
 * @description:cookie+session鉴权
 * @date 2020/12/17 - 10:32
 */
public class CookieTest {
    @Test
    public void testAuthenticationWithSession(){
        Response res =
        given().
                header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8").
                formParam("loginame","admin").formParam("password","e10adc3949ba59abbe56e057f20f883e").
        when().
                post("http://erp.lemfix.com/user/login").
        then().
                log().all().extract().response();
//        System.out.println(res.header("Set-Cookie"));
        //getCookies()获取响应头里的cookie
        Map<String, String> cookieMap = res.getCookies();

        //getUserSession接口请求
        given().
                header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8").
                //添加cookie
                cookies(cookieMap).
        when().
                get("http://erp.lemfix.com/user/getUserSession").
        then().
                log().all();
    }
}
