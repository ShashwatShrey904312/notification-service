package com.shashwatshrey.notificationservice.smsrequest.model.ThirdPartyApiPostBody;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DestinationPostApi {
    public ArrayList<String> msisdn;
    public String correlationId;
}
