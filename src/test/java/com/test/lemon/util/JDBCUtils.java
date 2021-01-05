package com.test.lemon.util;

import com.test.lemon.data.Constants;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * @author 小鱼干
 * @description:数据库连接工具类
 * @date 2020/12/24 - 11:06
 */
public class JDBCUtils {
    /**
     * 获取到数据库连接
     * @return
     */
    public static Connection getConnection(){
        //1.定义数据库连接
        String baseUrl = Constants.DATABASE__URL;
        String user = Constants.USERNAME;
        String password = Constants.PASSWORD;
        //2.定义数据库连接对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(baseUrl, user, password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 插入数据
     * @param sql
     */
    public static void insert(String sql){
        //1.获取数据库的连接对象
        Connection conn = getConnection();
        //2.数据库操作
        QueryRunner queryRunner = new QueryRunner();
        try {
            queryRunner.update(conn,sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //3.关闭数据库连接
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 修改数据
     * @param sql
     */
    public static void update(String sql){
        //1.获取数据库的连接对象
        Connection conn = getConnection();
        //2.数据库操作
        QueryRunner queryRunner = new QueryRunner();
        try {
            queryRunner.update(conn,sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //3.关闭数据库连接
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    /**
     * 查询所有的结果集
     * @param sql 要执行的sql语句
     * @return
     */
    public static List<Map<String, Object>> queryAll(String sql){
        //1.获取数据库的连接对象
        Connection conn = getConnection();
        //2.数据库操作
        QueryRunner queryRunner = new QueryRunner();
        try {
            //第一个参数：数据库连接对象，第二个参数：sql语句，第三个参数：接受查询结果
            List<Map<String, Object>> result = queryRunner.query(conn, sql, new MapListHandler());
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //3.关闭数据库连接
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * 查询结果集中的第一条数据
     * @param sql 要执行的sql语句
     * @return
     */
    public static Map<String, Object> queryOne(String sql){
        //1.获取数据库的连接对象
        Connection conn = getConnection();
        //2.数据库操作
        QueryRunner queryRunner = new QueryRunner();
        try {
            //第一个参数：数据库连接对象，第二个参数：sql语句，第三个参数：接受查询结果
            Map<String, Object> result = queryRunner.query(conn, sql, new MapHandler()); //结果有多条的情况只显示第一条
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //3.关闭数据库连接
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    /**
     * 查询结果集中的单个数据
     * @param sql 要执行的sql语句
     * @return
     */
    public static Object querySingle(String sql){
        //1.获取数据库的连接对象
        Connection conn = getConnection();
        //2.数据库操作
        QueryRunner queryRunner = new QueryRunner();
        try {
            //第一个参数：数据库连接对象，第二个参数：sql语句，第三个参数：接受查询结果
            Object result = queryRunner.query(conn, sql, new ScalarHandler<Object>()); //结果有多条的情况只显示第一条
            return result;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //3.关闭数据库连接
        try {
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
