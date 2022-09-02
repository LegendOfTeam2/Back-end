package com.example.rhythme_backend.service.googleLogin;

import com.example.rhythme_backend.dto.requestDto.GoogleOAuthTokenDto;
import com.example.rhythme_backend.dto.requestDto.GoogleUserInfoDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
@Getter
public class GoogleOauth implements SocialOauth {

    @Value("${url}")
    private String GOOGLE_SNS_LOGIN_URL;

    @Value("${client-id}")
    private String GOOGLE_SNS_CLIENT_ID;

    @Value("${callback-url}")
    private String GOOGLE_SNS_CALLBACK_URL;

    @Value("${client-secret}")
    private String GOOGLE_SNS_CLIENT_SECRET;

    @Value("${scope}")
    private String GOOGLE_DATA_ACCESS_SCOPE;

    private final ObjectMapper objectMapper;
    @Override
    public String getOauthRedirectURL() {

        Map<String,Object> params=new HashMap<>();
        params.put("scope",GOOGLE_DATA_ACCESS_SCOPE);
        params.put("response_type","code");
        params.put("client_id",GOOGLE_SNS_CLIENT_ID);
        params.put("redirect_uri",GOOGLE_SNS_CALLBACK_URL);

        //parameter를 형식에 맞춰 구성해주는 함수
        String parameterString = params.entrySet().stream()
                .map(x->x.getKey()+"="+x.getValue())
                .collect(Collectors.joining("&"));
        String redirectURL = GOOGLE_SNS_LOGIN_URL+"?"+parameterString;
        System.out.println("redirectURL = " + redirectURL);

        return redirectURL;
    }


    public ResponseEntity<String> requestAccessToken(String code) {
        String GOOGLE_TOKEN_REQUEST_URL="https://oauth2.googleapis.com/token";
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> params = new HashMap<>();
        params.put("code", code);
        params.put("client_id", GOOGLE_SNS_CLIENT_ID);
        params.put("client_secret", GOOGLE_SNS_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_SNS_CALLBACK_URL);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity=restTemplate.postForEntity(GOOGLE_TOKEN_REQUEST_URL,
                params,String.class);

        if(responseEntity.getStatusCode()== HttpStatus.OK){
            return responseEntity;
        }
        return null;
    }


    public GoogleOAuthTokenDto getAccessToken(ResponseEntity<String> response) throws JsonProcessingException {
        System.out.println("response.getBody() = " + response.getBody());
        GoogleOAuthTokenDto googleOAuthToken = objectMapper.readValue(response.getBody(),GoogleOAuthTokenDto.class);
        return googleOAuthToken;
    }


    public GoogleUserInfoDto requestUserInfo(GoogleOAuthTokenDto oAuthToken) throws JsonProcessingException {
        String GOOGLE_USERINFO_REQUEST_URL="https://www.googleapis.com/oauth2/v1/userinfo";

        //header에 accessToken을 담는다.
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+oAuthToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");


        //HttpEntity를 하나 생성해 헤더를 담아서 restTemplate으로 구글과 통신하게 된다.
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(GOOGLE_USERINFO_REQUEST_URL, HttpMethod.GET,request,String.class);
        System.out.println("response.getBody() = " + response.getBody());


        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        String id = jsonNode.get("id").asText();
        String name = jsonNode.get("name").asText();
        String email = jsonNode.get("email").asText();


        System.out.println("구글 사용자 정보: " + id + ", " + name + ", " + email);
        return GoogleUserInfoDto.builder()
                .googleId(id)
                .email(email)
                .name(name)
                .build();
    }


    @Configuration
    public static class RestTemplateConfig {
        //HTTP get,post 요청을 날릴때 일정한 형식에 맞춰주는 template
        @Bean
        public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
            return restTemplateBuilder
                    .requestFactory(() -> new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()))
                    .additionalMessageConverters(new StringHttpMessageConverter(Charset.forName("UTF-8")))
                    .build();
        }
    }
}
