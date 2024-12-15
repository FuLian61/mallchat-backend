package com.fulian.mallchat.common;

import com.fulian.mallchat.common.common.thread.MyUncaughtExceptionHandler;
import com.fulian.mallchat.common.common.utils.JwtUtils;
import com.fulian.mallchat.common.user.domain.enums.IdempotentEnum;
import com.fulian.mallchat.common.user.domain.enums.ItemEnum;
import com.fulian.mallchat.common.user.service.LoginService;
import com.fulian.mallchat.common.user.service.UserBackpackService;
import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TmpTest {

    public static final long UID = 11001L;
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private UserBackpackService userBackpackService;

    @Test
    public void jwt() throws InterruptedException {
//        System.out.println(jwtUtils.createToken(1L));
//        Thread.sleep(1000);
        System.out.println(jwtUtils.createToken(1L));

    }
    @Autowired
    private LoginService loginService;

    @Test
    public void redis() {
//        RedisUtils.set("name","卷心菜");
//        System.out.println(RedisUtils.get("name"));
        String s = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjExMDAxLCJjcmVhdGVUaW1lIjoxNzMwNjQyOTcwfQ.fVimEWz4MDmR4yz4Z4owh9dJL8IKQSHlL0fG7uhJwic";
        System.out.println(loginService.login(UID));
        System.out.println(loginService.getValidUid(s));
    }

    @Test
    public void acquireItem() {
        userBackpackService.acquireItem(UID, ItemEnum.LIKE_BADGE.getId(), IdempotentEnum.UID,UID+"");
    }

    @Autowired
    private RedissonClient redissonClient;
    @Test
    public void redission() {
        RLock lock = redissonClient.getLock("123");
        lock.lock();
        System.out.println();
        lock.unlock();
    }


    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void thread() throws InterruptedException {
        threadPoolTaskExecutor.execute(()->{
            if (1 ==1){
                log.error("123");
                throw new RuntimeException("1234");
            }
        });
        Thread.sleep(200);
    }
}