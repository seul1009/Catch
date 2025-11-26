package com.catcher.catchApp.controller;

import com.catcher.catchApp.dto.LoginRequest;
import com.catcher.catchApp.dto.ResetPasswordRequest;
import com.catcher.catchApp.dto.SignupRequest;
import com.catcher.catchApp.repository.UserRepository;
import com.catcher.catchApp.security.CustomUserDetails;
import com.catcher.catchApp.security.CustomUserDetailsService;
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
    private final CustomUserDetailsService customUserDetailsService;

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

            String accessToken = jwtUtil.generateToken(email, roles);
            String refreshToken = jwtUtil.generateRefreshToken(email);
            
            return ResponseEntity.ok(Map.of(
                    "accessToken", accessToken,
                    "refreshToken", refreshToken
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 실패: 아이디 또는 비밀번호 오류");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody Map<String, String> body) {

        String refreshToken = body.get("refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refresh token required");
        }

        // Refresh Token 만료 체크
        if (jwtUtil.isTokenExpired(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
        }

        // Refresh Token에서 email 추출
        String email = jwtUtil.extractUsername(refreshToken);

        CustomUserDetails userDetails =
                            (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);

        String roles = userDetails.getAuthorities().iterator().next().getAuthority();

        // 새 Access Token 발급
        String newAccessToken = jwtUtil.generateToken(email, roles);

        return ResponseEntity.ok(Map.of(
                "accessToken", newAccessToken
        ));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid ResetPasswordRequest request) {
        boolean success = userService.resetPassword(request.getEmail(), request.getNewPassword());

        if (!success) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("가입되지 않은 이메일입니다.");
        }

        return ResponseEntity.ok("비밀번호가 성공적으로 재설정되었습니다.");
    }

    @DeleteMapping("/withdraw")
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
