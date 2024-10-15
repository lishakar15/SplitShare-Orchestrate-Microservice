package com.splitwise.Activity_Service.service;

import com.google.gson.Gson;
import com.splitwise.Activity_Service.clients.SqsClientService;
import com.splitwise.Activity_Service.model.InvitationMessage;
import com.splitwise.Activity_Service.model.InvitationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class InvitationService {
    @Autowired
    SqsClientService sqsClientService;
    @Async
    public void sendInvitation(InvitationRequest invitationRequest) {
        if (invitationRequest != null) {
            InvitationMessage invitationMessage = InvitationMessage.builder()
                    .emailList(Arrays.asList(invitationRequest.getEmail()))
                    .subject("SplitShare - Invitation")
                    .body("Hi there,\n\n" +
                            "You have been invited to join the SplitShare app, a simple way to share expenses with friends and family.\n\n" +
                            "Please click the link below to register and start using SplitShare:\n" +
                            invitationRequest.getInvitationLink() + "\n\n" +
                            "Thanks for being a part of the journey! We hope you find the app helpful.\n\n" +
                            "Best regards,\n" +
                            "The SplitShare Team")
                    .build();

            Gson gson = new Gson();
            sqsClientService.sendMessage(gson.toJson(invitationMessage));
        }

    }
}
