package object_orienters.techspot.controler;

import org.springframework.web.bind.annotation.RestController;

import object_orienters.techspot.model.User;
import object_orienters.techspot.repository.ProfileModelAssembler;
import object_orienters.techspot.repository.ProfileRepo;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class ProfileController {
    private final ProfileRepo repo;
    private final ProfileModelAssembler assembler;

    public ProfileController(ProfileRepo repo, ProfileModelAssembler assembler) {
        this.repo = repo;
        this.assembler = assembler;
    }

    // get user profile
    @GetMapping("/profiles/{userName}")
    public EntityModel<User> one(@PathVariable String userName) {
        User user = repo.findById(userName).orElseThrow();
        return assembler.toModel(user);
    }

    // create new user profile
    @PostMapping("/profiles")
    public ResponseEntity<EntityModel<User>> createUser(@PathVariable User newUser) {
        EntityModel<User> entityModel = assembler.toModel(repo.save(newUser));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // update user profile
    @PutMapping("/profiles/{userName}")
    ResponseEntity<EntityModel<User>> updateProfile(@RequestBody User newUser, @PathVariable String userName) {
        User updatedUser = repo.findById(userName).map(user -> {
            user.setUserName(newUser.getUserName());
            user.setProfilePic(newUser.getProfilePic());
            user.setDob(newUser.getDob());
            user.setEmail(newUser.getEmail());
            user.setFollowers(newUser.getFollowers());
            user.setFollowing(newUser.getFollowing());
            user.setName(newUser.getName());
            user.setProffesion(newUser.getProffesion());
            user.setGender(newUser.getGender());
            user.setPublishedPosts(newUser.getPublishedPosts());
            user.setSharedPosts(newUser.getSharedPosts());
            return repo.save(user);
        })
                .orElseGet(() -> {
                    newUser.setUserName(userName);
                    return repo.save(newUser);
                });
        EntityModel<User> entityModel = assembler.toModel(updatedUser);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    };

    // get user followers
    @GetMapping("localhost:8080/profiles/{userName}/followers")
    public ResponseEntity<CollectionModel<EntityModel<User>>> getFollowers(@PathVariable String userName) {
        List<EntityModel<User>> followers = repo.findFollowersByUserId(userName).stream()
                .map(userModel -> assembler.toModel(userModel))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(followers,
                linkTo(methodOn(ProfileController.class).one(userName)).withSelfRel()));
    }

    // get specific user follower
    @GetMapping("/profiles/{userName}/followers/{followerUserName}")
    public EntityModel<User> GetFollower(@PathVariable String userName, @PathVariable String followerUserName) {
        Optional<User> follower = repo.findFollowerByUsername(userName, followerUserName);
        return assembler.toModel(follower.get());

    }

    // get user followers
    @GetMapping("/profiles/{userName}/following")
    public ResponseEntity<CollectionModel<EntityModel<User>>> getFollowing(@PathVariable String userName) {
        List<EntityModel<User>> following = repo.findFollowingByUserId(userName).stream()
                .map(userModel -> assembler.toModel(userModel))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(following,
                linkTo(methodOn(ProfileController.class).one(userName)).withSelfRel()));
    }

    // get specific user following 
    @GetMapping("/profiles/{userName}/following/{userName}")
    public EntityModel<User> getspecificFollowing(@PathVariable String userName,
            @PathVariable String followingUserName) {
        Optional<User> follower = repo.findFollowingByUsername(userName, followingUserName);
        return assembler.toModel(follower.get());
    }

    // add new follower to user
    @PostMapping("/profiles/{userName}/followers")
    public EntityModel<User> addNewFollower(@PathVariable String userName, @RequestBody User newFollower) {
        Optional<User> user = repo.findById(userName);
        user.get().getFollowers().add(newFollower);
        EntityModel<User> entityModel = assembler.toModel(repo.save(user.get()));
        return entityModel;
    }

    // delete follower from user 
    @DeleteMapping("/profiles/{userName}/followers/{UserName}")
    ResponseEntity<User> deleteFollower(@PathVariable String userName) {
        repo.deleteById(userName);
        return ResponseEntity.noContent().build();
    }

    // add new following to user
    @PostMapping("/profiles/{userName}/following")
    public EntityModel<User> addNewFollowing(@PathVariable String userName, @RequestBody User newFollowing) {
        Optional<User> user = repo.findById(userName);
        user.get().getFollowers().add(newFollowing);
        EntityModel<User> entityModel = assembler.toModel(repo.save(user.get()));
        return entityModel;
    }

    // delete following from user
    @DeleteMapping("/profiles/{userName}/following/{delUserName}")
    ResponseEntity<User> deleteFollowing(@PathVariable String userName, @PathVariable String delUserName) {
        repo.deleteById(delUserName);
        return ResponseEntity.noContent().build();
    }
}
