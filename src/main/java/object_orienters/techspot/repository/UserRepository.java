package object_orienters.techspot.repository;

import object_orienters.techspot.model.Chat;
import object_orienters.techspot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
