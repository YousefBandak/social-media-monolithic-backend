package object_orienters.techspot.security.model;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import object_orienters.techspot.model.UserBase;
import object_orienters.techspot.security.model.Role;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "username"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;


    public User(@NotBlank @Size(max = 20) String username, @NotBlank @Size(max = 50) @Email String email,
            @NotBlank @Size(max = 120) String password) {
        this.setUsername(username);
        this.setEmail(email);
        this.password = password;
    }

    public String toString() {
        return "User [email=" + getEmail() + ", password=" + password + ", roles=" + roles + ", username=" + getUsername() + "]";
    }



}
