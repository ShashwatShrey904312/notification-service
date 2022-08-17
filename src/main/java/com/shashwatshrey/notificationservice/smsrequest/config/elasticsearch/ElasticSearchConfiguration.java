package com.shashwatshrey.notificationservice.smsrequest.config.elasticsearch;

import com.shashwatshrey.notificationservice.smsrequest.constants.AppConstants;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@Configuration
@EnableElasticsearchRepositories(basePackages = "com.shashwatshrey.notificationservice.smsrequest.repository")
@ComponentScan(basePackages = {"com.shashwatshrey.notificationservice"})
public class ElasticSearchConfiguration extends AbstractElasticsearchConfiguration {
    @Bean
    @Override
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration config = ClientConfiguration.builder()
                .connectedTo(AppConstants.ElasticSearchUrl)
                .build();

        return RestClients.create(config).rest();
    }

}
