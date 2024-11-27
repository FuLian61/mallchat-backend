package com.fulian.mallchat.common.user.controller;

import com.fulian.mallchat.common.common.domain.dto.RequestInfo;
import com.fulian.mallchat.common.common.domain.vo.resp.ApiResult;
import com.fulian.mallchat.common.common.interceptor.TokenInterceptor;
import com.fulian.mallchat.common.common.utils.RequestHolder;
import com.fulian.mallchat.common.user.domain.vo.req.ModifyNameReq;
import com.fulian.mallchat.common.user.domain.vo.resp.UserInfoResp;
import com.fulian.mallchat.common.user.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/capi/user")
@Api(tags = "用户相关接口")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    @ApiOperation("获取用户个人信息")
    public ApiResult<UserInfoResp> getUserInfo(){
        return ApiResult.success(userService.getUserInfo(RequestHolder.get().getUid()));
    }

    @PutMapping("/name")
    @ApiOperation("修改用户名")
    public ApiResult<UserInfoResp> modifyName(@Valid @RequestBody ModifyNameReq req){
        userService.modifyName(RequestHolder.get().getUid(),req.getName());
        return ApiResult.success();
    }

}
