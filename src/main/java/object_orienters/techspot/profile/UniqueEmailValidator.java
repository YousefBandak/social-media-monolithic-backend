package object_orienters.techspot.profile;

import org.springframework.stereotype.Component;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    // @Autowired
    // private ProfileRepository repo;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email != null && RepositoryAccessor.getProfileRepository().findByEmail(email) == null)
            return true;
        return false;
    }
}
