package com.shashwatshrey.notificationservice.smsrequest.config.kafka;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceKafkaConsumer {
    private final Logger logger = LoggerFactory.getLogger(NotificationServiceKafkaConsumer.class);

    @KafkaListener(topics=AppConstants.TOPIC_NAME, groupId = AppConstants.GROUP_ID)

    public void consumeMessageId(long id) {

        System.out.println(id + "Consumer is running");
    }
}
