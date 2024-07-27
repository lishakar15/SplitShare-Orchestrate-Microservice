package com.splitwise.Activity_Service.service;

import com.google.gson.Gson;
import com.splitwise.Activity_Service.entity.Activity;
import com.splitwise.Activity_Service.entity.ChangeLog;
import com.splitwise.Activity_Service.repository.ActivityRepository;
import com.splitwise.Activity_Service.repository.ChangeLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            getActivitiesWithUserName(activityList,userNameMap);
        }
        //Need to replace the userId with userName in Message
        return activityList;
    }

    public List<Activity> getAllGroupActivitiesOfUser(Long userId) {
        List<Long> groupIds = null; //need to get this from User Microservice
        return activityRepository.findByGroupIdIn(groupIds);

    }

    public void saveActivity(String activityMsg) {
        try
        {
            Gson gson = new Gson();
            Activity activity = gson.fromJson(activityMsg,Activity.class);
            setChangeLogsToActivity(activity);
            activityRepository.save(activity);
        }
        catch (Exception ex)
        {
            LOGGER.error("Error occurred while saving Activity "+ex);
        }
    }

    private void setChangeLogsToActivity(Activity activity) {

        if(activity != null)
        {
            List<ChangeLog> changeLogs = activity.getChangeLogs();
            if(changeLogs != null && !changeLogs.isEmpty())
            {
                for(ChangeLog changeLog : changeLogs)
                {
                    changeLog.setActivity(activity);
                }
            }
        }
    }
    public List<Activity> getActivitiesWithUserName(List<Activity> activities, Map<Long, String> userNameMap )
    {
        if(activities != null || userNameMap != null)
        {
            for(Activity activity : activities) {
                String message = activity.getMessage();
                if (message != null)
                {
                    Pattern pattern = Pattern.compile("\\{userId:(\\d+)\\}");//(\d+) matcher group for userId
                    Matcher matcher = pattern.matcher(message);
                    StringBuffer sb = new StringBuffer();
                    while (matcher.find()) {
                        Long userId = Long.parseLong(matcher.group(1));
                        //Todo: Need to check if the userid is equal to logged in user and show "you" instead of name
                        matcher.appendReplacement(sb, userNameMap.get(userId));
                    }
                    matcher.appendTail(sb);
                    System.out.println(sb.toString());
                    if(sb.length()>0)
                    {
                        activity.setMessage(sb.toString());
                    }
                }
            }
        }
        return activities;
    }

}
