package com.splitwise.Activity_Service.clients;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "USER-SERVICE")
public interface UserClient {

    @GetMapping("/user/get-user-name-map/{groupId}")
    public Map<Long,String> getUserNameMap(@PathVariable("groupId") Long groupId);
    @PostMapping("user/get-user-email-map")
    public Map<Long,String> getUserEmailMapByUserIds(List<Long> userIds);
}
