package com.catcher.catchApp.service;

import com.catcher.catchApp.dto.LoginRequest;
import com.catcher.catchApp.dto.SignupRequest;
import com.catcher.catchApp.model.User;

public interface UserService {

    User login(LoginRequest loginRequest);

    boolean signup(SignupRequest signupRequest);

    User getUserByEmail(String email);

    String findOrCreateKakaoUser(String kakaoId, String nickname, String email);
}

