package com.shashwatshrey.notificationservice.smsrequest.controller;

import com.shashwatshrey.notificationservice.smsrequest.model.ElasticSearchSmsRequest;
import com.shashwatshrey.notificationservice.smsrequest.service.ElasticSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


@RestController
public class ElasticSearchController {
    private final ElasticSearchService  elasticSearchService;

    @Autowired
    public ElasticSearchController(ElasticSearchService elasticSearchService) {
        this.elasticSearchService=elasticSearchService;

    }


    //Getting All the messages between two Dates using Elastic Search

    @GetMapping("v1/smsrequests/phone")
//@RequestHeader("phone-number") String phoneNumber, @RequestHeader("startingDate") Date startingDate,@RequestHeader("endingDate") Date endingDate
    public ResponseEntity searchMsgBetweenDates(@RequestHeader("phoneNumber") String phoneNumber,
                                                @RequestHeader("startingDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startingDate,
                                                @RequestHeader("endingDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endingDate,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize
                                                ){
            Pageable pageable= PageRequest.of(pageNo,pageSize);
            Page<ElasticSearchSmsRequest> response = elasticSearchService.findBetweenDates(startingDate,endingDate,phoneNumber,pageable);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        @GetMapping("/v1/smsrequests/searchtext")
        public ResponseEntity searchMsgByText(@RequestHeader("text") String text,@RequestParam int pageNo,@RequestParam int pageSize) {

            Pageable pageable= PageRequest.of(pageNo,pageSize);
            Page<ElasticSearchSmsRequest> response = elasticSearchService.findByText(text,pageable);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

}
