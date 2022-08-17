package com.shashwatshrey.notificationservice.smsrequest.config.kafka;


import com.shashwatshrey.notificationservice.smsrequest.config.redis.RedisCaching;
import com.shashwatshrey.notificationservice.smsrequest.constants.AppConstants;
import com.shashwatshrey.notificationservice.smsrequest.model.Blacklist;
import com.shashwatshrey.notificationservice.smsrequest.model.ElasticSearchSmsRequest;
import com.shashwatshrey.notificationservice.smsrequest.model.PostApiResponse;
import com.shashwatshrey.notificationservice.smsrequest.model.Sms_Requests;
import com.shashwatshrey.notificationservice.smsrequest.repository.SmsRequestsRepository;
import com.shashwatshrey.notificationservice.smsrequest.service.ElasticSearchService;
import com.shashwatshrey.notificationservice.smsrequest.utils.ThirdPartyApiPost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationServiceKafkaConsumer {
    private final Logger LOG = LoggerFactory.getLogger(NotificationServiceKafkaConsumer.class);
    @Autowired
    private SmsRequestsRepository repository;

    @Autowired
    private  RedisCaching redisCaching;

    private final ElasticSearchService elasticSearchService;

    @Autowired
    public NotificationServiceKafkaConsumer(ElasticSearchService elasticSearchService) {
        this.elasticSearchService=elasticSearchService;

    }

    //Kafka Consumer listening to TOPIC : notification.send_sms
    @KafkaListener(topics= AppConstants.TOPIC_NAME, groupId = AppConstants.GROUP_ID)
    public void consumeMessageId(long id) {

        LOG.info("Consumer is running");

        try {

            //Searching in SQL DB for SmsRequest by it's Id
            List <Sms_Requests> requestedSms = repository.findByIdEquals(id);
            if(requestedSms.isEmpty())
            {
                //the details are not found in the DataBase
                LOG.info("Consumer not found  in MySQL DB");
                throw new Exception("DataBase Error: Consumer could not find SmsRequest in MySQL DB");
            }
            else
            {

                Blacklist blacklist = redisCaching.findBlacklistedByNumber(requestedSms.get(0).getPhone_number());
                Sms_Requests sms_requests = requestedSms.get(0);
                if(blacklist==null)
                {
                    //Number is not blacklisted
                    LOG.info("The Number is not blacklisted so sending POST request to ImiConnect");

                    ThirdPartyApiPost thirdPartyApiPost = new ThirdPartyApiPost();
                    PostApiResponse response =  thirdPartyApiPost.postToApi(sms_requests.getId(), sms_requests.getPhone_number(),sms_requests.getMessage());

                    //Adding this SMS to the ELastic Search
                    elasticSearchService.addSmsToElasticSearch(
                            new ElasticSearchSmsRequest(id,sms_requests.getPhone_number(),sms_requests.getMessage(),null,200,sms_requests.getCreated_at(), sms_requests.getUpdated_at())
                    );
                    LOG.info("Status Code is "+ response.getResponse().get(0).getDescription());

                }
                else
                    LOG.info("The Number "+ requestedSms.get(0).getPhone_number()+" is  Blacklisted ");

            }
            System.out.println("Consumer is running "+requestedSms.get(0).getMessage());
        }
        catch(Exception e) {
            Sms_Requests sms_requests = repository.findByIdEquals(id).get(0);
            if(sms_requests!=null) {
                sms_requests.setFailure_code(500);
                sms_requests.setFailure_comments(e.getMessage());
                sms_requests.setUpdated_at(new Date());
                repository.save(sms_requests);
            }
            LOG.info("Exception while consuming sms of id "+id+e.getMessage());
            LOG.error(e.getStackTrace().toString());
        }


    }
}
