package object_orienters.techspot.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import object_orienters.techspot.security.model.Provider;
import object_orienters.techspot.security.model.UserOAuthTemp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleOAuth {
    private static String CLIENT_ID_GOOGLE;
    private static String CLIENT_SECRET_GOOGLE;
    private static String REDIRECT_URL_GOOGLE;

    private final RestTemplate restTemplate;

    public GoogleOAuth() {
        this.restTemplate = new RestTemplate();
    }

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    public void setClientIdGoogle(String clientIdGoogle) {
        CLIENT_ID_GOOGLE = clientIdGoogle;
    }

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    public void setClientSecretGoogle(String clientSecretGoogle) {
        CLIENT_SECRET_GOOGLE = clientSecretGoogle;
    }

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    public void setRedirectUrlGoogle(String redirectUrlGoogle) {
        REDIRECT_URL_GOOGLE = redirectUrlGoogle;
    }

    @Transactional
    public String exchangeCodeForAccessTokenGoogle(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(CLIENT_ID_GOOGLE, CLIENT_SECRET_GOOGLE);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", REDIRECT_URL_GOOGLE);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                request,
                String.class
        );

        String accessToken = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode root = objectMapper.readTree(accessToken);
            accessToken = root.path("access_token").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    System.out.println(accessToken);
        return accessToken;
    }

    @Transactional
    public UserOAuthTemp getUserInfoGoogle(String accessToken) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange("https://www.googleapis.com/oauth2/v2/userinfo", HttpMethod.GET, entity, String.class);

        String responseBody = responseEntity.getBody();

        ObjectMapper obtMapper = new ObjectMapper();
        String email, name, picture_url, id;

        try {
            JsonNode root = obtMapper.readTree(responseBody);
            email = root.path("email").asText();
            name = root.path("name").asText();
            picture_url = root.path("picture").asText();
            id = root.path("id").asText();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch user info from Google");
        }

        UserOAuthTemp userOAuthTemp = new UserOAuthTemp();
        userOAuthTemp.setEmail(email);
        userOAuthTemp.setName(name);
        userOAuthTemp.setProfilePicUrl(picture_url);
        userOAuthTemp.setId(id);
        userOAuthTemp.setProvider(Provider.GOOGLE);
        userOAuthTemp.setAccessToken(accessToken);

        return userOAuthTemp;
    }
}
