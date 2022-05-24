package de.neuefische.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class BackendApplication {

    @Value("${api.base.url}")
    private String apiBaseUrl;
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    public WebClient getWebClient() {
        return WebClient.create(apiBaseUrl);
    }
}
