package object_orienters.techspot.controller;

import org.springframework.web.bind.annotation.RestController;

import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Profile;
import object_orienters.techspot.repository.ProfileModelAssembler;
import object_orienters.techspot.repository.ProfileRepo;
import object_orienters.techspot.service.ImpleProfileService;

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
    private final ImpleProfileService profileService;

    public ProfileController(ProfileRepo repo, ProfileModelAssembler assembler, ImpleProfileService profileService) {
        this.repo = repo;
        this.assembler = assembler;
        this.profileService = profileService;
    }

    // TODO: IMPLEMENT THIS METHOD
    // @GetMapping("path")
    // public String getUserPosts(@RequestParam String param) {
    // return new String();
    // }

    // TODO: IMPLEMENT THIS METHOD
    // @GetMapping("path")
    // public String getUserChats(@RequestParam String param) {
    // return new String();
    // }

    // @RamHusam111 here the assembler returns links to self and to the followers
    // get user profile
    @GetMapping("/profiles/{userName}")
    public EntityModel<Profile> one(@PathVariable String username) throws UserNotFoundException {
        return assembler.toModel(profileService.getUserByUsername(username));
    }

    // create new user profile
    @PostMapping("/profiles")
    public ResponseEntity<EntityModel<Profile>> createUser(@RequestBody Profile newUser) {
        EntityModel<Profile> entityModel = assembler.toModel(profileService.createNewUser(newUser));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    // @RamHusam111 here when we use the assembler how will it return a link to the
    // followers of the user?
    // update user profile
    @PutMapping("/profiles/{userName}")
    ResponseEntity<EntityModel<Profile>> updateProfile(@RequestBody Profile newUser, @PathVariable String username)
            throws UserNotFoundException {
        Profile updatedUser = profileService.updateUserProfile(newUser, username);
        EntityModel<Profile> entityModel = assembler.toModel(updatedUser);
        return ResponseEntity.ok().location(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    };

    // get user followers
    @GetMapping("/profiles/{userName}/followers")
    public ResponseEntity<CollectionModel<EntityModel<Profile>>> Followers(@PathVariable String username)
            throws UserNotFoundException {
        List<EntityModel<Profile>> followers = profileService.getUserFollowersByUsername(username).stream()
                .map(userModel -> assembler.toModel(userModel))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(followers,
                linkTo(methodOn(ProfileController.class).one(username)).withSelfRel()));
    }

    // get specific user follower
    @GetMapping("/profiles/{userName}/followers/{followerUserName}")
    public EntityModel<Profile> getSpecificFollower(@PathVariable String username,
            @PathVariable String followerUserName) {
        Profile follower = profileService.getFollowerByUsername(username, followerUserName);
        return assembler.toModel(follower);

    }

    // get user followers
    @GetMapping("/profiles/{userName}/following")
    public ResponseEntity<CollectionModel<EntityModel<Profile>>> Following(@PathVariable String userName)
            throws UserNotFoundException {
        List<EntityModel<Profile>> following = profileService.getUserFollowingByUsername(userName).stream()
                .map(userModel -> assembler.toModel(userModel))
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(following,
                linkTo(methodOn(ProfileController.class).one(userName)).withSelfRel()));
    }

    // get specific user following
    @GetMapping("/profiles/{userName}/following/{userName}")
    public EntityModel<Profile> getSpecificFollowing(@PathVariable String username,
            @PathVariable String followingUsername) throws UserNotFoundException {
        Profile follower = profileService.getFollowingByUsername(username, followingUsername);
        return assembler.toModel(follower);
    }

    // add new follower to user
    @PostMapping("/profiles/{userName}/followers")
    public EntityModel<Profile> newFollower(@PathVariable String username, @RequestBody Profile newFollower)
            throws UserNotFoundException {
        return assembler.toModel(profileService.addNewFollower(username, newFollower));
    }

    // delete follower from user
    @DeleteMapping("/profiles/{userName}/followers/{UserName}")
    ResponseEntity<Profile> deleteFollower(@PathVariable String username) throws UserNotFoundException {
        profileService.deleteFollower(username);
        return ResponseEntity.noContent().build();
    }

    // add new following to user
    @PostMapping("/profiles/{userName}/following")
    public EntityModel<Profile> newFollowing(@PathVariable String username, @RequestBody Profile newFollowing)
            throws UserNotFoundException {
        return assembler.toModel(profileService.addNewFollowing(username, newFollowing));
    }

    // TODO
    // delete following from user
    @DeleteMapping("/profiles/{userName}/following/{delUserName}")
    ResponseEntity<Profile> deleteFollowing(@PathVariable String username, @PathVariable String delUserName)
            throws UserNotFoundException {
                profileService.deleteFollowing(username);
        return ResponseEntity.noContent().build();
    }
}
