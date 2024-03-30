package object_orienters.techspot.security.repository;

import java.util.Optional;

import object_orienters.techspot.security.model.ERole;
import object_orienters.techspot.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
