package com.fulian.mallchat.common.common.constant;

public class RedisKey {

    public static final String BASE_KEY = "mallchat:chat";

    /**
     * 用户 token 的 key
     */
    public static final String USER_TOKEN_STRING = "userToken:uid_%d";

    public static String getKey(String key, Object... o){
        return BASE_KEY + String.format(key, o);
    }
}
