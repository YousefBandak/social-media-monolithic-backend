package object_orienters.techspot.profile;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateProfile {
    private LocalDate dob;
    private String email;
    private String name;
    private String profession;
    private Profile.Gender gender;
    private String password;
}
