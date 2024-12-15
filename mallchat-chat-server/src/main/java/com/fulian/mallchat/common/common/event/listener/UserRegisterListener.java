package com.fulian.mallchat.common.common.event.listener;

import com.fulian.mallchat.common.common.event.UserRegisterEvent;
import com.fulian.mallchat.common.user.domain.entity.User;
import com.fulian.mallchat.common.user.domain.enums.IdempotentEnum;
import com.fulian.mallchat.common.user.domain.enums.ItemEnum;
import com.fulian.mallchat.common.user.service.UserBackpackService;
import com.fulian.mallchat.common.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserRegisterListener {

    @Autowired
    private UserBackpackService userBackpackService;

    @Autowired
    private UserService userService;

    @Async
    @TransactionalEventListener(classes = UserRegisterEvent.class,phase = TransactionPhase.AFTER_COMMIT)
    public void sendCard(UserRegisterEvent event) {
        User user = event.getUser();
        userBackpackService.acquireItem(user.getId(), ItemEnum.MODIFY_NAME_CARD.getId(), IdempotentEnum.UID,user.getId().toString());
    }

    @TransactionalEventListener(classes = UserRegisterEvent.class,phase = TransactionPhase.AFTER_COMMIT)
    public void sendBadge(UserRegisterEvent event) {

        User user = event.getUser();
        int registeredCount = userService.count();
        if (registeredCount < 10){
            // 前十名的注册徽章
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP10_BADGE.getId(), IdempotentEnum.UID,user.getId().toString());
        }else if (registeredCount < 100){
            // 前百名的注册徽章
            userBackpackService.acquireItem(user.getId(), ItemEnum.REG_TOP100_BADGE.getId(), IdempotentEnum.UID,user.getId().toString());
        }

    }
}
