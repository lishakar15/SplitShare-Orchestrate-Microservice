package com.splitwise.Activity_Service.controller;

import com.splitwise.Activity_Service.entity.Activity;
import com.splitwise.Activity_Service.model.ActivityMessage;
import com.splitwise.Activity_Service.model.ActivityRequest;
import com.splitwise.Activity_Service.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    @Autowired
    ActivityService activityService;

    @GetMapping("/getGroupActivities/{groupId}")
    public ResponseEntity<List<Activity>> getGroupActivities(@PathVariable("groupId") Long groupId)
    {
        if(groupId == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        List<Activity> activities = activityService.getActivitiesByGroupId(groupId);
        return new ResponseEntity<>(activities,HttpStatus.OK);
    }
    @GetMapping("/getUserActivities/{userId}")
    public ResponseEntity<List<Activity>> getAllGroupActivitiesOfUser(@PathVariable("userId") Long userId) {
        if (userId == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Activity> activities = activityService.getAllGroupActivitiesOfUser(userId);
        return new ResponseEntity<>(activities,HttpStatus.OK);
    }
    @GetMapping("/getExpenseActivity/{expenseId}")
    public ResponseEntity<List<Activity>> getActivityByExpenseId(@PathVariable("expenseId") Long expenseId){
        if(expenseId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Activity> activities = activityService.getActivitiesByExpenseId(expenseId);
        return new ResponseEntity<>(activities,HttpStatus.OK);
    }
    @GetMapping("/getSettlementActivity/{settlementId}")
    public ResponseEntity<List<Activity>> getActivityBySettlementId(@PathVariable("settlementId") Long settlementId){
        if(settlementId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Activity> activities = activityService.getActivitiesBySettlementId(settlementId);
        return new ResponseEntity<>(activities,HttpStatus.OK);
    }
    @GetMapping("/get-most-active-group/{userId}")
    public ResponseEntity<String> getMostActiveGroup(@PathVariable("userId") Long userId)
    {
        if(userId == null)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String mostActiveGroup = activityService.getMostActiveGroupByUserId(userId);
        return new ResponseEntity<>(mostActiveGroup,HttpStatus.OK);
    }
    @PostMapping("/processActivityRequest")
    public ResponseEntity<?> processActivityRequest(@RequestBody ActivityRequest activityRequest) {
        if (activityRequest != null) {
            Activity activity = activityRequest.getActivity();
            //Save Activities to DB
            activityService.saveActivity(activity);
            //Process activity to send to SQS
            activityService.processEmailRequest(activityRequest);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/sendEmail")
    public void sendEmail(@RequestBody ActivityMessage activityMessage)
    {
        try{
            activityService.sendToSqs(activityMessage);
        }
        catch(Exception ex){
            System.out.println("Error occurred while sending to SQS");
        }
    }
}
