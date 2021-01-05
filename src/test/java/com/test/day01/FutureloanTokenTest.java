package com.test.day01;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.*;

/**
 * @author 小鱼干
 * @description:接口鉴权
 * @date 2020/12/15 - 16:57
 */
public class FutureloanTokenTest {
    @Test
    public void testLogin(){
        String jsonStr = "{\"mobile_phone\": \"13543436568\",\"pwd\": \"12345678\"}";
        Response res =
        given().
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                body(jsonStr).
        when().
                post("http://api.lemonban.com/futureloan/member/login").
        then().
                log().all().
                extract().response();  //获取响应内容
        //获取响应信息中的所有内容：响应头+响应体
        System.out.println(res.asString());  //asString():把res转换成字符串
        //提取响应状态码
        System.out.println(res.statusCode()); //statusCode():提取响应状态码
        //提取响应头
        System.out.println(res.header("Content-Type"));
        //获取接口响应时间
        System.out.println(res.time());
        //提取响应体
        //提取响应结果对应得字段值token
        String tokenValue = res.path("data.token_info.token");
        //提取会员id
        int memberId = res.path("data.id");

        //充值请求
        //把请求参数存到map
        Map<String,Integer> map = new HashMap<>();
        map.put("member_id",memberId);
        map.put("amount",10000);
        given().
                header("Content-Type", "application/json").
                header("X-Lemonban-Media-Type", "lemonban.v2").
                header("Authorization","Bearer "+tokenValue).
                //maven提供的依赖直接将map转成json
                body(map).
        when().
                post("http://api.lemonban.com/futureloan/member/recharge").
        then().
                log().all();
    }
}
