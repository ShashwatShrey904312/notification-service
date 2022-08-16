package com.shashwatshrey.notificationservice.smsrequest.utils;

import com.shashwatshrey.notificationservice.smsrequest.constants.AppConstants;
import com.shashwatshrey.notificationservice.smsrequest.model.PostApiResponse;
import com.shashwatshrey.notificationservice.smsrequest.model.ThirdPartyApiPostBody.ChannelsPostApi;
import com.shashwatshrey.notificationservice.smsrequest.model.ThirdPartyApiPostBody.DestinationPostApi;
import com.shashwatshrey.notificationservice.smsrequest.model.ThirdPartyApiPostBody.PostApiBody;
import com.shashwatshrey.notificationservice.smsrequest.model.ThirdPartyApiPostBody.SmsPostApi;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public class ThirdPartyApiPost {
    public  PostApiResponse postToApi(long id,String phoneNumber, String message) {
        PostApiBody payload = new PostApiBody("sms",new ChannelsPostApi(new SmsPostApi(message)), new ArrayList<DestinationPostApi>(Arrays.asList(new DestinationPostApi(new ArrayList<String>(Arrays.asList(phoneNumber)),String.valueOf(id)))));

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("key",AppConstants.key);
        HttpEntity<Object> requestEntity = new HttpEntity<>(payload,headers);
        ResponseEntity<PostApiResponse> response = restTemplate.exchange(AppConstants.url, HttpMethod.POST, requestEntity, PostApiResponse.class);
        System.out.println(response.getBody().toString()+" 3rd Party API is running");
        return response.getBody();

    }
}
