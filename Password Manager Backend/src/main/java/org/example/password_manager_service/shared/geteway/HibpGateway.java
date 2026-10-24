package org.example.password_manager_service.shared.geteway;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

@Component
public class HibpGateway {
    // Сделать thread
    private final WebClient webClient;
    public HibpGateway(WebClient webClient){
        this.webClient = webClient;
    }
    public List<String> pwnedApiClient(String prefix) {
        String response =  webClient.get()
                .uri("/range/{hash_pass}", prefix)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (response == null || response.isBlank()) {
            return List.of();
        }

        return Arrays.stream(response.split("\r?\n"))
                .map(line -> line.split(":")[0]).toList();
    }
}