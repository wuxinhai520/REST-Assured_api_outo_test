package com.test.lemon.util;

import java.util.Random;

/**
 * @author 小鱼干
 * @description:
 * @date 2020/12/24 - 17:45
 */
public class PhoneRandom {
    /**
     * 随机生成一个手机号码
     * @return
     */
    public static String getPhone(){
        //1.定义手机号码号段
        String phonePrefix="133";
        //循环8次
        for (int i=0;i<8;i++) {
            //随机生成手机号码后八位
            Random random = new Random();
            //随机生成一个数，参数可以指定范围
            int num = random.nextInt(9);
            phonePrefix+=num;
        }
        return phonePrefix;
    }

    /**
     * 查询生成的手机号码是否被注册
     * @return
     */
    public static String getRandomPhone(){
        while(true){
            String phone = getPhone();
            Object result = JDBCUtils.querySingle("select count(*) from member where mobile_phone=" + phone);
            if ((Long) result == 1) {
                System.out.println("手机号码已注册");
            } else {
                return phone;
            }
        }
    }

}
