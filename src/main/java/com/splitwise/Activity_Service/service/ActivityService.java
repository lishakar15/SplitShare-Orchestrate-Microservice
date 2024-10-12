package com.splitwise.Activity_Service.service;

import com.amazonaws.services.sqs.model.SendMessageResult;
import com.splitwise.Activity_Service.clients.UserClient;
import com.splitwise.Activity_Service.clients.SqsClientService;
import com.splitwise.Activity_Service.constants.StringConstants;
import com.splitwise.Activity_Service.entity.Activity;
import com.splitwise.Activity_Service.entity.ChangeLog;
import com.splitwise.Activity_Service.enums.ActivityType;
import com.splitwise.Activity_Service.model.ActivityMessage;
import com.splitwise.Activity_Service.model.ActivityRequest;
import com.splitwise.Activity_Service.repository.ActivityRepository;
import com.splitwise.Activity_Service.repository.ChangeLogRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ActivityService {
    @Autowired
    ActivityRepository activityRepository;
    @Autowired
    ChangeLogRepository changeLogRepository;
    @Autowired
    UserClient userClient;
    @Autowired
    SqsClientService sqsClientService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityService.class);


    public List<Activity> getActivitiesByGroupId(Long groupId) {
        List<Activity> activityList = activityRepository.findByGroupId(groupId);
        return activityList;
    }
    public List<Activity> getActivitiesByExpenseId(Long expenseId) {
        List<Activity> activityList = activityRepository.findByExpenseId(expenseId);
        return activityList;
    }

    public List<Activity> getActivitiesBySettlementId(Long settlementId){
        List<Activity> activityList = activityRepository.findBySettlementId(settlementId);
        return activityList;
    }

    public List<Activity> getAllGroupActivitiesOfUser(Long userId) {
        List<Activity> activityList = new ArrayList<>();
        Map<Long,String> groupMap = userClient.getGroupNameMap(userId);
        if(groupMap != null || !groupMap.isEmpty()){
            List<Long> groupIds = new ArrayList<>(groupMap.keySet());
            activityList = activityRepository.findByGroupIdIn(groupIds);
        }
        return activityList;
    }

    public void saveActivity(Activity activity) {
        try {
            setChangeLogsToActivity(activity);
            activityRepository.save(activity);
        } catch (Exception ex) {
            LOGGER.error("Error occurred while saving Activity " + ex);
        }
    }

    private void setChangeLogsToActivity(Activity activity) {

        if (activity != null) {
            List<ChangeLog> changeLogs = activity.getChangeLogs();
            if (changeLogs != null && !changeLogs.isEmpty()) {
                for (ChangeLog changeLog : changeLogs) {
                    changeLog.setActivity(activity);
                }
            }
        }
    }

//    public List<Activity> getActivitiesWithUserName(List<Activity> activities, Map<Long, String> userNameMap) {
//        if (activities != null || userNameMap != null) {
//            Long loggedInUser = 0L;
//            for (Activity activity : activities) {
//                String message = activity.getMessage();
//                if (message != null) {
//                    Pattern pattern = Pattern.compile("\\{userId:(\\d+)\\}");//(\d+) matcher group for userId
//                    Matcher matcher = pattern.matcher(message);
//                    StringBuffer sb = new StringBuffer();
//                    while (matcher.find()) {
//                        Long userId = Long.parseLong(matcher.group(1));
//                        //Todo: Need to check if the userid is equal to logged in user and show "you" instead of name
//                        String userName = userNameMap.get(userId);
//                        if (userName == null || userName.length() == 0) {
//                            userName = StringConstants.UNKNOWN_USER;
//                        } else if (userId.equals(loggedInUser)) {
//                            userName = StringConstants.YOU;
//                        }
//                        matcher.appendReplacement(sb, userName);
//                    }
//                    matcher.appendTail(sb);
//                    System.out.println(sb.toString());
//                    if (sb.length() > 0) {
//                        activity.setMessage(sb.toString());
//                    }
//                }
//            }
//        }
//        return activities;
//    }

    @Async
    public void processEmailRequest(ActivityRequest activityRequest) {

        if (activityRequest != null) {
            List<Long> userIds = activityRequest.getUserIdList();
            Activity activity = activityRequest.getActivity();
            try {
                //Get email ids for the userIds
                Map<Long, String> userEmailMap = userClient.getUserEmailMapByUserIds(userIds);
                if (userEmailMap != null && !userEmailMap.isEmpty()) {
                    List<String> usersEmailList = new ArrayList<>(userEmailMap.values());
                    ActivityMessage activityMessage = ActivityMessage.builder()
                            .emailList(usersEmailList)
                            .subject(getSubjectString(activity.getActivityType()))
                            .body(activity.getMessage())
                            .build();
                    //Send message to SQS
                    SendMessageResult sendMessageResult = sqsClientService.sendMessage(activityMessage);
                    if (sendMessageResult != null) {
                        LOGGER.info("Message sent Successfully ");
                    } else {
                        LOGGER.error("Error occurred while sending message to SQS ");
                    }
                }
            } catch (Exception ex) {
                LOGGER.error("Error occurred while processing send email request " + ex);
            }
        }
    }

    public String getSubjectString(ActivityType activityType) {

        String subjectStr = StringConstants.APP_NAME;
        switch (activityType) {
            case EXPENSE_CREATED: {
                subjectStr = subjectStr + "  - New Expense Created";
                break;
            }
            case EXPENSE_UPDATED: {
                subjectStr = subjectStr + "  - Expense Updated";
                break;
            }
            case EXPENSE_DELETED: {
                subjectStr = subjectStr + "  - Expense Deleted";
                break;
            }
            case PAYMENT_CREATED: {
                subjectStr = subjectStr + " - New Payment Created";
                break;
            }
            case PAYMENT_UPDATED: {
                subjectStr = subjectStr + " - Payment Updated";
                break;
            }
            case PAYMENT_DELETED: {
                subjectStr = subjectStr + " - Payment Deleted";
                break;
            }
            case COMMENT_ADDED: {
                subjectStr = subjectStr + " - New Comment added ";
                break;
            }
            case COMMENT_DELETED: {
                subjectStr = subjectStr + " - Comment Deleted";
                break;
            }
            case USER_ADDED: {
                subjectStr = subjectStr + " - New user joined the group";
                break;
            }
            case USER_REMOVED: {
                subjectStr = subjectStr + " - User Removed";
                break;
            }
            default: {
                //Do nothing
            }
        }
        return subjectStr;
    }

    public void sendToSqs(ActivityMessage activityMessage) {
        SendMessageResult sendMessageResult = sqsClientService.sendMessage(activityMessage);
        if (sendMessageResult != null) {
            LOGGER.info("Message sent Successfully ");
        } else {
            LOGGER.error("Error occurred while sending message to SQS ");
        }
    }
}

