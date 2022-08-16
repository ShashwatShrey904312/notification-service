package com.shashwatshrey.notificationservice.smsrequest.config.kafka;


import com.shashwatshrey.notificationservice.smsrequest.config.redis.RedisCaching;
import com.shashwatshrey.notificationservice.smsrequest.constants.AppConstants;
import com.shashwatshrey.notificationservice.smsrequest.model.Blacklist;
import com.shashwatshrey.notificationservice.smsrequest.model.Sms_Requests;
import com.shashwatshrey.notificationservice.smsrequest.repository.SmsRequestsRepository;
import com.shashwatshrey.notificationservice.smsrequest.utils.ThirdPartyApiPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceKafkaConsumer {
    private final Logger logger = LoggerFactory.getLogger(NotificationServiceKafkaConsumer.class);
    @Autowired
    private SmsRequestsRepository repository;

    @Autowired
    private  RedisCaching redisCaching;




    @KafkaListener(topics= AppConstants.TOPIC_NAME, groupId = AppConstants.GROUP_ID)
    public void consumeMessageId(long id) {

        System.out.println(id + "Consumer is running");

        try {
            List <Sms_Requests> requestedSms = repository.findByIdEquals(id);

            if(requestedSms.isEmpty())
            {
                //error
                //the details are not found in the DataBase
                //throw Exception("Kafka Consumer SMS Id data not found in MySQL DB");
                System.out.println("Kafka Consumer could not find sms details from MySQl");
            }
            else
            {

                Blacklist blacklist = redisCaching.findBlacklistedByNumber(requestedSms.get(0).getPhone_number());
                Sms_Requests sms_requests = requestedSms.get(0);
                if(blacklist==null)
                {
                    //Not in Redis Blacklist so move Ahead
                    System.out.println("The Number is not blacklisted "+ requestedSms.get(0).getPhone_number());
                    ThirdPartyApiPost thirdPartyApiPost = new ThirdPartyApiPost();
                    Object response =  thirdPartyApiPost.postToApi(sms_requests.getId(), sms_requests.getPhone_number(),sms_requests.getMessage());


                }
                else
                    System.out.println("The Number is  Blacklisted "+ requestedSms.get(0).getPhone_number());

            }
            System.out.println("Consumer is running "+requestedSms.get(0).getMessage());
        }
        catch(Exception e) {
            System.out.println(e);
        }


    }
}
