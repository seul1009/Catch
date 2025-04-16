package com.catcher.catchApp.service;

import com.catcher.catchApp.dto.*;
import com.catcher.catchApp.model.User;
import com.catcher.catchApp.model.LoginType;
import com.catcher.catchApp.repository.UserRepository;
import com.catcher.catchApp.security.JWTUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KakaoLoginService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;

    public AuthTokens login(KakaoLoginRequest request) {
        KakaoUserInfo userInfo = getKakaoUserInfo(request.getAccessToken());
        String username = findOrCreateUser(userInfo);
        String token = jwtUtil.generateToken(username);
        return new AuthTokens(token, userInfo.getNickname());
    }

    private KakaoUserInfo getKakaoUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                String.class
        );

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode json = objectMapper.readTree(response.getBody());

            String kakaoId = json.get("id").asText();
            String nickname = json.get("properties").get("nickname").asText();
            String email = json.get("kakao_account").get("email").asText();

            return new KakaoUserInfo(kakaoId, nickname, email);

        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보를 불러오지 못했습니다.", e);
        }
    }

    private String findOrCreateUser(KakaoUserInfo info) {
        Optional<User> optionalUser = userRepository.findByKakaoId(info.getKakaoId());

        if (optionalUser.isPresent()) {
            return optionalUser.get().getUsername();
        }

        User newUser = new User();
        newUser.setUsername("kakao_" + info.getKakaoId());
        newUser.setEmail(info.getEmail());
        newUser.setNickname(info.getNickname());
        newUser.setKakaoId(info.getKakaoId());
        newUser.setLoginType(LoginType.KAKAO);

        userRepository.save(newUser);
        return newUser.getUsername();
    }
}
