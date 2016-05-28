package com.example.wireframe.protocal.protocalProcess.model;


public class ResponseCommonData extends Base {
    private static final long serialVersionUID = 6957941053538656395L;

    // 业务指令编码
    public String busiCode = "";

    // 服务器响应时间戳，代为毫秒
    public String serverTime = "";

    // 用户信息-用户ID
    public String userInfo_uid = "";

    // 用户信息-回话ID
    public String userInfo_jsessionId = "";

    // 响应结果-结果编码-0:成功,-x:失败
    public String result_code = "";

    // 响应结果-错误提示文字
    public String result_msg = "";

    // 响应结果-外链
    public String result_url = "";

    @Override
    public String toString() {
        return "ResponseCommonData [busiCode=" + busiCode + ", serverTime=" + serverTime + ", userInfo_uid="
                + userInfo_uid + ", userInfo_jsessionId=" + userInfo_jsessionId + ", result_code=" + result_code
                + ", result_msg=" + result_msg + ", result_url=" + result_url + "]";
    }

}
