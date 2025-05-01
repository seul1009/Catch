package com.catcher.catchApp.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CallHistoryResponse {
    private String id;
    private String date;
    private String phoneNumber;
    private int vishingPercent;
}
