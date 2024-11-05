package com.fulian.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.fulian.mallchat.common.user.domain.entity.User;
import com.fulian.mallchat.common.user.service.UserService;
import com.fulian.mallchat.common.user.service.WXMsgService;
import com.fulian.mallchat.common.user.service.adapter.TextBuilder;
import com.fulian.mallchat.common.user.service.adapter.UserAdapter;
import com.fulian.mallchat.common.websocket.service.WebsocketService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class WXMsgServiceImpl implements WXMsgService {

    @Value("${wx.mp.callback}")
    private String callback;

    public static final String URL = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
    /**
     * openId 和 登录 code 的关系 map
     */
    private static final ConcurrentHashMap<String,Integer> WAIT_AUTHORIZE_map = new ConcurrentHashMap<>();
    @Autowired
    private UserService userService;

    @Autowired
    @Lazy
    private WxMpService wxMpService;

    @Autowired
    private WebsocketService websocketService;

    /**
     * 用户扫码成功
     * @param wxMpXmlMessage
     * @return
     */
    @Override
    public WxMpXmlOutMessage scan(WxMpXmlMessage wxMpXmlMessage) {
        String openId = wxMpXmlMessage.getFromUser();
        Integer code = getEventKey(wxMpXmlMessage);
        if (Objects.isNull(code)){
            return null;
        }
        User user = userService.getByOpenId(openId);
        boolean registered = Objects.nonNull(user);
        boolean authorized = registered && StrUtil.isNotBlank(user.getAvatar());
        // 用户已经注册并授权
        if (registered && authorized){
            // 走登录成功逻辑 通过 code 给 channel 推送消息
            websocketService.scanLoginSuccess(code,user.getId());
        }
        // 用户未注册，就先注册
        if (!registered){
            User insert = UserAdapter.buildUserSave(openId);
            userService.register(insert);
        }

        // 推送链接让用户授权
        WAIT_AUTHORIZE_map.put(openId,code);
        websocketService.waitAuthorize(code);
        String authorizeUrl = String.format(URL, wxMpService.getWxMpConfigStorage().getAppId(), URLEncoder.encode(callback + "/wx/portal/public/callBack"));
        System.out.println(authorizeUrl);
        return TextBuilder.build("请点击登录：<a href=\"" + authorizeUrl + "\">登录</a>", wxMpXmlMessage);

    }

    @Override
    public void authorize(WxOAuth2UserInfo userInfo) {
        String openid = userInfo.getOpenid();
        User user = userService.getByOpenId(openid);
        // 更新用户信息
        if (StrUtil.isBlank(user.getAvatar())) {
            fillUserInfo(user.getId(),userInfo);
        }
        //通过 code 找到 用户 channel 进行登录
        Integer code = WAIT_AUTHORIZE_map.remove(openid);
        websocketService.scanLoginSuccess(code,user.getId());
    }

    private void fillUserInfo(Long uid, WxOAuth2UserInfo userInfo) {
        User user = UserAdapter.buildAuthorizeUser(uid,userInfo);
        userService.updateById(user);
    }

    private Integer getEventKey(WxMpXmlMessage wxMpXmlMessage) {
        try {
            String eventKey = wxMpXmlMessage.getEventKey();
            String code = eventKey.replace("qrscene_", "");
            return Integer.valueOf(code);
        } catch (NumberFormatException e) {
            log.error("getEventKey error eventKey:{}",wxMpXmlMessage.getEventKey(),e);
            return null;
        }
    }
}
