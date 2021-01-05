package com.test.day02;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;

/**
 * @author 小鱼干
 * @description:断言
 * @date 2020/12/17 - 11:08
 */
public class AssertTest {
    @Test
    public void testLogin(){
        String jsonStr = "{\"mobile_phone\": \"13543436568\",\"pwd\": \"12345678\"}";
        Response res =
        given().
                header("Content-Type","application/json").
                header("X-Lemonban-Media-Type","lemonban.v1").
                body(jsonStr).
        when().
                post("http://api.lemonban.com/futureloan/member/login").
        then().
                log().body().extract().response();
        //获取业务状态码
        int code = res.path("code");
        //获取msg
        String msg = res.path("msg");
        //获取手机号
        String mobilePhone = res.path("data.mobile_phone");
        //断言--使用TestNG框架提供的断言API
        //第一个参数是实际值，第二个参数是期望值，第三个参数是断言失败后需要打印的信息
        Assert.assertEquals(code,0);
        Assert.assertEquals(msg,"OK");
        Assert.assertEquals(mobilePhone,"13543436568","断言失败");
        //判断表达式是否为真
        Assert.assertTrue(msg.equals("OK"));
    }
}
