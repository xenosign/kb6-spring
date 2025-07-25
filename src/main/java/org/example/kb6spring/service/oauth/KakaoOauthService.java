package org.example.kb6spring.service.oauth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.kb6spring.domain.user.User;
import org.example.kb6spring.dto.oauth.KakaoUserInfoDto;
import org.example.kb6spring.repository.user.UserRepository;
import org.example.kb6spring.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOauthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${kakao.rest_key}")
    private String REST_API_KEY;

    @Value("${kakao.redirect_uri}")
    private String REDIRECT_URI;

//    private String REST_API_KEY = "2be90ab71a1f36d735f12cd91b53a982";
//    private String REDIRECT_URI = "http://localhost:8080/oauth/kakao/callback";

    public KakaoUserInfoDto processKakaoLogin(String code) {
        String accessToken = this.getAccessToken(code);
        KakaoUserInfoDto userInfo = this.getUserInfo(accessToken);

        User user = this.processKakaoUser(userInfo);

        List<String> roles = Arrays.asList(user.getRole());
        String jwtToken = jwtTokenProvider.createToken(user.getUsername(), roles);

        userInfo.setToken(jwtToken);

        return userInfo;
    }

    public String getAccessToken(String authorizationCode) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", REST_API_KEY);
        params.add("redirect_uri", REDIRECT_URI);
        params.add("code", authorizationCode);

        // 전체 요청 URL 확인용
        String fullUrl = tokenUrl + "?" +
                "grant_type=authorization_code" +
                "&client_id=" + REST_API_KEY +
                "&redirect_uri=" + REDIRECT_URI +
                "&code=" + authorizationCode;

        log.info("카카오 토큰 요청 전체 URL: {}", fullUrl);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                tokenUrl,
                request,
                String.class
        );

        log.info("카카오 토큰 요청에 대한 전체 응답: {}", response.getBody());

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            return root.get("access_token").asText();
        } catch (Exception e) {
            log.error("카카오 토큰 요청 실패", e);
            throw new RuntimeException("카카오 토큰 요청 실패");
        }
    }

    public KakaoUserInfoDto getUserInfo(String accessToken) {
        String userUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                userUrl,
                HttpMethod.POST,
                request,
                String.class
        );

        log.info("카카오 토큰 요청에 대한 전체 응답: {}", response.getBody());

        try {
            JsonNode root = objectMapper.readTree(response.getBody());
            Long kakaoId = root.get("id").asLong();

            JsonNode kakaoAccount = root.get("kakao_account");
//            String email = kakaoAccount.get("email").asText(null);
            JsonNode profile = kakaoAccount.get("profile");
            String nickname = profile.get("nickname").asText(null);
            String profileImageUrl = profile.get("profile_image_url").asText(null);

            return new KakaoUserInfoDto(kakaoId, null, nickname, profileImageUrl, null);
        } catch (Exception e) {
            log.error("카카오 사용자 정보 요청 실패", e);
            throw new RuntimeException("카카오 사용자 정보 요청 실패");
        }
    }

    public User processKakaoUser(KakaoUserInfoDto userInfo) {
        Optional<User> userOptional = userRepository.findByUsername(userInfo.getNickname());

        if (userOptional.isPresent()) {
            return userOptional.get();
        }

        User kkakaoUser = new User();
        kkakaoUser.setUsername(userInfo.getNickname());
        kkakaoUser.setPassword(null);
        kkakaoUser.setRole("ROLE_KAKAO");

        return userRepository.save(kkakaoUser);
    }
}