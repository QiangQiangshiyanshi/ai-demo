package com.ai_mcp.ai_demo.controller;

import com.ai_mcp.ai_demo.service.ConversationMemoryService;
import com.ai_mcp.ai_demo.service.McpService;
import com.ai_mcp.ai_demo.service.OpenAiService;
import com.ai_mcp.ai_demo.service.PrometheusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 接收自然语言请求
 */
@RestController
@RequestMapping("/api")
public class NlqController {

    @Autowired
    private McpService mcpService;

    @Autowired
    private PrometheusService prometheusService;
    
    @Autowired
    private ConversationMemoryService conversationMemoryService;

    @Autowired
    private OpenAiService openAiService;


    @PostMapping("/nlq")
    public ResponseEntity<String> handleQuery(@RequestBody Map<String, String> payload) {
        String question = payload.get("question");
        String sessionID = payload.get("sessionID");
        if (sessionID==null || sessionID.isEmpty()){
            return ResponseEntity.badRequest().body("error sessionID");
        }
        conversationMemoryService.appendUserMessage(sessionID,question);
        String promql = mcpService.ask(question);
        String result = prometheusService.query(promql);

        String summary  = "问题：" + question + "\nPromQL：" + promql + "\n查询结果：" + result;
        conversationMemoryService.appendAssistantMessage(sessionID,summary);
        List<Map<String, String>> fullContext  = conversationMemoryService.getConversation(sessionID);
        if (fullContext.stream().noneMatch(m -> "system".equals(m.get("role")))) {
            fullContext.add(0, Map.of("role", "system", "content", "你是一个智能监控助手，能理解 PromQL 和查询结果，回答清晰自然。"));
        }
        String reply = openAiService.chat(fullContext);
        return ResponseEntity.ok(reply);
    }
}
