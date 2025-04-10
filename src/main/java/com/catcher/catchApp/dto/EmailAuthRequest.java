package com.catcher.catchApp.dto;

import lombok.Data;

@Data
public class EmailAuthRequest {
    private String email;
    private String confirmCode;
}
