package com.catcher.catchApp.service;

import com.catcher.catchApp.dto.LoginRequest;
import com.catcher.catchApp.dto.SignupRequest;
import com.catcher.catchApp.repository.UserRepository;
import com.catcher.catchApp.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.emailService = emailService;
    }


    @Override
    public boolean signup(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getEmail())) {
            return false;
        }

        User user = new User();
        user.setUsername(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        userRepository.save(user);
        return true;
    }

    @Override
    public User login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않는 이메일입니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return user;
    }

    //username으로 조회
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("해당 이용자를 찾을 수 없습니다: " + username));
    }

}
