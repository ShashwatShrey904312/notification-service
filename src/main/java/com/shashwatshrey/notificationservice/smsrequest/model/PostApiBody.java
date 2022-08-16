package com.shashwatshrey.notificationservice.smsrequest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostApiBody {
    private String deliveryChannel;
    private SmsDataPostApi sms;
    private DestinationDataPostApi destination;
}
