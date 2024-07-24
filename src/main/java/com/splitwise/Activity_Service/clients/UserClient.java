package com.splitwise.Activity_Service.clients;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "user-service", url = "http://localhost:8080/")
public interface UserClient {

    @GetMapping("/user/get-user-name-map/{groupId}")
    public Map<Long,String> getUserNameMap(@PathVariable("groupId") Long groupId);
}
