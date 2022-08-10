package com.shashwatshrey.notificationservice.smsrequest.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorSmsFailure {
	private String code;
	private String message;

}
