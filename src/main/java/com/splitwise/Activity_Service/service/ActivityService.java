package com.splitwise.Activity_Service.service;

import com.splitwise.Activity_Service.entity.Activity;
import com.splitwise.Activity_Service.repository.ActivityRepository;
import com.splitwise.Activity_Service.repository.ChangeLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActivityService {
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    ChangeLogRepository changeLogRepository;


    public List<Activity> getActivityByGroupId(Long groupId) {

        return activityRepository.getActivityByGroupId(groupId);

    }

    public List<Activity> getAllGroupActivitiesOfUser(Long userId) {
        List<Long> groupIds = null; //need to get this from User Microservice
        return activityRepository.findByGroupIdIn(groupIds);

    }
}
