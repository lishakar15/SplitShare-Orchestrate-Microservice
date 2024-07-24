package com.splitwise.Activity_Service.service;

import com.splitwise.Activity_Service.clients.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CacheService {
    @Autowired
    UserClient userClient;

    @Cacheable(value ="userNameCache", key ="#groupId")
    public Map<Long,String> getUserNameMap(Long groupId)
    {
        return userClient.getUserNameMap(groupId);
    }

    @CacheEvict(value ="userNameCache", key ="#groupId")
    public void evictCacheByKey(Long groupId)
    {

    }

}
