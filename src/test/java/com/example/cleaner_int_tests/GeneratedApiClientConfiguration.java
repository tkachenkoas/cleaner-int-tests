package com.example.cleaner_int_tests;

import com.example.apiclient.invoker.ApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan("com.example.apiclient.api")
public class GeneratedApiClientConfiguration {

    @Bean
    public ApiClient apiClient(@Value("${server.port}") Integer serverPort) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        ApiClient apiClient = new ApiClient(restTemplate);
        apiClient.setBasePath("http://localhost:" + serverPort);
        return apiClient;
    }

}
