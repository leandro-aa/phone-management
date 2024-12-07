package com.challenge.phonemanagement.configuration;

import com.challenge.phonemanagement.configuration.properties.AbstractApiProperties;
import com.challenge.phonemanagement.interceptor.CustomClientHttpRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BeanConfiguration {

    @Bean
    public RestClient restClient(AbstractApiProperties apiProperties) {
        return RestClient.builder()
                .baseUrl(apiProperties.getBaseUrl())
                .requestInterceptor(new CustomClientHttpRequestInterceptor())
                .build();
    }
}
