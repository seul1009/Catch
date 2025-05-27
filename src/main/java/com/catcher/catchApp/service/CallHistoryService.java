package com.catcher.catchApp.service;

import com.catcher.catchApp.dto.CallHistoryResponse;
import com.catcher.catchApp.dto.MessageDTO;
import com.catcher.catchApp.entity.CallHistory;
import com.catcher.catchApp.repository.CallHistoryRepository;
import com.catcher.catchApp.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CallHistoryService {

    private final CallHistoryRepository callHistoryRepository;

    public void saveCallHistory(CallHistory callHistory, CustomUserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        callHistory.setUserId(userEmail);
        callHistoryRepository.save(callHistory);
    }

    public List<CallHistoryResponse> getHistoriesByUserId(String userId) {
        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return callHistoryRepository.findAllByUserIdOrderByDateDesc(userId).stream()
                .map(h -> new CallHistoryResponse(
                        h.getId(),
                        h.getDate().format(formatter),
                        h.getPhoneNumber(),
                        h.getVishingPercent()
                ))
                .collect(Collectors.toList());
    }

    public List<MessageDTO> getMessages(String id, CustomUserDetails userDetails) {
        String userEmail = userDetails.getUsername();

        CallHistory history = callHistoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));

        if (!history.getUserId().equals(userEmail)) {
            throw new SecurityException("접근 권한이 없습니다");
        }

        return history.getMessages().stream()
                .map(m -> new MessageDTO(m.getSender(), m.getContent()))
                .collect(Collectors.toList());
    }

    public void saveFromFlask(CallHistory callHistory){
        if (callHistory.getDate() == null) {
            callHistory.setDate(LocalDateTime.now());
        }
        callHistoryRepository.save(callHistory);
    }
}