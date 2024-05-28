package object_orienters.techspot.security.service;

import object_orienters.techspot.security.model.OAuthDto;
import object_orienters.techspot.security.model.User;
import object_orienters.techspot.security.model.UserOAuthTemp;
import object_orienters.techspot.security.payload.request.SignupRequest;
import object_orienters.techspot.security.repository.UserOAuthTempRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;

@Service
public class OAuth2Services {

    @Autowired
    private GeneralAuthServices generalAuthServices;

    @Autowired
    private UserOAuthTempRepository userOAuthTempRepository;
    private static String FINAL_FRONTEND_URL;

    @Value("${techspot.objectOrienters.frontendUrl}")
    public void setFinalFrontendUrl(String url) {
        FINAL_FRONTEND_URL = url;
    }

    @Transactional
    public String saveUserOAuthTempAndRedirectURL(UserOAuthTemp userOAuthTemp) {
        System.out.println(userOAuthTempRepository.save(userOAuthTemp));
        String redirectUrl = UriComponentsBuilder.fromHttpUrl(FINAL_FRONTEND_URL)
                .queryParam("id", userOAuthTemp.getId())
                .toUriString();

        System.out.println("OAuth2Services.saveUserOAuthTempAndRedirectURL: redirectUrl = " + redirectUrl);

        return redirectUrl;
    }

    @Transactional
    public User registerOuthUser(OAuthDto oAuthDto) throws IOException {
        UserOAuthTemp userOAuthTemp = userOAuthTempRepository.findById(oAuthDto.getId()).orElseThrow(() -> new RuntimeException("Invalid OAuth2 request"));

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setUsername(oAuthDto.getUsername());
        signupRequest.setEmail(userOAuthTemp.getEmail());
        signupRequest.setName(userOAuthTemp.getName());
        signupRequest.setPassword(userOAuthTemp.getProvider().name());

        System.out.println("OAuth2Services.registerOuthUser: signupRequest = " + signupRequest);
        userOAuthTempRepository.delete(userOAuthTemp);

        return generalAuthServices.registerUser(signupRequest);
    }

}
