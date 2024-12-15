package com.fulian.mallchat.common.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fulian.mallchat.common.user.domain.entity.ItemConfig;

import java.util.List;

/**
* @author fulian
* @description 针对表【item_config(功能物品配置表)】的数据库操作Service
* @createDate 2024-11-24 21:20:36
*/
public interface ItemConfigService extends IService<ItemConfig> {

    List<ItemConfig> getByType(Integer itemType);
}
