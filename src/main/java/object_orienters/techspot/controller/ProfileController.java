package object_orienters.techspot.controller;

import org.springframework.web.bind.annotation.RestController;

import object_orienters.techspot.exception.ChatNotFoundException;
import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Profile;
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

    //@RamHusam111 here the assembler returns links to self and to the followers 
    // get user profile
    @GetMapping("/profiles/{userName}")
    public EntityModel<Profile> one(@PathVariable String userName) {
        Profile user = repo.findById(userName).orElseThrow();
        return assembler.toModel(user);
    }

    // create new user profile
    @PostMapping("/profiles")
    public ResponseEntity<EntityModel<Profile>> createUser(@RequestBody Profile newUser) {
        EntityModel<Profile> entityModel = assembler.toModel(repo.save(newUser));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    //@RamHusam111 here when we use the assembler how will it return a link to the followers of the user?
    // update user profile
    @PutMapping("/profiles/{userName}")
    ResponseEntity<EntityModel<Profile>> updateProfile(@RequestBody Profile newUser, @PathVariable String userName)
            throws Exception {
        Profile updatedUser = repo.findById(userName).map(user -> {
            user.setUsername(newUser.getUsername());
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
        }).orElseThrow(() -> new UserNotFoundException(userName));
        EntityModel<Profile> entityModel = assembler.toModel(updatedUser);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    };

    // get user followers
    @GetMapping("/profiles/{userName}/followers")
    public ResponseEntity<CollectionModel<EntityModel<Profile>>> getFollowers(@PathVariable String userName) {
        List<EntityModel<Profile>> followers = repo.findFollowersByUserId(userName).stream()
                .map(userModel -> assembler.toModel(userModel))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(followers,
                linkTo(methodOn(ProfileController.class).one(userName)).withSelfRel()));
    }

    // get specific user follower
    @GetMapping("/profiles/{userName}/followers/{followerUserName}")
    public EntityModel<Profile> GetFollower(@PathVariable String userName, @PathVariable String followerUserName) {
        Optional<Profile> follower = repo.findFollowerByUsername(userName, followerUserName);
        return assembler.toModel(follower.get());

    }

    // get user followers
    @GetMapping("/profiles/{userName}/following")
    public ResponseEntity<CollectionModel<EntityModel<Profile>>> getFollowing(@PathVariable String userName) {
        List<EntityModel<Profile>> following = repo.findFollowingByUserId(userName).stream()
                .map(userModel -> assembler.toModel(userModel))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(following,
                linkTo(methodOn(ProfileController.class).one(userName)).withSelfRel()));
    }

    // get specific user following
    @GetMapping("/profiles/{userName}/following/{userName}")
    public EntityModel<Profile> getspecificFollowing(@PathVariable String userName,
            @PathVariable String followingUserName) {
        Optional<Profile> follower = repo.findFollowingByUsername(userName, followingUserName);
        return assembler.toModel(follower.get());
    }

    // add new follower to user
    @PostMapping("/profiles/{userName}/followers")
    public EntityModel<Profile> addNewFollower(@PathVariable String userName, @RequestBody Profile newFollower) {
        Optional<Profile> user = repo.findById(userName);
        user.get().getFollowers().add(newFollower);
        EntityModel<Profile> entityModel = assembler.toModel(repo.save(user.get()));
        return entityModel;
    }

    // delete follower from user
    @DeleteMapping("/profiles/{userName}/followers/{UserName}")
    ResponseEntity<Profile> deleteFollower(@PathVariable String userName) {
        repo.deleteById(userName);
        return ResponseEntity.noContent().build();
    }

    // add new following to user
    @PostMapping("/profiles/{userName}/following")
    public EntityModel<Profile> addNewFollowing(@PathVariable String userName, @RequestBody Profile newFollowing) {
        Optional<Profile> user = repo.findById(userName);
        user.get().getFollowers().add(newFollowing);
        EntityModel<Profile> entityModel = assembler.toModel(repo.save(user.get()));
        return entityModel;
    }

    // delete following from user
    @DeleteMapping("/profiles/{userName}/following/{delUserName}")
    ResponseEntity<Profile> deleteFollowing(@PathVariable String userName, @PathVariable String delUserName) {
        repo.deleteById(delUserName);
        return ResponseEntity.noContent().build();
    }
}
