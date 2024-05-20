package object_orienters.techspot.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
public class UserBase {
    @Id
    @NotBlank
    @NotNull(message = "Username shouldn't be null.")
    @Size(min = 4, max = 20, message = "Username size should be between 4 and 20 characters.")
    private String username;

    @NotNull(message = "Email shouldn't be null.")
    @Email
    @NotBlank
    @Size(max = 50)
    private String email;






}
