package com.catcher.catchApp.service;

import com.catcher.catchApp.dto.CallHistoryResponse;
import com.catcher.catchApp.dto.MessageDTO;
import com.catcher.catchApp.entity.CallHistory;
import com.catcher.catchApp.repository.CallHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CallHistoryService {

    private final CallHistoryRepository callHistoryRepository;

    public List<CallHistoryResponse> getAllHistories() {
        return callHistoryRepository.findAll().stream()
                .map(h -> new CallHistoryResponse(
                        h.getId(), h.getDate(), h.getPhoneNumber(), h.getVishingPercent()
                ))
                .collect(Collectors.toList());
    }

    public List<MessageDTO> getMessages(String id) {
        CallHistory history = callHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));

        return history.getMessages().stream()
                .map(m -> new MessageDTO(m.getSender(), m.getContent()))
                .collect(Collectors.toList());
    }
}