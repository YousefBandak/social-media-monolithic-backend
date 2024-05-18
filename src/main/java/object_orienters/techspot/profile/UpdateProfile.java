package object_orienters.techspot.profile;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdateProfile {
    private LocalDate dob;
    private String email;
    private String name;
    private String profession;
    private Profile.Gender gender;
    private String password;
}
