package com.splitwise.Activity_Service.controller;

import com.splitwise.Activity_Service.model.InvitationRequest;
import com.splitwise.Activity_Service.service.InvitationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invite")
public class InvitationController {
    @Autowired
    InvitationService invitationService;
    @PostMapping()
    public ResponseEntity<String> sendInvitation(@RequestBody InvitationRequest invitationRequest){
        if(invitationRequest != null)
        {
            invitationService.sendInvitation(invitationRequest);
        }
        return  new ResponseEntity<>(HttpStatus.OK);
    }
}
