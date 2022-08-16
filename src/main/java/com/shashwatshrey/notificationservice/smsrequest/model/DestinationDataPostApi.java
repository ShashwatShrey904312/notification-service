package com.shashwatshrey.notificationservice.smsrequest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationDataPostApi {
    private String msisdn;
    private String correlationId;
}
