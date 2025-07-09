package com.ai_mcp.ai_demo.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MCPRequest {
    @NotBlank(message = "查询不能为空")
    private String query;
}