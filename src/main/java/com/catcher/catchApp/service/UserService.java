package com.catcher.catchApp.service;

import com.catcher.catchApp.dto.LoginRequest;
import com.catcher.catchApp.dto.SignupRequest;
import com.catcher.catchApp.model.User;

import java.util.Optional;

public interface UserService {

    User login(LoginRequest loginRequest);

    boolean signup(SignupRequest signupRequest);

    User getUserByEmail(String email);

    Optional<User> findByEmail(String email);

    void deleteUserByEmail(String email);

    boolean resetPassword(String email, String newPassword);
}

