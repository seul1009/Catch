package com.catcher.catchApp.controller;

import com.catcher.catchApp.dto.CallHistoryResponse;
import com.catcher.catchApp.dto.MessageDTO;
import com.catcher.catchApp.service.CallHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/call-history")
@RequiredArgsConstructor
public class CallHistoryController {

    private final CallHistoryService callHistoryService;

    @GetMapping
    public List<CallHistoryResponse> getHistories() {
        return callHistoryService.getAllHistories();
    }

    @GetMapping("/{id}")
    public Map<String, List<MessageDTO>> getDetail(@PathVariable String id) {
        return Map.of("messages", callHistoryService.getMessages(id));
    }
}
