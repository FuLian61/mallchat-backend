package com.fulian.mallchat.common.common.service;

import com.fulian.mallchat.common.common.exception.BusinessException;
import com.fulian.mallchat.common.common.exception.CommonErrorEnum;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LockService {
    @Autowired
    private RedissonClient redissonClient;

    @SneakyThrows
    public <T> T executeWithLock(String key, int waitTime, TimeUnit timeUnit, Supplier<T> supplier) {
        RLock lock = redissonClient.getLock(key);
        boolean success = lock.tryLock(waitTime, timeUnit);
        if (!success){
            throw new BusinessException(CommonErrorEnum.LOCK_LIMIT);
        }
        try {
            return supplier.get();
        }finally {
            lock.unlock();
        }
    }

    public <T> T executeWithLock(String key, Supplier<T> supplier) {
        return executeWithLock(key,-1, TimeUnit.MILLISECONDS, supplier);
    }

    public <T> T executeWithLock(String key, Runnable runnable) {
        return executeWithLock(key,-1, TimeUnit.MILLISECONDS, ()->{
            runnable.run();
            return null;
        });
    }

    @FunctionalInterface
    public interface Supplier<T> {
        T get() throws Throwable;
    }
}
