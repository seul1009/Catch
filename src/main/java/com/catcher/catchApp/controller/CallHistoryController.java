package com.catcher.catchApp.controller;

import com.catcher.catchApp.dto.CallHistoryResponse;
import com.catcher.catchApp.dto.MessageDTO;
import com.catcher.catchApp.entity.CallHistory;
import com.catcher.catchApp.security.CustomUserDetails;
import com.catcher.catchApp.service.CallHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/call-history")
@RequiredArgsConstructor
public class CallHistoryController {

    private final CallHistoryService callHistoryService;

    @PostMapping
    public ResponseEntity<?> saveCallHistory(@RequestBody CallHistory callHistory, @AuthenticationPrincipal CustomUserDetails userDetails) {
        callHistoryService.saveCallHistory(callHistory, userDetails);
        return ResponseEntity.ok("저장 완료");
    }

    @GetMapping
    public List<CallHistoryResponse> getUserHistories(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();
        return callHistoryService.getHistoriesByEmail(email);
    }

    @GetMapping("/{id}")
    public Map<String, List<MessageDTO>> getDetail(@PathVariable("id") String id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return Map.of("messages", callHistoryService.getMessages(id, userDetails));
    }

    @PostMapping("/flask")
    public ResponseEntity<?> saveFromFlask(@RequestBody CallHistory callHistory) {
        callHistoryService.saveFromFlask(callHistory);
        return ResponseEntity.ok("저장 완료");
    }
}
