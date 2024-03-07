package object_orienters.techspot;

import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/profiles/{userName}")
    public EntityModel<User> one(@PathVariable String userName) {
        User user = repo.findById(userName).orElseThrow();
        return assembler.toModel(user);
    }

    @PostMapping("/profiles")
    public ResponseEntity<EntityModel<User>> createUser(@PathVariable User newUser) {
        EntityModel<User> entityModel = assembler.toModel(repo.save(newUser));
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    @PutMapping("/profiles/{userName}")
    ResponseEntity<EntityModel<User>> updateProfile(@RequestBody User newUser, @PathVariable String userName) {
        User updatedUser = repo.findById(userName).map(user -> {
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
        })
                .orElseGet(() -> {
                    newUser.setUsername(userName);
                    return repo.save(newUser);
                });
        EntityModel<User> entityModel = assembler.toModel(updatedUser);
        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    };

    // TODO: needs Attention
    // @GetMapping("localhost:8080/profiles/{userName}/followers")
    // public String getMethodName(@PathVariable String userName) {
    // List<EntityModel<User>> followers = repo.findAll().stream()
    // .map(patient -> assembler.toModel(patient)).collect(Collectors.toList());

    // return CollectionModel.of(patients,
    // linkTo(methodOn(PatientController.class).all()).withSelfRel());
    // }

    @GetMapping("/profiles/{userName}/followers/{getUserName}")
    public String GetFollower(@PathVariable String userName, @PathVariable String getUserName) {
        return new String();
    }

    @GetMapping("path")
    public String getFollowing(@RequestParam String param) {
        return new String();
    }

    @GetMapping("path")
    public String getspecificFollowing(@RequestParam String param) {
        return new String();
    }

    @PostMapping("path")
    public String addNewFollower(@RequestBody String entity) {
        // TODO: process POST request

        return entity;
    }

    @DeleteMapping("/profiles/{userName}/followers/{UserName}")
    ResponseEntity<User> deleteFollower(@PathVariable String userName) {
        repo.deleteById(userName);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profiles/{userName}/following")
    public String addFollowing(@PathVariable String userName, @RequestBody String entity) {
        // TODO: process PUT request

        return entity;
    }

    @DeleteMapping("/profiles/{userName}/following/{delUserName}")
    ResponseEntity<User> deleteFollowing(@PathVariable String userName, @PathVariable String delUserName) {
        repo.deleteById(delUserName);
        return ResponseEntity.noContent().build();
    }
}
