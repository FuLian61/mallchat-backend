package com.fulian.mallchat.common.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.fulian.mallchat.common.common.constant.RedisKey;
import com.fulian.mallchat.common.common.utils.JwtUtils;
import com.fulian.mallchat.common.common.utils.RedisUtils;
import com.fulian.mallchat.common.user.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    public static final int TOKEN_EXPIRE_DAYS = 3;
    public static final int TOKEN_RENEWAL_DAYS = 1;
    @Autowired
    private JwtUtils jwtUtils;

    ThreadPoolExecutor executor;

    /**
     * 刷新token有效期
     *
     * @param token
     */
    @Override
    public void renewalTokenIfNecessary(String token) {

        Long uid = getValidUid(token);
        String userTokenKey = getUserTokenKey(uid);
        Long expireDays = RedisUtils.getExpire(userTokenKey, TimeUnit.DAYS);
        if (expireDays == -2){ // 不存在的key
           return;
        }
        if(expireDays < TOKEN_RENEWAL_DAYS) { //小于一天的token帮忙续期
            RedisUtils.expire(getUserTokenKey(uid),TOKEN_EXPIRE_DAYS,TimeUnit.DAYS);
        }
    }

    @Override
    public String login(Long uid) {
        String token = jwtUtils.createToken(uid);
        RedisUtils.set(getUserTokenKey(uid),token, TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }

    @Override
    public Long getValidUid(String token) {
        Long uid = jwtUtils.getUidOrNull(token);
        if (Objects.isNull(uid)){
            return null;
        }
        String oldToken = RedisUtils.get(getUserTokenKey(uid));
        return Objects.equals(oldToken, token) ? uid : null;
    }

    private String getUserTokenKey(Long uid){
        return RedisKey.getKey(RedisKey.USER_TOKEN_STRING,uid);
    }

}
