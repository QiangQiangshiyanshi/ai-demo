package com.ai_mcp.ai_demo.service;



import org.springframework.beans.factory.annotation.Value;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GrafanaService {
    @Value("${grafana.url}")
    private String grafanaBaseUrl;

    public String buildUrl(String promql) {
        String encoded = URLEncoder.encode(promql, StandardCharsets.UTF_8);
        return grafanaBaseUrl + "?var-promql=" + encoded;
    }
}