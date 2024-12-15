package com.fulian.mallchat.common.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fulian.mallchat.common.user.domain.entity.UserBackpack;
import com.fulian.mallchat.common.user.domain.enums.IdempotentEnum;

import java.util.List;

/**
* @author fulian
* @description 针对表【user_backpack(用户背包表)】的数据库操作Service
* @createDate 2024-11-24 21:20:42
*/
public interface UserBackpackService extends IService<UserBackpack> {

    Integer getCountByValidItemId(Long uid, Long itemId);

    UserBackpack getFirstValidItem(Long uid, Long id);

    boolean userItem(UserBackpack modifyNameItem);

    List<UserBackpack> getByItemIds(Long uid, List<Long> collect);

    /**
     * 给用户发放一个物品
     * @param uid 用户id
     * @param itemId 物品id
     * @param idempotentEnum 幂等类型
     * @param businessId 幂等唯一标识
     */
    void acquireItem(Long uid, Long itemId, IdempotentEnum idempotentEnum,String businessId);

    UserBackpack getByIdempotent(String idempotent);
}
