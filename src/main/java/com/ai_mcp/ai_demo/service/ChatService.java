package com.ai_mcp.ai_demo.service;



import com.ai_mcp.ai_demo.config.AIConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
@Slf4j
public class ChatService {
    private final RestTemplate restTemplate;
    @Autowired
    private final AIConfig config;

    public ChatService(AIConfig config) {
        this.config = config;
        this.restTemplate = new RestTemplate();
    }

    @Retryable(maxAttempts = 3)
    public Map<String, Object> chat(String message) {
        log.info("Sending chat request: {}", message);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(config.getKey());

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "4o-mini");
        requestBody.put("messages", List.of(
                Map.of("role", "user", "content", message)
        ));
        requestBody.put("temperature", 0.7);
        requestBody.put("max_tokens", 800);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    config.getUrl() + "/v1/chat/completions",
                    HttpMethod.POST,
                    entity,
                    Map.class
            );
            log.info("Received response from AI service");
            return response.getBody();
        } catch (Exception e) {
            log.error("Chat request failed", e);
            throw new RuntimeException("聊天请求失败: " + e.getMessage());
        }
    }
}
