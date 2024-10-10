package com.splitwise.Activity_Service.clients;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.google.gson.Gson;
import com.splitwise.Activity_Service.model.ActivityMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SqsClientService {
    @Autowired
    private AmazonSQS sqsClient;

    @Value("${sqs.queue.url}")
    private String queueUrl;
    private static final Logger LOGGER = LoggerFactory.getLogger(SqsClientService.class);
    public SendMessageResult sendMessage(ActivityMessage activityMessage) {

        SendMessageResult sendMessageResponse = null;
        try {
            Gson gson = new Gson();
            String message = gson.toJson(activityMessage);
            SendMessageRequest sendMessageRequest = new SendMessageRequest()
                    .withQueueUrl(queueUrl)
                    .withMessageBody(message);
            sendMessageResponse = sqsClient.sendMessage(sendMessageRequest);

        } catch (AmazonClientException exception) {
            LOGGER.error("Exception occurred while sending message to SQS "+exception.getMessage());
        }
        return sendMessageResponse;
    }
}
