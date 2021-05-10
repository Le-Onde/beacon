package com.shida.educenter.controller;

import com.google.gson.Gson;
import com.shida.commonutils.JwtUtils;
import com.shida.educenter.entity.UcenterMember;
import com.shida.educenter.service.UcenterMemberService;
import com.shida.educenter.utils.ConstantWxUtils;
import com.shida.educenter.utils.HttpClientUtils;
import com.shida.servicebase.exceptionhandler.LeOndeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.URLEncoder;
import java.util.HashMap;

@Controller //只请求地址 不需要返回数据
@CrossOrigin
@RequestMapping("/api/ucenter/wx")
public class WxApiController {
    @Autowired
    private UcenterMemberService memberService;
    // 获取扫描人信息 添加数据
    @GetMapping("callback")
    public String callback(String code,String state){
        try {
            // 获取code值 临时票据 类似于验证码

            // 拿着code请求微信固定地址 得到两个值access_key和open_id
            String baseAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=%s" +
                    "&secret=%s" +
                    "&code=%s" +
                    "&grant_type=authorization_code";
            //拼接三个参数 id 密钥 code
            String accessTokenUrl = String.format(
                    baseAccessTokenUrl,
                    ConstantWxUtils.WX_OPEN_APP_ID,
                    ConstantWxUtils.WX_OPEN_APP_SECRET,
                    code
            );

            //请求拼接好的地址  得到返回两个值access_token和openid

            String accessTokenUrlInfo = HttpClientUtils.get(accessTokenUrl);

            //从accessTokenInfo字符串获取出来两个值 access_token和openid
            //把accessTokenInfo字符串转换map集合 根据map里面key获取对应值
            //使用json转换工具Gson
            Gson gson = new Gson();
            HashMap mapAccessToken = gson.fromJson(accessTokenUrlInfo,HashMap.class);
            String access_token = (String)mapAccessToken.get("access_token");
            String openid = (String)mapAccessToken.get("openid");

            //把扫描人信息加到数据库
            //判断是否存在相同信息数据库里
            UcenterMember ucenterMember = memberService.getOpenIdMember(openid);
            if(ucenterMember == null){

                //拿着得到的access_token和openid 再去请求微信提供的固定地址，获取扫码人信息
                //访问微信的资源服务器，获取用户信息
                String baseUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                        "?access_token=%s" +
                        "&openid=%s";
                //拼接两个参数
                String userInfoUrl = String.format(
                        baseUserInfoUrl,
                        access_token,
                        openid
                );
                //发送请求
                String UserInfo = HttpClientUtils.get(userInfoUrl);
                //获取返回UserInfo字符串的扫码人信息
                HashMap userInfoMap = gson.fromJson(UserInfo,HashMap.class);
                String nickname = (String)userInfoMap.get("nickname"); //昵称
                String headimgurl = (String)userInfoMap.get("headimgurl"); //头像

                //如果是空 表中没有数据 添加
                ucenterMember = new UcenterMember();
                ucenterMember.setOpenid(openid);
                ucenterMember.setNickname(nickname);
                ucenterMember.setAvatar(headimgurl);
                memberService.save(ucenterMember);
            }
            //使用jwt根据member对象生成token字符串
            String JwtToken = JwtUtils.getJwtToken(ucenterMember.getId(),ucenterMember.getNickname());
            //最后返回首页面 通过路径传递token字符串
            return "redirect:http://localhost:3000?token="+JwtToken;

        }catch (Exception e){
            throw new LeOndeException(20001,"登陆失败");
        }
    }

    //生成二维码
    @GetMapping("login")
    public String getWxCode(){
        String baseUrl = "https://open.weixin.qq.com/connect/qrconnect" +
                "?appid=%s" +
                "&redirect_uri=%s" +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=%s" +
                "#wechat_redirect";
        //对Url进行编码
        String redirectUrl = ConstantWxUtils.WX_OPEN_REDIRECT_URL;
        try {
            redirectUrl = URLEncoder.encode(redirectUrl,"utf8");
        }catch (Exception e){
            e.printStackTrace();
        }

        //设置%s里面值
        String url = String.format(
                baseUrl,
                ConstantWxUtils.WX_OPEN_APP_ID,
                redirectUrl,
                "LeOnde"
                );
        //重定向到请求微信地址里
        return "redirect:"+url;
    }
}
