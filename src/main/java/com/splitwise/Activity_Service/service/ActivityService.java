package com.splitwise.Activity_Service.service;

import com.splitwise.Activity_Service.entity.Activity;
import com.splitwise.Activity_Service.repository.ActivityRepository;
import com.splitwise.Activity_Service.repository.ChangeLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ActivityService {
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    ChangeLogRepository changeLogRepository;
    @Autowired
    CacheService cacheService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);


    public List<Activity> getActivitiesByGroupId(Long groupId) {
        List<Activity> activityList = activityRepository.getActivityByGroupId(groupId);
        if(activityList != null && !activityList.isEmpty() )
        {
            //Get UserNames from Cache
            Map<Long, String> userNameMap = cacheService.getUserNameMap(groupId);
            userNameMap.forEach((k,v)-> System.out.println("Key = "+k+" value = "+v.toString()));
        }
        //Need to replace the userId with userName in Message
        return activityList;
    }

    public List<Activity> getAllGroupActivitiesOfUser(Long userId) {
        List<Long> groupIds = null; //need to get this from User Microservice
        return activityRepository.findByGroupIdIn(groupIds);

    }
}
