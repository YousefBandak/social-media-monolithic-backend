package object_orienters.techspot.security.blacklist;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table
public class TokenBlackList {

    @Id
    private String token;

    private LocalDateTime blacklistDate;

    // Standard getters and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getBlacklistDate() {
        return blacklistDate;
    }

    public void setBlacklistDate(LocalDateTime blacklistDate) {
        this.blacklistDate = blacklistDate;
    }

    // Constructors
    public TokenBlackList() {
        // JPA requires a no-argument constructor
    }

    public TokenBlackList(String token, LocalDateTime blacklistDate) {
        this.token = token;
        this.blacklistDate = blacklistDate;
    }
}