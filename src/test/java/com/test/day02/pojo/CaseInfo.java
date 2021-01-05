package com.test.day02.pojo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @author 小鱼干
 * @description:easypoi映射的实体类，类中的属性需要与Excel表头保持一致
 * @date 2020/12/17 - 16:15
 */
public class CaseInfo {
    //序号(caseId)
    @Excel(name = "序号(caseId)")
    private int caseId;
    //接口模块(interface)
    @Excel(name = "接口模块(interface)")
    private String interfaceName;
    //用例标题(title)
    @Excel(name = "用例标题(title)")
    private String caseTitle;
    //请求头(requestHeader)
    @Excel(name = "请求头(requestHeader)")
    private String requestHeader;
    //请求方式(method)
    @Excel(name = "请求方式(method)")
    private String method;
    //接口地址(url)
    @Excel(name = "接口地址(url)")
    private String url;
    //参数输入(inputParams)
    @Excel(name = "参数输入(inputParams)")
    private String params;
    //期望返回结果(expected)
    @Excel(name = "期望返回结果(expected)")
    private String expected;

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getCaseTitle() {
        return caseTitle;
    }

    public void setCaseTitle(String caseTitle) {
        this.caseTitle = caseTitle;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getExpected() {
        return expected;
    }

    public void setExpected(String expected) {
        this.expected = expected;
    }

    @Override
    public String toString() {
        return "CaseInfo{" +
                "caseId=" + caseId +
                ", interfaceName='" + interfaceName + '\'' +
                ", caseTitle='" + caseTitle + '\'' +
                ", requestHeader='" + requestHeader + '\'' +
                ", method='" + method + '\'' +
                ", url='" + url + '\'' +
                ", params='" + params + '\'' +
                ", expected='" + expected + '\'' +
                '}';
    }
}
