package com.shashwatshrey.notificationservice.smsrequest.controller;

import com.shashwatshrey.notificationservice.smsrequest.model.*;
import com.shashwatshrey.notificationservice.smsrequest.config.redis.RedisCaching;
import com.shashwatshrey.notificationservice.smsrequest.repository.BlacklistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BlacklistController {


    @Autowired
    private RedisCaching redisCaching;

    @Autowired
    private BlacklistRepository repository;


    @PostMapping("/v1/blacklist")
    public ResponseEntity  addToBlacklist(@RequestBody BlacklistRequestBody blacklistRequestBody) {
        HttpStatus httpStatus;
        try{
            List<PhoneNumber> toBlackList = blacklistRequestBody.getPhoneNumbers();
            for(PhoneNumber phone: toBlackList)
            {
                Blacklist newPhoneBlacklist=new Blacklist();
                newPhoneBlacklist.setPhoneNumber(phone.getNumber());
                System.out.println(phone.getNumber());
                List<Blacklist> findByPhone = repository.findByPhoneNumberEquals(phone.getNumber());
                if(findByPhone.isEmpty())
                {
                    repository.save(newPhoneBlacklist);
                    redisCaching.addBlacklistRedis(newPhoneBlacklist, newPhoneBlacklist.getPhoneNumber());
                }
            }
            httpStatus=HttpStatus.OK;
            BlacklistResponse blacklistResponse = new BlacklistResponse();
            blacklistResponse.setData("Successly blacklisted");
            return ResponseEntity.status(httpStatus).body(blacklistResponse);
        }
        catch(Exception e){
            System.out.println("Exception while adding a number into the Blacklist DB MySQL and Redis Cache " +e);
            httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
            return ResponseEntity.status(httpStatus).body("Internal Server Error");
        }
    }

    @DeleteMapping("/v1/blacklist")
    public ResponseEntity deleteFromBlacklist(@RequestBody BlacklistRequestBody blacklistRequestBody) {
        HttpStatus httpStatus;
        try{
            List<PhoneNumber> phoneNumbers = blacklistRequestBody.getPhoneNumbers();
            for (PhoneNumber number : phoneNumbers)
            {
                List<Blacklist> findByPhone = repository.findByPhoneNumberEquals(number.getNumber());
                for(Blacklist toDelete: findByPhone)
                {

                    repository.deleteById(toDelete.getId());
                    redisCaching.deleteBlacklistRedis(toDelete.getPhoneNumber());
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
