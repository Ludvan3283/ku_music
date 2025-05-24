package com.ludvan.kumusic.service.impl;

import cn.hutool.json.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.ludvan.kumusic.service.SmsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {

    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;

    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;

    @Value("${aliyun.sms.signName}")
    private String signName;

    @Value("${aliyun.sms.templateCode}")
    private String templateCode;

    private DefaultAcsClient initClient() {
        DefaultProfile profile = DefaultProfile.getProfile("default", accessKeyId, accessKeySecret);
        return new DefaultAcsClient(profile);
    }

    @Override
    public String sendSms(String phoneNumber, String code) throws ClientException {
        DefaultAcsClient client = initClient();
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phoneNumber);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            String responseData = response.getData();
            System.out.println(responseData);
            // 解析响应数据
            JSONObject jsonResponse = new JSONObject(responseData);
            String msg = jsonResponse.getStr("Code");
            if ("OK".equals(msg)) {
                return "验证码发送成功";
            } else if ("isv.BUSINESS_LIMIT_CONTROL".equals(msg)) {
                return "发送验证码过于频繁，请稍后再试";
            } else {
                return "发送验证码失败: " + jsonResponse.getStr("Message");
            }
        } catch (ClientException e) {
            System.err.println("Failed to send SMS: " + e.getMessage());
            return String.format("{\"Message\":\"%s\",\"RequestId\":\"%s\",\"Code\":\"%s\"}",
                    e.getErrMsg(), e.getRequestId(), e.getErrCode());
        }
    }
}
