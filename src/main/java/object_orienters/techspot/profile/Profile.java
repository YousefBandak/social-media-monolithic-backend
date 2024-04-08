package object_orienters.techspot.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.model.UserBase;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.SharedPost;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.security.model.User;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@SuppressWarnings({ "unchecked", "rawtypes" })
@Entity
@Data
@NoArgsConstructor
@Table(name = "profile")
@Valid
public class Profile extends UserBase {

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @Valid
    private User owner;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "datatype_id", referencedColumnName = "datatype_id", nullable = false)
    private DataType profilePic;
    @NotNull(message = "Name shouldn't be null.")
    @NotBlank(message = "Name cannot be left blank.")
    @Size(min = 3, max = 30, message = "Name size should be between 3 and 30 characters.")
    private String name;
    private String profession;
    // @Enumerated(EnumType.STRING)
    private Gender gender;
    @Past(message = "Date of Birth should be in the past.")
    private LocalDate dob;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "followship", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
    @JsonIgnore
    private List<Profile> following;

    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Profile> followers;

    @JsonIgnore
    @OneToMany(mappedBy = "contentAuthor", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> publishedPosts;

    @JsonIgnore
    @OneToMany(mappedBy = "sharer", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SharedPost> sharedPosts;

    public Profile(User user, String name, String profession, String email, DataType profilePic, Gender gender,
            String dob) {
        this.owner = user;
        this.name = name;
        this.profession = profession;
        this.setEmail(email);
        this.profilePic = profilePic;
        this.gender = gender;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.publishedPosts = new ArrayList<>();
        this.dob = LocalDate.parse(dob);
        this.setUsername(user.getUsername());
    }

    public String toString() {
        return "Username: " + getUsername() + " Name: " + name + " Profession: " + profession + " Email: " + getEmail()
                + "User: " + owner;
    }

    public List<Profile> getFollowing() {
        if (this.following == null) {
            this.following = new ArrayList<>();
        }
        return following;
    }

    @JsonIgnore
    private List<? extends Content> getPrivateAndPublicTimelinePosts() {
        List<? extends Content> timeline = new ArrayList<>();
        timeline.addAll((Collection) this.getSharedPosts());
        timeline.addAll((Collection) this.getPublishedPosts());
        return timeline;
    }

    @JsonIgnore
    public List<? extends Content> getTimelinePostsByPrivacy(Privacy privacy) {
        return switch (privacy) {
            case PUBLIC -> this.getPublicTimelinePosts();
            case PRIVATE -> this.getPrivateAndPublicTimelinePosts();
        };
    }

    @JsonIgnore
    private List<? extends Content> getPublicTimelinePosts() {
        return this.getPrivateAndPublicTimelinePosts().stream()
                .filter(content -> content.getPrivacy().equals(Privacy.PUBLIC)).toList();
    }

    public List<Post> getPublishedPosts() {
        if (this.publishedPosts == null) {
            this.publishedPosts = new ArrayList<>();
        }
        return publishedPosts;
    }

    public List<SharedPost> getSharedPosts() {
        if (this.sharedPosts == null) {
            this.sharedPosts = new ArrayList<>();
        }
        return sharedPosts;
    }

    public Timestamp getLastLogin() {
        return owner.getLastLogin();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Profile))
            return false;
        return getUsername() != null && getUsername().equals(((Profile) o).getUsername());
    }

    public enum Gender {
        MALE,
        FEMALE
    }

}
