package com.catcher.catchApp.dto;

public class KakaoUserInfo {
    private String kakaoId;
    private String nickname;
    private String email;

    public KakaoUserInfo(String kakaoId, String nickname, String email) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.email = email;
    }

    public String getKakaoId() {
        return kakaoId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmail() {
        return email;
    }
}
