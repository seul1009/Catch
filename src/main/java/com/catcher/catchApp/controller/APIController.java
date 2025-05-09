package com.catcher.catchApp.controller;

import com.catcher.catchApp.model.User;
import com.catcher.catchApp.repository.UserRepository;
import com.catcher.catchApp.security.JWTUtil;
import com.catcher.catchApp.service.CallHistoryService;
import com.catcher.catchApp.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;


@RestController
@RequestMapping("/api")
public class APIController {

    private final UserService userService;
    private final JWTUtil jwtUtil;

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    private final UserRepository userRepository;
    private final CallHistoryService callHistoryService;

    public APIController(UserService userService, JWTUtil jwtUtil, UserRepository userRepository, CallHistoryService callHistoryService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.callHistoryService = callHistoryService;
    }

    @GetMapping("/home")
    public ResponseEntity<String> getHome(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        System.out.println("token: "+ token);
        if (token != null && token.startsWith("Bearer ")) {
            try {
                String jwtToken = token.substring(7);  // "Bearer " 제거
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(jwtToken)
                        .getBody();

                String email = claims.getSubject();
                Optional<User> userOpt = userRepository.findByEmail(email);

                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    int phishingCount = user.getPhishingCount();
                    return ResponseEntity.ok(String.valueOf(phishingCount));  // 데이터를 반환
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저를 찾을 수 없습니다.");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization 헤더가 없거나, 형식이 잘못되었습니다.");
        }
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

