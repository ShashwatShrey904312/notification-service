package com.shashwatshrey.notificationservice.smsrequest.service;

import com.shashwatshrey.notificationservice.smsrequest.model.Blacklist;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Service
public class RedisCaching {
    private HashOperations hashOperations;
    private RedisTemplate redisTemplate;


    public RedisCaching(RedisTemplate redisTemplate) {
        this.redisTemplate=redisTemplate;
        this.hashOperations=redisTemplate.opsForHash();
    }
    //TODO - MySQL and Redis Object seperate
    @CachePut(value="BLACKLIST",key="#phoneNumber")
    public void addBlacklistRedis(Blacklist blacklist, String phoneNumber) {
        System.out.println("The number is added to REDIS BL "+ phoneNumber);
        hashOperations.put("BLACKLIST",phoneNumber,blacklist);
    }
    @CacheEvict(value="BLACKLIST",key="#phoneNumber")
    public void deleteBlacklistRedis(String phoneNumber) {
        hashOperations.delete("BLACKLIST",phoneNumber);
    }
//    @Cacheable(value="BLACKLIST",key="#phoneNumber")
    public Blacklist findBlacklistedByNumber(String phoneNumber) {
        System.out.println("Cache is searched");
        Blacklist blacklist = (Blacklist) hashOperations.get("BLACKLIST",phoneNumber);


        return blacklist;
    }
}
