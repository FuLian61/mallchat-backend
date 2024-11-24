package com.fulian.mallchat.common.common.exception;


import cn.hutool.http.ContentType;
import com.fulian.mallchat.common.common.domain.vo.resp.ApiResult;
import com.fulian.mallchat.common.common.utils.JsonUtils;
import lombok.AllArgsConstructor;
import org.apache.commons.io.Charsets;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@AllArgsConstructor
public enum HttpErrorEnum {
    ACCESS_DENIED(401,"登录失效请重新登录");

    private Integer httpCode;
    private String desc;

    public void sendHttpError(HttpServletResponse response) throws IOException {
        response.setStatus(httpCode);
        response.setContentType(ContentType.JSON.toString(Charsets.UTF_8));
        response.getWriter().write(JsonUtils.toStr(ApiResult.fail(httpCode,desc)));
    }
}
