package com.shashwatshrey.notificationservice.smsrequest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataSmsSuccess {

	private String requestId,comments;
}
