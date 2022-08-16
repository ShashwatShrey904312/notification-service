package com.shashwatshrey.notificationservice.smsrequest.repository;

import com.shashwatshrey.notificationservice.smsrequest.model.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    List<Blacklist> findByPhoneNumberEquals(String phoneNumber);
    List<Blacklist> findAll();
}
