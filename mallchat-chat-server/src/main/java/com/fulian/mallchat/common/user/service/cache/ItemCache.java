package com.fulian.mallchat.common.user.service.cache;

import com.fulian.mallchat.common.user.domain.entity.ItemConfig;
import com.fulian.mallchat.common.user.service.ItemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemCache {
    @Autowired
    private ItemConfigService itemConfigService;

    @Cacheable(cacheNames = "item",key = "'itemsByType:'+#itemType")
    public List<ItemConfig> getByType(Integer itemType) {
        return itemConfigService.getByType(itemType);
    }

    @CacheEvict(cacheNames = "item",key = "'itemsByType:'+#itemType")
    public void evictByType(Integer itemType) {
    }

}
