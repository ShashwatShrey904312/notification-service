package com.shashwatshrey.notificationservice.smsrequest.controller;

import com.shashwatshrey.notificationservice.smsrequest.model.SendSmsByIdSuccess;
import com.shashwatshrey.notificationservice.smsrequest.model.Sms;
import com.shashwatshrey.notificationservice.smsrequest.model.Sms_Requests;
import com.shashwatshrey.notificationservice.smsrequest.constants.AppConstants;
import com.shashwatshrey.notificationservice.smsrequest.repository.SmsRequestsRepository;
import com.shashwatshrey.notificationservice.smsrequest.response.ErrorSmsFailure;
import com.shashwatshrey.notificationservice.smsrequest.response.SendSmsFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
//import com.shashwatshrey.notificationservice.smsrequest.response.SendSmsFailure;

@RestController
public class SmsRequestsController {

	@Autowired
	private KafkaTemplate<String,Long> kafkaTemplate;
	@Autowired
	private SmsRequestsRepository repository;

	@PostMapping("/v1/sms/send")
	public ResponseEntity addSmsRequest(@RequestBody Sms sms) {
		HttpStatus httpStatus;
		if (sms.getMessage().isEmpty() || sms.getPhoneNumber().isEmpty()) {
			String code = "INVALID_REQUEST", message;

			if (sms.getMessage().isEmpty() && sms.getPhoneNumber().isEmpty())
				message = "phone_number and message is mandatory";
			else if (sms.getPhoneNumber().isEmpty())
				message = "phone_number is mandatory";
			else
				message = "message is mandatory";

			ErrorSmsFailure error = new ErrorSmsFailure(code, message);
			SendSmsFailure sendSmsFailure = new SendSmsFailure();
			sendSmsFailure.setError(error);
			httpStatus = HttpStatus.BAD_REQUEST;
			return ResponseEntity.status(httpStatus).body(sendSmsFailure);
		} else {
			try {
				Sms_Requests sms_requests = new Sms_Requests();
				Date date = new Date();
				sms_requests.setCreated_at(date);
				sms_requests.setUpdated_at(date);
				sms_requests.setMessage(sms.getMessage());
				sms_requests.setPhone_number(sms.getPhoneNumber());
				repository.save(sms_requests);
				httpStatus = HttpStatus.OK;
//				System.out.println(sms_requests.getMessage());

				//Kafka Producing

				kafkaTemplate.send(AppConstants.TOPIC_NAME,sms_requests.getId());

				return ResponseEntity.status(httpStatus).body(sms_requests);

			} catch (Exception e) {
				System.out.println(e);
				System.out.println("hehehhe");
				httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
				return ResponseEntity.status(httpStatus).body("Internal Server Error");
			}


		}
	}


	@GetMapping("/v1/sms/{request_id}")
	public ResponseEntity sendSmsDetails(@PathVariable long request_id) {
		HttpStatus httpStatus;
		try{
			List<Sms_Requests> requestedSms = repository.findByIdEquals(request_id);
			if(requestedSms.isEmpty())
			{
				ErrorSmsFailure errorSmsFailure= new ErrorSmsFailure("INVALID_REQUEST","request_id not found");
				SendSmsFailure sendSmsFailure = new SendSmsFailure(errorSmsFailure);
				httpStatus=HttpStatus.BAD_REQUEST;
				return ResponseEntity.status(httpStatus).body(sendSmsFailure);
			}
			else
			{
				SendSmsByIdSuccess sendSmsByIdSuccess = new SendSmsByIdSuccess();
				sendSmsByIdSuccess.setData(requestedSms.get(0));

				httpStatus=HttpStatus.OK;
				return ResponseEntity.status(httpStatus).body(sendSmsByIdSuccess);

			}
		}
		catch(Exception e) {
			httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body("Internal Server Error");
		}
	}



}
