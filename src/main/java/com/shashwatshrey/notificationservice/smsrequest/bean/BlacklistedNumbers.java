package com.shashwatshrey.notificationservice.smsrequest.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;

@Data
@AllArgsConstructor
//@NoArgsConstructor
public class BlacklistedNumbers {

    private ArrayList<String> data;
    public BlacklistedNumbers()
    {
        data = new ArrayList<>();
    }
    public void addNumberInData(String number) {
        data.add(number);
    }

}
