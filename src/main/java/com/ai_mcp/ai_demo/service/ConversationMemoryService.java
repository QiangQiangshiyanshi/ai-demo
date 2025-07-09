package com.ai_mcp.ai_demo.service;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConversationMemoryService {

    // sessionId -> 对话历史（OpenAI ChatCompletion 的格式）
    private final Map<String, List<Map<String, String>>> memory = new ConcurrentHashMap<>();

    public List<Map<String, String>> getConversation(String sessionId) {
        return memory.getOrDefault(sessionId, new ArrayList<>());
    }

    public void appendUserMessage(String sessionId, String userContent) {
        memory.computeIfAbsent(sessionId, k -> new ArrayList<>())
                .add(Map.of("role", "user", "content", userContent));
    }

    public void appendAssistantMessage(String sessionId, String assistantContent) {
        memory.computeIfAbsent(sessionId, k -> new ArrayList<>())
                .add(Map.of("role", "assistant", "content", assistantContent));
    }

    public void reset(String sessionId) {
        memory.remove(sessionId);
    }
}

