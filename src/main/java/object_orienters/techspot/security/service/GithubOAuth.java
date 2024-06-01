package object_orienters.techspot.security.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import object_orienters.techspot.security.model.Provider;
import object_orienters.techspot.security.model.UserOAuthTemp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubOAuth {
    private static String CLIENT_ID_GIT;
    private static String CLIENT_SECRET_GIT;

    private final RestTemplate restTemplate;

    public GithubOAuth() {
        this.restTemplate = new RestTemplate();
    }


    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    public void setClientIdGit(String clientIdGit) {
        CLIENT_ID_GIT = clientIdGit;
    }

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    public void setClientSecretGit(String clientSecretGit) {
        CLIENT_SECRET_GIT = clientSecretGit;
    }


    @Transactional
    public String exchangeCodeForAccessTokenGithub(String code) {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("code", code);
        map.add("redirect_uri", "http://localhost:8080/login/oauth2/code/github");
        map.add("client_id", CLIENT_ID_GIT);
        map.add("client_secret", CLIENT_SECRET_GIT);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                request,
                String.class
        );

        String responseBody = response.getBody();

        String accessToken = null;
        try {
            for (String param : responseBody.split("&")) {
                String[] keyValue = param.split("=");
                if ("access_token".equals(keyValue[0])) {
                    accessToken = keyValue[1];
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }


    @Transactional
    public UserOAuthTemp getUserInfoGithub(String accessToken) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        requestHeaders.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(requestHeaders);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                entity,
                String.class
        );

        String rspBody = responseEntity.getBody();

        ObjectMapper obtMapper = new ObjectMapper();
        String email, name, picture_url, id;

        try {
            JsonNode root = obtMapper.readTree(rspBody);
            email = root.path("email").asText(); //fixme
            name = root.path("name").asText().equals("null") ? root.path("login").asText() : root.path("name").asText();
            picture_url = root.path("avatar_url").asText();
            id = root.path("id").asText();

        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to fetch user info from Github");
        }

        UserOAuthTemp userOAuthTemp = new UserOAuthTemp();
        userOAuthTemp.setEmail(email);
        userOAuthTemp.setName(name);
        userOAuthTemp.setProfilePicUrl(picture_url);
        userOAuthTemp.setId(id);
        userOAuthTemp.setProvider(Provider.GITHUB);
        userOAuthTemp.setAccessToken(accessToken);

        return userOAuthTemp;
    }
}
