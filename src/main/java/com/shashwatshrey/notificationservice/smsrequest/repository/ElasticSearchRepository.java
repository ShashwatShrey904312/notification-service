package com.shashwatshrey.notificationservice.smsrequest.repository;

import com.shashwatshrey.notificationservice.smsrequest.model.ElasticSearchSmsRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Date;

public interface ElasticSearchRepository extends ElasticsearchRepository<ElasticSearchSmsRequest, Long> {
    Page<ElasticSearchSmsRequest> findByPhoneNumberAndCreatedAtAfterAndCreatedAtBefore(String phoneNumber, Date startDate, Date endDate, Pageable pageable);
    Page<ElasticSearchSmsRequest> findByMessageContains(String text, Pageable pageable);
}
