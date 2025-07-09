package com.ai_mcp.ai_demo.service;

import com.ai_mcp.ai_demo.config.AIConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ChatService {
    private final RestTemplate restTemplate;
    private final AIConfig config;

    public ChatService(AIConfig config) {
        this.config = config;
        this.restTemplate = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(30))
            .setReadTimeout(Duration.ofSeconds(30))
            .build();
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Map<String, Object> chat(String message) {
        log.info("Sending chat request: {}", message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(config.getKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "4o-mini");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", "You are a helpful assistant."),
                Map.of("role", "user", "content", message)
        ));
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 800);
        requestBody.put("stream", false);  // 添加stream参数

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    config.getUrl() + "/chat/completions",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Received response from AI service");
                return response.getBody();
            } else {
                throw new RuntimeException("Invalid response from AI service");
            }
        } catch (Exception e) {
            log.error("Chat request failed", e);
            throw new RuntimeException("聊天请求失败: " + e.getMessage());
        }
    }
}
