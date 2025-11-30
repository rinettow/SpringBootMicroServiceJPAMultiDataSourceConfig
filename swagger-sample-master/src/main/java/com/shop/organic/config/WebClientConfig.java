package com.shop.organic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    // You can inject WebClient.Builder and customize it
    public WebClient webClient(WebClient.Builder builder) {
        // You can set a base URL and default headers here
        return builder
                .baseUrl("http://www.smsintegra.com/api") // Base URL for the target microservice
                .build();
    }
}