package com.shashwatshrey.notificationservice.smsrequest.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendSmsByIdSuccess {
    private Sms_Requests data;
}
