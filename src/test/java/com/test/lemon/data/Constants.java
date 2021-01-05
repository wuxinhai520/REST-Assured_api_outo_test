package com.test.lemon.data;

/**
 * @author 小鱼干
 * @description:常量类
 * @date 2020/12/24 - 20:47
 */
public class Constants {
    //sql查询语句
    public static final String SQL = "select count(*) from member where mobile_phone=";
    //数据库连接地址
    public static final String DATABASE__URL = "jdbc:mysql://8.129.91.152:3306/futureloan?useUnicode=true&characterEncoding=utf-8";
    //数据库用户名
    public static final String USERNAME = "future";
    //数据库密码
    public static final String PASSWORD = "123456";
    //用例数据路径
    public static final String EXCEL_URL = "src/test/resources/api_testcases_futureloan_v2.xls";
    //日志调试开关 true输出到控制台，false输出到文件
    public static final boolean IS_DEBUG = false;
}
