package object_orienters.techspot.profile;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/profiles")
public class ProfileController {
    private final ProfileModelAssembler assembler;
    private final ImpleProfileService profileService;

    public ProfileController(ProfileModelAssembler assembler, ImpleProfileService profileService) {
        this.assembler = assembler;
        this.profileService = profileService;
    }

    // get user profile
    @GetMapping("/{username}")
    public ResponseEntity<?> one(@PathVariable String username) {
        try {
            EntityModel<Profile> profileModel = assembler.toModel(profileService.getUserByUsername(username));
            return ResponseEntity.ok(profileModel);
        } catch (UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }
    }

    // create new user profile
    @PreAuthorize("hasRole('ROLE_ADMIN')") //TODO: IMPLEMENT THIS METHOD correctly with user object
    @PostMapping("")
    public ResponseEntity<?> postProfile(@Valid @RequestBody Profile newProfile)
            throws EmailAlreadyUsedException, UsernameAlreadyUsedExeption {
        try {
            EntityModel<Profile> entityModel = assembler.toModel(profileService.createNewProfile(newProfile));
            return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } catch (EmailAlreadyUsedException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Problem.create().withTitle("Email Already Used.")
                            .withDetail(exception.getMessage() + ", Email must be unique."));
        } catch (UsernameAlreadyUsedExeption exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Problem.create().withTitle("Username Already Used.")
                            .withDetail(exception.getMessage() + ", Username must be unique."));
        }catch (DataIntegrityViolationException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Problem.create().withTitle("Violation exception")
                    .withDetail(exception.getMessage() + ", Violation exception"));
            }
    }

    // update user profile
    @PutMapping("/{username}")
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity<?> updateProfile(@Valid @RequestBody Profile newUser, @PathVariable String username) {
        try {
            Profile updatedUser = profileService.updateUserProfile(newUser, username);
            EntityModel<Profile> entityModel = assembler.toModel(updatedUser);
            return ResponseEntity.ok().location(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                    .body(entityModel);
        } catch (UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }
    };

    // get user followers
    @GetMapping("/{username}/followers")
    public ResponseEntity<?> Followers(@PathVariable String username) {
        try {
            List<EntityModel<Profile>> followers = profileService.getUserFollowersByUsername(username).stream()
                    .map(assembler::toModel)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(CollectionModel.of(followers,
                    linkTo(methodOn(ProfileController.class).one(username)).withSelfRel()));
        } catch (UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }
    }

    // get specific user follower
    @GetMapping("/{username}/followers/{followerUserName}")
    public ResponseEntity<?> getSpecificFollower(@PathVariable String username,
            @PathVariable String followerUserName) {
        try {
            return ResponseEntity
                    .ok(assembler.toModel(profileService.getFollowerByUsername(username, followerUserName)));
        } catch (UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }
    }

    // get user followers
    @GetMapping("/{username}/following")
    public ResponseEntity<?> Following(@PathVariable String username) {
        try {
            List<EntityModel<Profile>> following = profileService.getUserFollowingByUsername(username).stream()
                    .map(userModel -> assembler.toModel(userModel))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(CollectionModel.of(following,
                    linkTo(methodOn(ProfileController.class).one(username)).withSelfRel()));
        } catch (UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }
    }

    // get specific user following
    @GetMapping("/{username}/following/{followingUsername}")
    public ResponseEntity<?> getSpecificFollowing(@PathVariable String username,
            @PathVariable String followingUsername) {
        try {
            return ResponseEntity
                    .ok(assembler.toModel(profileService.getFollowingByUsername(username, followingUsername)));
        } catch (UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }
    }

    // TODO: IS IT POST MAPPING OR PUT?
    // add new follower to user
    @PostMapping("/{username}/followers")
    @PreAuthorize("#username == authentication.principal.username")
    public ResponseEntity<?> newFollower(@PathVariable String username, @RequestBody ObjectNode followerUserName) {
        try {
            return ResponseEntity.ok(assembler.toModel(profileService.addNewFollower(username, followerUserName.get("username").asText())));
        } catch (UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }
    }

    // delete follower from user
    @DeleteMapping("/{username}/followers")
    @PreAuthorize("#username == authentication.principal.username")
    ResponseEntity<?> deleteFollower(@PathVariable String username, @RequestBody Profile deletedUser) {
        try {
            profileService.deleteFollower(username, deletedUser);
            return ResponseEntity.noContent().build();
        } catch (UserNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Problem.create().withTitle("User Not Found").withDetail(exception.getMessage()));
        }
    }

}
