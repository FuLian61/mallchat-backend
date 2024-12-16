package com.fulian.mallchat.common.common.event.listener;

import com.fulian.mallchat.common.common.event.UserOnlineEvent;
import com.fulian.mallchat.common.common.event.UserRegisterEvent;
import com.fulian.mallchat.common.user.domain.entity.User;
import com.fulian.mallchat.common.user.domain.enums.IdempotentEnum;
import com.fulian.mallchat.common.user.domain.enums.ItemEnum;
import com.fulian.mallchat.common.user.domain.enums.UserActiveStatusEnum;
import com.fulian.mallchat.common.user.service.IpService;
import com.fulian.mallchat.common.user.service.UserBackpackService;
import com.fulian.mallchat.common.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class UserOnlineListener {

    @Autowired
    private UserService userService;

    @Autowired
    private IpService ipService;

    @Async
    @TransactionalEventListener(classes = UserOnlineEvent.class,phase = TransactionPhase.AFTER_COMMIT,fallbackExecution = true)
    public void sendDB(UserOnlineEvent event) {
        User user = event.getUser();
        User update = new User();
        update.setId(user.getId());
        update.setLastOptTime(user.getLastOptTime());
        update.setIpInfo(user.getIpInfo());
        update.setActiveStatus(UserActiveStatusEnum.ONLINE.getStatus());
        userService.updateById(update);
        // 用户ip详情的解析
        ipService.refreshIpDetailAsync(user.getId());
    }

}
