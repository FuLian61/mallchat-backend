package com.fulian.mallchat.common.common.interceptor;

import cn.hutool.http.ContentType;
import com.fulian.mallchat.common.common.exception.HttpErrorEnum;
import com.fulian.mallchat.common.user.service.LoginService;
import org.apache.commons.io.Charsets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;
import java.util.Optional;

@Component
public class TokenInterceptor implements HandlerInterceptor {


    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String AUTHORIATION_SCHEMA = "Bearer ";
    public static final String UID = "uid";

    @Autowired
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = getToken(request);
        Long validUid = loginService.getValidUid(token);
        if (Objects.nonNull(validUid)) {
            // 用户有登录态
            request.setAttribute(UID, validUid);
        } else {
            // 未登录
            boolean isPublicURI = isPublicURI(request);
            if (!isPublicURI) {
                // 401
                HttpErrorEnum.ACCESS_DENIED.sendHttpError(response);
                return false;
            }
        }
        return true;
    }

    private static boolean isPublicURI(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] split = requestURI.split("/");
        boolean isPublicURI = split.length > 2 && "public".equalsIgnoreCase(split[3]);
        return isPublicURI;
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(HEADER_AUTHORIZATION);
        return Optional.ofNullable(header)
                .filter(h -> h.startsWith(AUTHORIATION_SCHEMA))
                .map(h -> h.replaceFirst(AUTHORIATION_SCHEMA,""))
                .orElse(null);
    }
}
