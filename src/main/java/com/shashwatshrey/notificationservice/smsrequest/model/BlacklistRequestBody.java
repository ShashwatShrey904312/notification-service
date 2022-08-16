package com.shashwatshrey.notificationservice.smsrequest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlacklistRequestBody {
    private List<PhoneNumber> phoneNumbers;

}
