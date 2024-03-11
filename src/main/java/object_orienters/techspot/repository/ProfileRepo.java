package object_orienters.techspot.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import object_orienters.techspot.model.Profile;

public interface ProfileRepo extends JpaRepository<Profile, String> {

    @Query("SELECT f FROM User u JOIN u.followers f WHERE u.username = :userName")
    List<Profile> findFollowersByUserId(String userName);

    @Query("SELECT f FROM User u JOIN u.followers f WHERE f.username = :username AND u.username = :accountUsername")
    Optional<Profile> findFollowerByUsername(String accountUsername, String username);

    @Query("SELECT f FROM User u JOIN u.following f WHERE u.username = :userName")
    List<Profile> findFollowingByUserId(String userName);

    @Query("SELECT f FROM User u JOIN u.following f WHERE f.username = :username AND u.username = :accountUsername")
    Optional<Profile> findFollowingByUsername(String accountUsername, String username);

}
