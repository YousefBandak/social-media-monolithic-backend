package object_orienters.techspot.security.blacklist;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImpleTokenBlackListService implements TokenBlackListService {

    private final TokenBlacklistRepository repository;

    @Autowired
    public ImpleTokenBlackListService(TokenBlacklistRepository repository) {
        this.repository = repository;
    }

    public void blacklistToken(String token) {
        TokenBlackList blacklistedToken = new TokenBlackList(token, LocalDateTime.now());
        repository.save(blacklistedToken);
    }

    public boolean isTokenBlacklisted(String token) {
        return repository.findById(token).isPresent();
    }
}
