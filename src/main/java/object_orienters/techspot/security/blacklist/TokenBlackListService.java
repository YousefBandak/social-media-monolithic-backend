package object_orienters.techspot.security.blacklist;

import org.springframework.stereotype.Service;

@Service
public interface TokenBlackListService {

    void blacklistToken(String token);

    boolean isTokenBlacklisted(String token);
}
