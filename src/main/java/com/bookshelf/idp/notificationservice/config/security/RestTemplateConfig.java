package com.bookshelf.idp.notificationservice.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // HttpComponentsClientHttpRequestFactory supportă PATCH
        // (default-ul Java HttpURLConnection NU suportă PATCH)
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory());
    }
}
