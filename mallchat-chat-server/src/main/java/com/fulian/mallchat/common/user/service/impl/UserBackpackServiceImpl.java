package com.fulian.mallchat.common.user.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fulian.mallchat.common.common.annotation.RedissonLock;
import com.fulian.mallchat.common.common.domain.enums.YesOrNoEnum;
import com.fulian.mallchat.common.common.service.LockService;
import com.fulian.mallchat.common.user.domain.entity.UserBackpack;
import com.fulian.mallchat.common.user.domain.enums.IdempotentEnum;
import com.fulian.mallchat.common.user.mapper.UserBackpackMapper;
import com.fulian.mallchat.common.user.service.UserBackpackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;

/**
* @author fulian
* @description 针对表【user_backpack(用户背包表)】的数据库操作Service实现
* @createDate 2024-11-24 21:20:42
*/
@Service
public class UserBackpackServiceImpl extends ServiceImpl<UserBackpackMapper, UserBackpack>
    implements UserBackpackService {

    @Autowired
    private LockService lockService;

    @Autowired
    @Lazy
    private UserBackpackServiceImpl userBackpackServiceImpl;

    @Override
    public Integer getCountByValidItemId(Long uid, Long itemId) {
        return lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, itemId)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .count();
    }

    @Override
    public UserBackpack getFirstValidItem(Long uid, Long id) {
        return lambdaQuery()
                .eq(UserBackpack::getUid, uid)
                .eq(UserBackpack::getItemId, id)
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .orderByAsc(UserBackpack::getId)
                .last("limit 1")
                .one();
    }

    @Override
    public boolean userItem(UserBackpack modifyNameItem) {
        return lambdaUpdate()
                .eq(UserBackpack::getId, modifyNameItem.getId())
                .eq(UserBackpack::getStatus, YesOrNoEnum.NO.getStatus())
                .set(UserBackpack::getStatus, YesOrNoEnum.YES.getStatus())
                .update();
    }

    @Override
    public List<UserBackpack> getByItemIds(Long uid, List<Long> itemIds) {
        return lambdaQuery()
                .eq(UserBackpack::getUid,uid)
                .eq(UserBackpack::getStatus,YesOrNoEnum.NO.getStatus())
                .in(UserBackpack::getItemId,itemIds)
                .list();
    }

    @Override
    public void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        String idempotent = getIdempotent(itemId,idempotentEnum,businessId);
        userBackpackServiceImpl.doAcquireItem(uid,itemId,idempotent);

    }

    @RedissonLock(key = "#idempotent",waitTime = 5000)
    public void doAcquireItem(Long uid, Long itemId, String idempotent) {
        UserBackpack userBackpack = this.getByIdempotent(idempotent);
        if (Objects.nonNull(userBackpack)) {
            return ;
        }
        // 业务检查
        // 发放物品
        UserBackpack insert = UserBackpack.builder()
                .uid(uid)
                .itemId(itemId)
                .status(YesOrNoEnum.NO.getStatus())
                .idempotent(idempotent)
                .build();

        this.save(insert);
    }

    private String getIdempotent(Long itemId, IdempotentEnum idempotentEnum, String businessId) {
        return String.format("%d_%d_%s",itemId,idempotentEnum.getType(),businessId);
    }

    @Override
    public UserBackpack getByIdempotent(String idempotent) {
        return lambdaQuery()
                .eq(UserBackpack::getIdempotent,idempotent)
                .one();
    }
}




