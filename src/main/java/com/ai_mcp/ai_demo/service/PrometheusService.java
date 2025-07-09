package com.ai_mcp.ai_demo.service;

/**
 * 调用 Prometheus 查询数据
 */

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class PrometheusService {
    @Value("${prometheus.url}")
    private String prometheusUrl;

    public String query(String promql) {
        RestTemplate restTemplate = new RestTemplate();
        String encoded = URLEncoder.encode(promql, StandardCharsets.UTF_8);
        String fullUrl = prometheusUrl + "?query=" + encoded;
        return restTemplate.getForObject(fullUrl, String.class);
    }
}
