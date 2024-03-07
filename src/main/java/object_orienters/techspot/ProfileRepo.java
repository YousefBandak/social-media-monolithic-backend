package object_orienters.techspot;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepo extends JpaRepository<User, String>{
    
}
