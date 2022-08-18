package com.shashwatshrey.notificationservice.smsrequest.controller;

import com.shashwatshrey.notificationservice.smsrequest.model.ElasticSearchSmsRequest;
import com.shashwatshrey.notificationservice.smsrequest.service.ElasticSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

//Rest Controller to search msgs by text or between dates
@RestController
public class ElasticSearchController {
    private final Logger LOG = LoggerFactory.getLogger(ElasticSearchController.class);
    private final ElasticSearchService  elasticSearchService;

    @Autowired
    public ElasticSearchController(ElasticSearchService elasticSearchService) {
        this.elasticSearchService=elasticSearchService;

    }


    //Getting All the messages between two Dates using Elastic Search
    /*GET method on "v1/smsrequests/phone"
    parameters:
    int pageNo,pageSize
    Headers:
    String phoneNumber
    Date startingDate,endingDate (Format : YYYY:MM:DD)
     */
    @GetMapping("v1/smsrequests/phone")
   public ResponseEntity searchMsgBetweenDates(@RequestHeader("phoneNumber") String phoneNumber,
                                                @RequestHeader("startingDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startingDate,
                                                @RequestHeader("endingDate")@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endingDate,
                                                @RequestParam int pageNo,
                                                @RequestParam int pageSize
                                                ){
        LOG.info("GET request on v1/smsrequests/phone ");
            try{
                LOG.info("Fetching pages from Elastic Search searching by dates");
                Pageable pageable= PageRequest.of(pageNo,pageSize);
                Page<ElasticSearchSmsRequest> response = elasticSearchService.findBetweenDates(startingDate,endingDate,phoneNumber,pageable);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            catch (Exception e) {
                LOG.error("Exception in fetching page from Elastic Search having index smsrequests " + e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
            }
        }
    //Getting All the messages between two Dates using Elastic Search
    /*GET method on "v1/smsrequests/searchText"
    parameters:
    int pageNo,pageSize
    Headers:
    String text
     */
        @GetMapping("/v1/smsrequests/searchtext")
        public ResponseEntity searchMsgByText(@RequestHeader("text") String text,@RequestParam int pageNo,@RequestParam int pageSize) {

            try{

                LOG.info("Fetching pages from Elastic Search searching by text");
                Pageable pageable= PageRequest.of(pageNo,pageSize);
                Page<ElasticSearchSmsRequest> response = elasticSearchService.findByText(text,pageable);
                return ResponseEntity.status(HttpStatus.OK).body(response);
            }
            catch (Exception e) {

                LOG.error("Exception in fetching page from Elastic Search having index smsrequests " + e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
            }
        }

}
