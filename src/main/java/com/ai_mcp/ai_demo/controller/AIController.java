package com.ai_mcp.ai_demo.controller;

import com.ai_mcp.ai_demo.model.ChatRequest;
import com.ai_mcp.ai_demo.model.MCPRequest;
import com.ai_mcp.ai_demo.service.ChatService;
import com.ai_mcp.ai_demo.service.MCPService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Slf4j
public class AIController {
    private final ChatService chatService;
    private final MCPService mcpService;

    public AIController(ChatService chatService, MCPService mcpService) {
        this.chatService = chatService;
        this.mcpService = mcpService;
    }

    @PostMapping("/chat")
    public ResponseEntity<?> chat(@Valid @RequestBody ChatRequest request) {
        try {
            Map<String, Object> response = chatService.chat(request.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Chat failed", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/mcp/query")
    public ResponseEntity<?> mcpQuery(@Valid @RequestBody MCPRequest request) {
        try {
            Map<String, Object> response = mcpService.query(request.getQuery());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("MCP query failed", e);
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP"));
    }
}

