package com.catcher.catchApp.service;

import com.catcher.catchApp.dto.LoginRequest;
import com.catcher.catchApp.dto.SignupRequest;
import com.catcher.catchApp.model.User;

public interface UserService {

    // 로그인 처리
    User login(LoginRequest loginRequest);

    // 회원가입 처리
    boolean signup(SignupRequest signupRequest);


    User getUserByUsername(String username);

}

