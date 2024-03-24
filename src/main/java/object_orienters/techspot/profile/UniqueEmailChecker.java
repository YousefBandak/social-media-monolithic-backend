package object_orienters.techspot.profile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueEmailChecker implements ConstraintValidator<UniqueEmail, String>{
    
    private ProfileRepository repo;


    public UniqueEmailChecker(ProfileRepository repo) {
        this.repo = repo;
    }
    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return email != null && repo.findByEmail(email) == null;
    }


}
