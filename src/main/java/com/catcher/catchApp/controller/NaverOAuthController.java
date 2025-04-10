package com.catcher.catchApp.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/naver")
public class NaverOAuthController {

    private static final String CLIENT_ID = System.getenv("NaverClientId");
    private static final String CLIENT_SECRET = System.getenv("NaverClientSecret");
    private static final String REDIRECT_URL = System.getenv("NaverRedirectUrl"); // 리디렉션 URI

    @GetMapping("/oauth/token")
    public ResponseEntity<?> getAccessToken(@RequestParam String code, @RequestParam String state) {
        // 네이버 액세스 토큰 요청 URL
        String tokenUrl = "https://nid.naver.com/oauth2.0/token";

        // 네이버에 전달할 요청 파라미터
        String body = "grant_type=authorization_code&client_id=" + CLIENT_ID
                + "&client_secret=" + CLIENT_SECRET + "&code=" + code + "&state=" + state;

        // HTTP 요청 준비
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>(body, headers);

        // 네이버로부터 액세스 토큰 받기
        ResponseEntity<String> response = restTemplate.exchange(tokenUrl, HttpMethod.GET, entity, String.class);

        // 응답에서 액세스 토큰 추출
        String accessToken = extractAccessToken(response.getBody());

        // 액세스 토큰을 리턴
        return ResponseEntity.ok(new NaverTokenResponse(accessToken));
    }

    // 응답에서 액세스 토큰을 추출하는 메서드
    private String extractAccessToken(String responseBody) {
        // 예시로 JSON에서 액세스 토큰을 추출 (실제 구현 시 JSON 파싱 라이브러리 사용 권장)
        String accessToken = responseBody.split("&")[0].split("=")[1];
        return accessToken;
    }

    // 액세스 토큰 응답을 위한 DTO
    static class NaverTokenResponse {
        private String access_token;

        public NaverTokenResponse(String access_token) {
            this.access_token = access_token;
        }

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }
    }

}
