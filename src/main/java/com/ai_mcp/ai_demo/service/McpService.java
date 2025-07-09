package com.ai_mcp.ai_demo.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class McpService {

    @Value("${mcp.url}")
    private String mcpUrl;

    public String ask(String question) {
        //RestTemplate 是 Spring 框架提供的一个 HTTP 客户端工具类，用于在 Java 代码中方便地发起 HTTP 请求（比如 GET、POST、PUT、DELETE 等），并处理响应结果。
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = Map.of("text", question);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        Map<String, Object> response = restTemplate.postForObject(mcpUrl + "/execute_query", request, Map.class);
        return response.get("validated_query").toString();
    }
}
