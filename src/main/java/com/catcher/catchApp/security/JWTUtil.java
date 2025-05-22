package com.catcher.catchApp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;  // JWT 서명 키
    private final long EXPIRATION_TIME = 86400000; // 24시간
//    private final long EXPIRATION_TIME = 10000; // 테스트

    // JWT 생성 (사용자명, 역할, 만료 시간)
    public String generateToken(String email, String roles) {
        return Jwts.builder()
                .setSubject(email)
                .claim("roles", roles)
                .setIssuedAt(new Date())  // 토큰 발급 시간
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))  // 만료 시간
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 서명 방식 설정
                .compact();
    }

    // JWT에서 클레임 추출
    public Claims extractClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (Exception e) {
            return null;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .getSubject(); // subject에 이메일 저장한 경우
    }

    // 토큰 만료 여부 확인
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration();

            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true; //  만료된 경우 명시적으로 true
        } catch (Exception e) {
            return true; //  기타 에러도 만료 처리
        }
    }

    // JWT에서 사용자 이름 추출
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // JWT에서 역할(roles) 추출
    public String extractRole(String token) {
        return extractClaims(token).get("roles", String.class);
    }

    // JWT 유효성 검증
    public boolean validateToken(String token, UserDetails userDetails) {
        return (userDetails.getUsername().equals(extractUsername(token)) && !isTokenExpired(token));
    }
}
