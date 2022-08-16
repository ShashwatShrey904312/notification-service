package com.shashwatshrey.notificationservice.smsrequest.utils;

import com.shashwatshrey.notificationservice.smsrequest.constants.AppConstants;
import com.shashwatshrey.notificationservice.smsrequest.model.DestinationDataPostApi;
import com.shashwatshrey.notificationservice.smsrequest.model.PostApiBody;
import com.shashwatshrey.notificationservice.smsrequest.model.SmsDataPostApi;
import lombok.Data;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class ThirdPartyApiPost {
    public  Object postToApi(long id,String phoneNumber, String message) {
        PostApiBody payload = new PostApiBody("sms",
                new SmsDataPostApi(message),
                new DestinationDataPostApi(phoneNumber,String.valueOf(id)));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> requestEntity = new HttpEntity<>(payload,headers);
        Object response = restTemplate.exchange(AppConstants.url, HttpMethod.POST, requestEntity, Object.class);
        return response;

    }
}
