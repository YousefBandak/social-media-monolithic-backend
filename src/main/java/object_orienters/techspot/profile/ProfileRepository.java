package object_orienters.techspot.profile;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Query("SELECT f FROM Profile u JOIN u.followers f WHERE u.owner.username = :userName")
    List<Profile> findFollowersByUserId(String userName);

    @Query("SELECT f FROM Profile u JOIN u.followers f WHERE f.owner.username = :username AND u.owner.username = :accountUsername")
    Profile findFollowerByUsername(String accountUsername, String username);

    @Query("SELECT f FROM Profile u JOIN u.following f WHERE u.owner.username = :userName")
    List<Profile> findFollowingByUserId(String userName);

    //TODO: Fix this query to return  Optional<Profile> instead of Profile
    @Query("SELECT f FROM Profile u JOIN u.following f WHERE f.owner.username = :username AND u.owner.username = :accountUsername")
    Profile findFollowingByUsername(String accountUsername, String username);

    @Query("SELECT u FROM Profile u WHERE u.owner.username = :username")
    Optional<Profile> findByUsername(String username);
    Optional<Profile>  findByEmail(String email);

}
