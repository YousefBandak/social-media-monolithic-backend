package object_orienters.techspot.security.service;

import object_orienters.techspot.exceptions.EmailAlreadyExistsException;
import object_orienters.techspot.exceptions.UserNotFoundException;
import object_orienters.techspot.exceptions.UsernameAlreadyExistsException;
import object_orienters.techspot.profile.ProfileService;
import object_orienters.techspot.security.jwt.JwtUtils;
import object_orienters.techspot.security.model.*;
import object_orienters.techspot.security.payload.response.JwtResponse;
import object_orienters.techspot.security.repository.UserOAuthTempRepository;
import object_orienters.techspot.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
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

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileService profileService;

    @Value("${techspot.objectOrienters.frontendUrl}")
    public void setFinalFrontendUrl(String url) {
        FINAL_FRONTEND_URL = url;
    }

    @Transactional
    public String saveUserOAuthTempAndRedirectURL(UserOAuthTemp userOAuthTemp) {
        userOAuthTempRepository.save(userOAuthTemp);
        String redirectUrl = UriComponentsBuilder.fromHttpUrl(FINAL_FRONTEND_URL)
                .queryParam("id", userOAuthTemp.getId())
                .queryParam("provider", userOAuthTemp.getProvider())
                .toUriString();


        return redirectUrl;
    }

    @Transactional
    public JwtResponse registerOuthUser(OAuthDto oAuthDto) throws IOException {
        if (userRepository.existsByUsername(oAuthDto.getUsername()))
            throw new UsernameAlreadyExistsException(oAuthDto.getUsername());

        UserOAuthTemp userOAuthTemp = userOAuthTempRepository.findById(oAuthDto.getId()).orElseThrow(() -> new RuntimeException("Invalid OAuth2 request"));

        if (userRepository.existsByEmail(userOAuthTemp.getEmail()))
            throw new EmailAlreadyExistsException(userOAuthTemp.getEmail());


        userOAuthTempRepository.delete(userOAuthTemp);
        System.out.println("enre" + userOAuthTemp.getEmail());
        User user = new User(oAuthDto.getUsername(), userOAuthTemp.getEmail(), oAuthDto.getProvider(), oAuthDto.getId());
        userRepository.save(user);
        profileService.createNewProfile(user.getUsername(), user.getEmail(), userOAuthTemp.getName());

        String jwt = jwtUtils.generateTokenFromUsername(user.getUsername());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return new JwtResponse(
                jwt,
                refreshToken.getToken(),
                user.getUsername(),
                user.getEmail()
        );

    }

    @Autowired
    AuthenticationManager authenticationManager;

    @Transactional
    public JwtResponse loginOAuthUser(UserOAuthTemp userOAuthTemp, Provider provider) {
        User user = userRepository.findByProviderAndProviderId(provider, userOAuthTemp.getId()).orElseThrow(() -> new UserNotFoundException(""));
        String jwt = jwtUtils.generateTokenFromUsername(user.getUsername());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getUsername());

        return new JwtResponse(
                jwt,
                refreshToken.getToken(),
                user.getUsername(),
                user.getEmail()
        );

    }

    public boolean isLogin(UserOAuthTemp userOAuthTemp, Provider provider) {
        return userRepository.findByProviderAndProviderId(provider, userOAuthTemp.getId()).isPresent();
    }
}
