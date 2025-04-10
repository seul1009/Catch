package com.catcher.catchApp.controller;

import com.catcher.catchApp.dto.EmailAuthRequest;
import com.catcher.catchApp.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches()) {
            return ResponseEntity.badRequest().body("올바른 이메일 형식이 아닙니다.");
        }

        try {
            emailService.sendSimpleMessage(email);
            return ResponseEntity.ok("인증 코드가 전송되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("이메일 전송 실패: " + e.getMessage());
        }
    }

    @PostMapping("/code")
    public ResponseEntity<Boolean> AuthCode(@RequestBody @Valid EmailAuthRequest request) {
        boolean isValid = emailService.authCode(request.getEmail(), request.getConfirmCode());


        return ResponseEntity.ok(isValid);
    }
}
