package com.catcher.catchApp.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/auth/signup") || requestURI.startsWith("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);  // 토큰이 없으면 다음 필터로 전달
            return;
        }

        String token = authorization.split(" ")[1];

        // 토큰 만료 확인
        if (jwtUtil.isTokenExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);
            return;
        }

        // 유효한 loginId와 role 추출
        String username = jwtUtil.extractUsername(token);

//        // User 객체 생성
//        User user = new User();
//        user.setUsername(username);
//
//        // CustomUserDetails 객체 생성
//        CustomUserDetails customUserDetails = new CustomUserDetails(user);
//
//        // 인증 토큰 생성
//        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities() );

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // ✅ CustomUserDetails 객체로 인증 처리
        Authentication authToken =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        // SecurityContext에 인증 정보 설정
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 다음 필터로 요청과 응답을 전달
        filterChain.doFilter(request, response);
    }
}
