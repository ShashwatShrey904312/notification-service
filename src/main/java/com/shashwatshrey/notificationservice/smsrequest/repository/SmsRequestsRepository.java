package com.shashwatshrey.notificationservice.smsrequest.repository;

import com.shashwatshrey.notificationservice.smsrequest.model.Sms_Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SmsRequestsRepository extends JpaRepository <Sms_Requests, Long>{
    List<Sms_Requests> findByIdEquals(long id);
}
