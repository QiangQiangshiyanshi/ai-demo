package com.ai_mcp.ai_demo.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ChatRequest {
    @NotBlank(message = "消息不能为空")
    private String message;
}