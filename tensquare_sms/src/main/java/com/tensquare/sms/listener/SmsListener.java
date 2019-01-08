package com.tensquare.sms.listener;

import com.aliyuncs.exceptions.ClientException;
import com.tensquare.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "sms")
public class SmsListener {

    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String template_code;

    @Value("${aliyun.sms.sign_name}")
    private String sign_name;

    /**
     * 发送短信
     */
    @RabbitHandler
    public void sendSms(Map message){
        System.out.println("手机号："+message.get("mobile"));
        System.out.println("验证码："+message.get("checkCode"));
        String mobile = (String) message.get("mobile");
        String checkCode= (String) message.get("checkCode");
        //把信息发给阿里,阿里会按照模板发送短信到用户手机
        try {
            smsUtil.sendSms(mobile,template_code,sign_name,"{\"number\":"+checkCode+"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }

    }
}
