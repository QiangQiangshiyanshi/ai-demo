package com.ai_mcp.ai_demo.service;


import com.ai_mcp.ai_demo.config.MCPConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import java.util.*;

@Service
@Slf4j
public class MCPService {
    private final RestTemplate restTemplate;
    private final MCPConfig config;

    public MCPService(MCPConfig config, RestTemplate restTemplate) {
        this.config = config;
        this.restTemplate = restTemplate;
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public Map<String, Object> query(String query) {
        log.info("Sending MCP query: {}", query);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("query", query);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    config.getUrl(),
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                log.info("Received response from MCP service");
                return response.getBody();
            } else {
                throw new RuntimeException("Invalid response from MCP service");
            }
        } catch (Exception e) {
            log.error("MCP query failed", e);
            throw new RuntimeException("MCP查询失败: " + e.getMessage());
        }
    }
}
