package com.ai_mcp.ai_demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

@Configuration
@ConfigurationProperties(prefix = "ai")
@Data
@Validated
public class AIConfig {
    @NotBlank
    private String url;
    @NotBlank
    private String key;
}
