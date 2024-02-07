package com.onlinehosp.yygh.user.api;

import com.alibaba.fastjson.JSONObject;
import com.onlinehosp.yygh.common.helper.JwtHelper;
import com.onlinehosp.yygh.common.result.Result;
import com.onlinehosp.yygh.model.user.UserInfo;
import com.onlinehosp.yygh.user.service.UserInfoService;
import com.onlinehosp.yygh.user.utils.ConstantWxPropertiesUtils;
import com.onlinehosp.yygh.user.utils.HttpClientUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/ucenter/wx")

public class WeixinApiController {
    @Autowired
    private UserInfoService userInfoService;
    //1.生成微信扫描二维码
    @GetMapping("getLoginParam")
    @ResponseBody
    public Result genQrConnect() throws UnsupportedEncodingException {
        String redirectUri = ConstantWxPropertiesUtils.WX_OPEN_REDIRECT_URL;
        String wxOpenRedirectUrl=URLEncoder.encode(redirectUri,"utf-8");
        Map<String, Object> map = new HashMap<>();
        map.put("appid", ConstantWxPropertiesUtils.WX_OPEN_APP_ID);
        map.put("scope", "snsapi_login");
        map.put("redirect_uri", wxOpenRedirectUrl);

        map.put("state", System.currentTimeMillis()+"");//System.currentTimeMillis()+""
        return Result.ok(map);
    }



    //2.回调方法，得到扫码人的信息
    @GetMapping("callback")
    public String callback(String code, String state) {
        //第一步 获取临时票据
        System.out.println("code:"+code);
        //第二部 根据code 和微信id 和密钥 请求微信固定地址，得到两个值
        //access_token和openid
        StringBuffer baseAccessTokenUrl = new StringBuffer()
                .append("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=%s")
                .append("&secret=%s")
                .append("&code=%s")
                .append("&grant_type=authorization_code");

        String accessTokenUrl = String.format(baseAccessTokenUrl.toString(),
                ConstantWxPropertiesUtils.WX_OPEN_APP_ID,
                ConstantWxPropertiesUtils.WX_OPEN_APP_SECRET,
                code);
        try {
           String accesstokenInfo= HttpClientUtils.get(accessTokenUrl);
            System.out.println(accesstokenInfo);

            JSONObject jsonObject=JSONObject.parseObject(accesstokenInfo);
           String access_token= jsonObject.getString("access_token");
           String openid= jsonObject.getString("openid");
           //判断数据库中是否存在扫码人信息
            //根据openid判断
            UserInfo userInfo=userInfoService.selectWxInfoOpenId(openid);
            if(userInfo==null)
            {
//拿着openid和access_token请求微信地址，得到扫码人信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                String userInfoUrl = String.format(baseUserInfoUrl, access_token, openid);
                String resultInfo=HttpClientUtils.get(userInfoUrl);
                System.out.println("resultInfo:"+resultInfo);
                JSONObject resultUserInfoJson = JSONObject.parseObject(resultInfo);
                //解析用户信息，得到用户昵称和头像
                String nickname = resultUserInfoJson.getString("nickname");
                String headimgurl = resultUserInfoJson.getString("headimgurl");
                userInfo=new UserInfo();
                userInfo.setOpenid(openid);
                userInfo.setNickName(nickname);
                userInfo.setStatus(1);
                userInfoService.save(userInfo);
            }

            Map<String, String> map = new HashMap<>();
            String name = userInfo.getName();
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getNickName();
            }
            if(StringUtils.isEmpty(name)) {
                name = userInfo.getPhone();
            }
            map.put("name", name);
            //前端判断，如果openid为空，不需要绑定手机号，如果不为空，需要绑定手机号
            if(StringUtils.isEmpty(userInfo.getPhone())) {
                map.put("openid", userInfo.getOpenid());
            } else {
                map.put("openid", "");
            }
            String token = JwtHelper.createToken(userInfo.getId(), name);
            map.put("token", token);
            return "redirect:" + ConstantWxPropertiesUtils.YYGH_BASE_URL + "/weixin/callback?token="+map.get("token")+"&openid="+map.get("openid")+"&name="+URLEncoder.encode(map.get("name"),"utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}