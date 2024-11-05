package com.fulian.mallchat.common.websocket.service;

import io.netty.channel.Channel;

public interface WebsocketService {

    /**
     * 处理所有ws连接的事件
     *
     * @param channel
     */
    void connect(Channel channel);

    /**
     * 处理用户登录请求，需要返回一张带code的二维码
     *
     * @param channel
     */
    void handleLoginReq(Channel channel);

    void remove(Channel channel);

    void scanLoginSuccess(Integer code, Long uid);

    /**
     * 等待授权
     * @param code
     */
    void waitAuthorize(Integer code);
}
