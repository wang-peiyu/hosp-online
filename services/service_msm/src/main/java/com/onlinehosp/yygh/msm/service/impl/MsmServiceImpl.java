package com.onlinehosp.yygh.msm.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.onlinehosp.yygh.msm.service.MsmService;
import com.onlinehosp.yygh.msm.utils.ConstantPropertiesUtils;
import com.onlinehosp.yygh.vo.msm.MsmVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean send(String phone, String code) {
       if(StringUtils.isEmpty(phone))
       {
           return false;
       }
        DefaultProfile profile=DefaultProfile.getProfile(ConstantPropertiesUtils.REGION_ID,
                ConstantPropertiesUtils.ACCESS_KEY_ID,
                ConstantPropertiesUtils.SECRECT);
        IAcsClient client=new DefaultAcsClient(profile);
        CommonRequest request=new CommonRequest();

        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers",phone);
        request.putQueryParameter("SignName","我的预约挂号尚医通的学习");
        request.putQueryParameter("TemplateCode","SMS_464796240");
        Map<String,Object> map=new HashMap<>();
        map.put("code",code);
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(map));
        try{
            CommonResponse response=client.getCommonResponse(request);

            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        }catch (ServerException e){
               e.printStackTrace();
        }catch (ClientException e){
            e.printStackTrace();
        }
        return false;
    }


    private boolean send(String phone, Map<String,Object> param) {
        if(StringUtils.isEmpty(phone))
        {
            return false;
        }
        DefaultProfile profile=DefaultProfile.getProfile(ConstantPropertiesUtils.REGION_ID,
                ConstantPropertiesUtils.ACCESS_KEY_ID,
                ConstantPropertiesUtils.SECRECT);
        IAcsClient client=new DefaultAcsClient(profile);
        CommonRequest request=new CommonRequest();

        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers",phone);
        request.putQueryParameter("SignName","我的预约挂号尚医通的学习");
        request.putQueryParameter("TemplateCode","SMS_464796240");

        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(param));
        try{
            CommonResponse response=client.getCommonResponse(request);

            System.out.println(response.getData());
            return response.getHttpResponse().isSuccess();
        }catch (ServerException e){
            e.printStackTrace();
        }catch (ClientException e){
            e.printStackTrace();
        }
        return false;
    }
    //mq发送短信封装
    @Override
    public boolean send(MsmVo msmVo) {
        if(!StringUtils.isEmpty(msmVo.getPhone())) {

            boolean isSend = this.send(msmVo.getPhone(),msmVo.getParam());
            return isSend;
        }
        return false;
    }
}
