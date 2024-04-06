package object_orienters.techspot.security.blacklist;

import org.springframework.stereotype.Service;

@Service
public interface TokenBlackListService {

    public void blacklistToken(String token);

    public boolean isTokenBlacklisted(String token);
}
