package com.catcher.catchApp.controller;

import com.catcher.catchApp.model.User;
import com.catcher.catchApp.repository.UserRepository;
import com.catcher.catchApp.service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api")
public class APIController {

    private final UserServiceImpl userServiceImpl;
    @Value("${JWT_SECRET_KEY}")
    private String secretKey;

    private final UserRepository userRepository;

    public APIController(UserRepository userRepository, UserServiceImpl userServiceImpl) {
        this.userRepository = userRepository;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping("/home")
    public ResponseEntity<String> getHome(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        System.out.println(token);
        if (token != null && token.startsWith("Bearer ")) {
            try {
                String jwtToken = token.substring(7);  // "Bearer " 제거
                Claims claims = Jwts.parser()
                        .setSigningKey(secretKey)
                        .parseClaimsJws(jwtToken)
                        .getBody();

                String username = claims.getSubject();
                Optional<User> userOpt = userRepository.findByUsername(username);

                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    int phishingCount = user.getPhishingCount();
                    System.out.println(phishingCount);
                    return ResponseEntity.ok(String.valueOf(phishingCount));  // 데이터를 반환
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
                }
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization header missing or invalid");
        }
    }

    @GetMapping("/info")
    public ResponseEntity<User> getInfo(Authentication authentication) {
        String username = authentication.getName();
        User user = userServiceImpl.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
}

