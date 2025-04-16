package com.catcher.catchApp.dto;

public class KakaoLoginRequest {
    private String accessToken;

    public KakaoLoginRequest() {}

    public KakaoLoginRequest(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
