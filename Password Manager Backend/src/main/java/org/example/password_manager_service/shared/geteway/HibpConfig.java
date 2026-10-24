package org.example.password_manager_service.shared.geteway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class HibpConfig {
    @Bean
    public WebClient webClient(@Value("${hibp.api.url}") String api) {
        return WebClient.builder()
                .baseUrl(api)
                .defaultHeader("User-Agent", "PasswordManager/1.0")
                .build();
    }
}
