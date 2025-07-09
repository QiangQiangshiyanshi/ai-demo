package com.ai_mcp.ai_demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

 @Configuration
@ConfigurationProperties(prefix = "mcp")
@Data
public class MCPConfig {
    private String url;
}
