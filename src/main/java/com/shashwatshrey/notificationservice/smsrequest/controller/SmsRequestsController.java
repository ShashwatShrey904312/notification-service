package com.shashwatshrey.notificationservice.smsrequest.controller;

import com.shashwatshrey.notificationservice.smsrequest.constants.AppConstants;
import com.shashwatshrey.notificationservice.smsrequest.model.SendSmsByIdSuccess;
import com.shashwatshrey.notificationservice.smsrequest.model.Sms;
import com.shashwatshrey.notificationservice.smsrequest.model.Sms_Requests;
import com.shashwatshrey.notificationservice.smsrequest.repository.SmsRequestsRepository;
import com.shashwatshrey.notificationservice.smsrequest.response.ErrorSmsFailure;
import com.shashwatshrey.notificationservice.smsrequest.response.SendSmsFailure;
import com.shashwatshrey.notificationservice.smsrequest.service.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
public class SmsRequestsController {
	private final Logger LOG = LoggerFactory.getLogger(SmsRequestsController.class);
	private final ElasticSearchService elasticSearchService;

	@Autowired
	public SmsRequestsController(ElasticSearchService elasticSearchService) {
		this.elasticSearchService=elasticSearchService;

	}

	@Autowired
	private KafkaTemplate<String,Long> kafkaTemplate;
	//TODO - create a handler
	@Autowired
	private SmsRequestsRepository repository;

	/*POST method on "v1/sms/send"

    Body:
    String phoneNumber,message
     */
	@PostMapping("/v1/sms/send")
	public ResponseEntity addSmsRequest(@RequestBody  Sms sms) {
		//TODO add @NotEmpty and Valid
		LOG.info("Adding a new SMS to MySQL DB");
		HttpStatus httpStatus;
//		elasticSearchService.addSmsToElasticSearch(new ElasticSearchSmsRequest(1,"Dummy","Dummy","Dummy",21,new Date(), new Date()));
		if (sms.getMessage().isEmpty() || sms.getPhoneNumber().isEmpty()) {
			LOG.info("Sending Bad Request Response");
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
				//TODO Builder for creating obj
				//Sms_Requests.builder().created_at(date).build()
				sms_requests.setCreated_at(date);
				sms_requests.setUpdated_at(date);
				sms_requests.setMessage(sms.getMessage());
				sms_requests.setPhone_number(sms.getPhoneNumber());
				sms_requests.setStatus("Added to SQL Database");
				repository.save(sms_requests);
				httpStatus = HttpStatus.OK;
				LOG.info("Saving the SMS in MySQL DataBase");

				//Kafka Producing on Topic : notification.send_sms
				kafkaTemplate.send(AppConstants.TOPIC_NAME,sms_requests.getId());
				LOG.info("The Kafka Produces produces on TOPIC notification.send_sms");

				return ResponseEntity.status(httpStatus).body(sms_requests);

			} catch (Exception e) {
				LOG.error(e.getMessage());
				LOG.info("Exception while adding SMS to MYSQL Database");
				httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
				return ResponseEntity.status(httpStatus).body("Internal Server Error");
			}


		}
	}

	/*GET method on "v1/sms/{}"

    path:
    int request_id
    String phoneNumber,message
     */
	@GetMapping("/v1/sms/{request_id}")
	public ResponseEntity sendSmsDetails(@PathVariable long request_id) {
		HttpStatus httpStatus;
		try{
			LOG.info("Fetching a SMS by its id");
			List<Sms_Requests> requestedSms = repository.findByIdEquals(request_id);
			if(requestedSms.isEmpty())
			{
				LOG.info("SMS id is invalid sending Bad Request Response");
				ErrorSmsFailure errorSmsFailure= new ErrorSmsFailure("INVALID_REQUEST","request_id not found");
				SendSmsFailure sendSmsFailure = new SendSmsFailure(errorSmsFailure);
				httpStatus=HttpStatus.BAD_REQUEST;
				return ResponseEntity.status(httpStatus).body(sendSmsFailure);
			}
			else
			{
				LOG.info("Fetching data from DB");
				SendSmsByIdSuccess sendSmsByIdSuccess = new SendSmsByIdSuccess();
				sendSmsByIdSuccess.setData(requestedSms.get(0));

				httpStatus=HttpStatus.OK;
				return ResponseEntity.status(httpStatus).body(sendSmsByIdSuccess);

			}
		}
		catch(Exception e) {
			LOG.error("Exception while sending SMS details by its id "+ e.getMessage());
			httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
			return ResponseEntity.status(httpStatus).body("Internal Server Error");
		}
	}



}
