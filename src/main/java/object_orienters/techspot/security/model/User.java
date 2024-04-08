package object_orienters.techspot.security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import object_orienters.techspot.model.UserBase;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "user", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
@NoArgsConstructor
public class User extends UserBase {

    @NotBlank
    @Size(max = 120)
    private String password;

    private Timestamp lastLogin;

    public User(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email,
            @NotBlank @Size(max = 120) String password) {
        this.setUsername(username);
        this.setEmail(email);
        this.password = password;
    }

    public String toString() {
        return "User [email=" + getEmail() + ", password=" + password + ", username=" + getUsername() + "]";
    }

}
