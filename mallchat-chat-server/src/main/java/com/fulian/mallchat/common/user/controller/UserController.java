package com.fulian.mallchat.common.user.controller;

import com.fulian.mallchat.common.common.domain.dto.RequestInfo;
import com.fulian.mallchat.common.common.domain.vo.resp.ApiResult;
import com.fulian.mallchat.common.common.interceptor.TokenInterceptor;
import com.fulian.mallchat.common.common.utils.RequestHolder;
import com.fulian.mallchat.common.user.domain.vo.resp.UserInfoResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/capi/user")
@Api(tags = "用户相关接口")
public class UserController {

    @GetMapping("/userInfo")
    @ApiOperation("获取用户个人信息")
    public ApiResult<UserInfoResp> getUserInfo(){
        RequestInfo requestInfo = RequestHolder.get();
        System.out.println(requestInfo.getUid());
        return null;
    }
}
