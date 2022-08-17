package com.shashwatshrey.notificationservice.smsrequest.repository;

import com.shashwatshrey.notificationservice.smsrequest.model.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    List<Blacklist> findByPhoneNumberEquals(String phoneNumber);
    List<Blacklist> findAll();
}
