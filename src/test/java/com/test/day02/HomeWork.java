package com.test.day02;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.test.day02.pojo.CaseInfo;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

/**
 * @author 小鱼干
 * @description:easyPoi读取Excel数据
 * @date 2020/12/17 - 17:05
 */
public class HomeWork {
    @Test(dataProvider = "getCasesDatas")
    public void outputDatas(CaseInfo cases){
        System.out.println(cases);
    }

    @DataProvider
    public Object[] getCasesDatas(){
        //1.初始化读取配置对象
        ImportParams importParams = new ImportParams();
        //2.设置读取sheet的起始索引
        importParams.setStartSheetIndex(0);
        //3.设置读取sheet的数量
        importParams.setSheetNum(2);
        //4.初始化File对象
        File excelFile = new File("src/test/resources/api_testcases_futureloan_v1.xls");
        //5.//读取excel中的sheet转成对象的List集合
        List<Object> list = ExcelImportUtil.importExcel(excelFile, CaseInfo.class, importParams);
        //6.将list集合转成一维数组
        Object[] datas = list.toArray();
        return datas;
    }
}
