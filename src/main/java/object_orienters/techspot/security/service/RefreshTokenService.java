package object_orienters.techspot.security.service;

import object_orienters.techspot.exceptions.TokenRefreshException;
import object_orienters.techspot.security.model.RefreshToken;
import object_orienters.techspot.security.model.User;
import object_orienters.techspot.security.repository.RefreshTokenRepository;
import object_orienters.techspot.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${techspot.objectOrienters.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(String userId) {
        final RefreshToken[] refreshToken = new RefreshToken[1];
        User user = userRepository.findById(userId).get();
        refreshTokenRepository.findByUser(user)
                .ifPresentOrElse(rt -> refreshToken[0] = rt, () -> refreshToken[0] = new RefreshToken());
        refreshToken[0].setUser(user);
        refreshToken[0].setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken[0].setToken(UUID.randomUUID().toString());
        refreshTokenRepository.save(refreshToken[0]);
        return refreshToken[0];
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException(token.getToken(), "Expired token. Please issue a new request");
        }
        return token;
    }
}
