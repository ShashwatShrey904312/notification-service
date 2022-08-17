package com.shashwatshrey.notificationservice.smsrequest.service;

import com.shashwatshrey.notificationservice.smsrequest.model.ElasticSearchSmsRequest;
import com.shashwatshrey.notificationservice.smsrequest.repository.ElasticSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ElasticSearchService {

    private final ElasticSearchRepository elasticSearchRepository;

    @Autowired
    public ElasticSearchService(ElasticSearchRepository elasticSearchRepository) {
        this.elasticSearchRepository=elasticSearchRepository;

    }
    public void addSmsToElasticSearch(ElasticSearchSmsRequest elasticSearchSmsRequest){
        elasticSearchRepository.save(elasticSearchSmsRequest);
    }
    public Page<ElasticSearchSmsRequest> findBetweenDates(Date startDate, Date endDate, String phoneNumber, Pageable pageable) {

        Page<ElasticSearchSmsRequest> findBetweenDates =elasticSearchRepository.findByPhoneNumberAndCreatedAtAfterAndCreatedAtBefore(phoneNumber, startDate,endDate,pageable);
        return findBetweenDates;
    }

    public Page<ElasticSearchSmsRequest> findByText(String text, Pageable pageable) {

        Page<ElasticSearchSmsRequest> findByText =elasticSearchRepository.findByMessageContains(text,pageable);
        return findByText;
    }
}


