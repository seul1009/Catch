package com.catcher.catchApp.controller;

import com.catcher.catchApp.model.User;
import com.catcher.catchApp.repository.CallHistoryRepository;
import com.catcher.catchApp.repository.UserRepository;
import com.catcher.catchApp.security.CustomUserDetails;
import com.catcher.catchApp.security.JWTUtil;
import com.catcher.catchApp.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class APIController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    private final UserRepository userRepository;
    private final CallHistoryRepository callHistoryRepository;

    public APIController(UserService userService, JWTUtil jwtUtil, UserRepository userRepository, CallHistoryRepository callHistoryRepository) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.callHistoryRepository = callHistoryRepository;
    }

    @GetMapping("/home")
    public ResponseEntity<String> getHome(@AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("인증 필요");
        }

        String email = userDetails.getUsername();
        User user = userRepository.findByEmail(email).orElseThrow();
        long phishingCount = callHistoryRepository.countByEmailAndVishingPercentGreaterThan(user.getEmail(), 60);

        return ResponseEntity.ok(String.valueOf(phishingCount));
    }

    @GetMapping("/report")
    public ResponseEntity<String> getReportInfo() {
        return ResponseEntity.ok("신고 관련 데이터입니다.");
    }

    @GetMapping("/info")
    public ResponseEntity<User> getMyInfo(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String email = jwtUtil.getEmailFromToken(token);

        User user = userService.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(user);
    }
}

