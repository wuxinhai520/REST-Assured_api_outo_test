package com.test.day01;
import org.testng.annotations.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
/**
 * @author 小鱼干
 * @description:
 * @date 2020/12/15 - 10:53
 */
public class RestApiTest {
    @Test
    public void testGet01(){
        //1.不带参数的get请求
        //given配置参数，请求头，请求参数，请求数据
        given().

        //when发起请求
        when().
                get("http://httpbin.org/get").
        //then对响应结果的操作
        then().
                log().all();
    }

    @Test
    public void testGet02(){
        //2.带参数的get请求
        given().

        when().
                get("http://httpbin.org/get?name=张三&age=14").
        then().
                log().all();
    }

    @Test
    public void testGet03(){
        //3.带参数的get请求-单个参数
        given().
                queryParam("name","张三").
        when().
                get("http://httpbin.org/get").
        then().
                log().all();
    }

    @Test
    public void testGet04(){
        //4.带参数的get请求-多个参数
        given().
                queryParam("name","张三").
                queryParam("age","20").
        when().
                get("http://httpbin.org/get").
        then().
                log().all();
    }

    @Test
    public void testGet05(){
        //5.带参数的get请求-以map的形式传递参数
        Map<String,String> map = new HashMap<>();
        map.put("name","张三");
        map.put("age","20");
        given().
                queryParams(map).
        when().
                get("http://httpbin.org/get").
        then().
                log().all();
    }

    @Test
    public void testPost01(){
        //6.发送post请求-form表单参数
        //注意事项：如果form表单参数有中文的话，记得加charset=utf-8到contentType里面，否则会有乱码问题
        given().
                contentType("application/x-www-form-urlencoded; charset=utf-8").
                formParam("name","李四").
        when().
                post("http://httpbin.org/post").
        then().
                log().all();
    }

    @Test
    public void testPost02(){
        //7.发送post请求-json参数类型
        String jsonStr = "{\"name\":\"张三\",\"age\":\"18\",\"score\":100}";
        given().
                contentType("application/json;charset=utf-8").
                body(jsonStr).
        when().
                post("http://httpbin.org/post").
        then().
                log().all();
    }

    @Test
    public void testPost03(){
        //8.发送post请求-xml参数类型
        String xmlStr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<suite name=\"All Test Suite\">\n" +
                "</suite>";
        given().
                contentType("text/xml").
                body(xmlStr).
        when().
                post("http://httpbin.org/post").
        then().
                log().all();
    }

    @Test
    public void testPost04(){
        //9.发送post请求-多参数表单-上传文件
        given().
                contentType("multipart/form-data;charset=utf-8").
                multiPart(new File("D:\\wwwlog.log")).
        when().
                post("http://httpbin.org/post").
        then().
                log().body();
    }

    public static void main(String[] args) {
/*        //1.不带参数的get请求
        //given配置参数，请求头，请求参数，请求数据
        given().

        //when发起请求
        when().
                get("http://httpbin.org/get").
        //then对响应结果的操作
        then().
                log().all();*/

/*        //2.带参数的get请求
        given().

        when().
                get("http://httpbin.org/get?name=张三&age=14").
        then().
                log().all();*/

        //3.带参数的get请求-单个参数
/*        given().
                queryParam("name","张三").
        when().
                get("http://httpbin.org/get").
        then().
                log().all();*/

        //4.带参数的get请求-多个参数
/*        given().
                queryParam("name","张三").
                queryParam("age","20").
        when().
                get("http://httpbin.org/get").
        then().
                log().all();*/

        //5.带参数的get请求-以map的形式传递参数
/*        Map<String,String> map = new HashMap<>();
        map.put("name","张三");
        map.put("age","20");
        given().
                queryParams(map).
        when().
                get("http://httpbin.org/get").
        then().
                log().all();*/
    }
}
