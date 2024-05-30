package object_orienters.techspot.security.repository;

import object_orienters.techspot.security.model.Provider;
import object_orienters.techspot.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  Optional<User> findByProviderAndProviderId(Provider provider, String id);
}
