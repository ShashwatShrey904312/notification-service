package com.shashwatshrey.notificationservice.smsrequest.controller;

import com.shashwatshrey.notificationservice.smsrequest.model.*;
import com.shashwatshrey.notificationservice.smsrequest.repository.BlacklistRepository;
import com.shashwatshrey.notificationservice.smsrequest.service.RedisCaching;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Rest Controller for adding and deleting from Blacklist

@RestController
public class BlacklistController {

    private final Logger LOG = LoggerFactory.getLogger(BlacklistController.class);
    @Autowired
    private RedisCaching redisCaching;

    @Autowired
    private BlacklistRepository repository;



    /*POST method on "/v1/blacklist/"
    parameters:
    List<phoneNumber>
     */
    @PostMapping("/v1/blacklist")
    public ResponseEntity  addToBlacklist(@RequestBody BlacklistRequestBody blacklistRequestBody) {
        HttpStatus httpStatus;
        try{
            LOG.info("Adding Numbers into Blacklist MySQL DB");
            List<PhoneNumber> toBlackList = blacklistRequestBody.getPhoneNumbers();
            //TODO : convert for loop to stream
            for(PhoneNumber phone: toBlackList)
            {
                //Checking if it's already blacklisted
                Blacklist newPhoneBlacklist=new Blacklist();
                newPhoneBlacklist.setPhoneNumber(phone.getNumber());
                List<Blacklist> findByPhone = repository.findByPhoneNumberEquals(phone.getNumber());
                if(findByPhone.isEmpty())
                {

                    LOG.info("Adding number "+newPhoneBlacklist.getPhoneNumber() +" into Blacklist MySQL DB");
                    repository.save(newPhoneBlacklist);
                    LOG.info("Adding number "+newPhoneBlacklist.getPhoneNumber() +" into Redis");
                    redisCaching.addBlacklistRedis(newPhoneBlacklist, newPhoneBlacklist.getPhoneNumber());
                }
            }
            //Creating a Response to send
            httpStatus=HttpStatus.OK;
            BlacklistResponse blacklistResponse = new BlacklistResponse("Successly blacklisted");
            return ResponseEntity.status(httpStatus).body(blacklistResponse);
        }
        catch(Exception e){
            LOG.info("Exception while adding a number into the Blacklist DB MySQL and Redis Cache " +e);
            httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(httpStatus).body("Internal Server Error");
        }
    }
    /*DELETE method on "/v1/blacklist/"
    parameters:
    List<phoneNumber>
     */
    @DeleteMapping("/v1/blacklist")
    public ResponseEntity deleteFromBlacklist(@RequestBody BlacklistRequestBody blacklistRequestBody) {
        HttpStatus httpStatus;
        try{
            LOG.info("Deleting numbers from MySQL DB and Redis Cache");
            List<PhoneNumber> phoneNumbers = blacklistRequestBody.getPhoneNumbers();
            //TODO: convert for to stream
            for (PhoneNumber number : phoneNumbers)
            {
                List<Blacklist> findByPhone = repository.findByPhoneNumberEquals(number.getNumber());
                for(Blacklist toDelete: findByPhone)
                {

                    repository.deleteById(toDelete.getId());
                    LOG.info("Deleting number "+toDelete.getPhoneNumber() +" from Blacklist MySQL DB");
                    redisCaching.deleteBlacklistRedis(toDelete.getPhoneNumber());
                    LOG.info("Deleting number "+toDelete.getPhoneNumber() +" from Redis Blacklist Cache");
                }
            }
            httpStatus=HttpStatus.OK;
            BlacklistResponse blacklistResponse = new BlacklistResponse();
            blacklistResponse.setData("Successfully whitelisted");

            return ResponseEntity.status(httpStatus).body(blacklistResponse);
        }
        catch (Exception e)
        {
            System.out.println(e);
            httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(httpStatus).body("Internal Server Error");
        }
    }

    /*GET method on "/v1/blacklist/"
     */
    @GetMapping("/v1/blacklist")
    public ResponseEntity sendAllBlacklistedNumbers(){
        HttpStatus httpStatus;
        try {
            List<Blacklist> allBlacklisted = repository.findAll();
//            List<PhoneNumber>emptyList;
            BlacklistedNumbers blacklistedNumbers = new BlacklistedNumbers();
            for(Blacklist blacklist: allBlacklisted)
                blacklistedNumbers.addNumberInData(blacklist.getPhoneNumber());

            httpStatus= HttpStatus.OK;
            return ResponseEntity.status(httpStatus).body(blacklistedNumbers);
        }
        catch(Exception e) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            System.out.println(e);
            return ResponseEntity.status(httpStatus).body("Internal Server error");
        }
    }
}
