package com.catcher.catchApp.controller;

import com.catcher.catchApp.dto.LoginRequest;
import com.catcher.catchApp.dto.ResetPasswordRequest;
import com.catcher.catchApp.dto.SignupRequest;
import com.catcher.catchApp.repository.UserRepository;
import com.catcher.catchApp.security.CustomUserDetails;
import com.catcher.catchApp.security.JWTUtil;
import com.catcher.catchApp.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody @Valid SignupRequest request) {
        boolean isSignedUp = userService.signup(request);

        if (isSignedUp) {
            return ResponseEntity.ok().body("회원가입이 성공적으로 완료되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 가입된 이메일입니다.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String email = userDetails.getMember().getEmail();

            String roles = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(GrantedAuthority::getAuthority)
                    .orElse("ROLE_USER");

            String token = jwtUtil.generateToken(email, roles);
            System.out.println("토큰 생성됨: " + token);
            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 아이디 또는 비밀번호 오류");
        }
    }


    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        boolean success = userService.resetPassword(request.getEmail(), request.getNewPassword());

        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가입되지 않은 이메일입니다.");
        }

        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        String email = userDetails.getUsername();

        try {
            userService.deleteUserByEmail(email);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("탈퇴 처리 중 오류 발생: " + e.getMessage());
        }
    }


}
