package com.catcher.catchApp.dto;

public class AuthTokens {
    private String token;
    private String nickname;

    public AuthTokens(String token, String nickname) {
        this.token = token;
        this.nickname = nickname;
    }

    public String getToken() {
        return token;
    }

    public String getNickname() {
        return nickname;
    }
}
