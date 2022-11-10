package com.pile.backend.common.bean;


import com.pile.backend.common.util.DateTimeUtil;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CommonBean {
    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public DateTimeUtil dateTimeUtil(){
        return new DateTimeUtil();
    }

}
