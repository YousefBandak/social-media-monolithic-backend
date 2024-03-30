package object_orienters.techspot.profile;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import lombok.Value;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.SharedPost;
import object_orienters.techspot.security.User;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    Long id;


    @PrimaryKeyJoinColumn(name = "username")
    @OneToOne
    @JsonIgnore
    @Valid
    private User owner;

    private String profilePic;
    @NotNull(message = "Name shouldn't be null.")
    @NotBlank(message = "Name cannot be left blank.")
    @Size(min = 3, max = 30, message = "Name size should be between 3 and 30 characters.")
    private String name;
    private String profession;
    @NotNull(message = "Email shouldn't be null.")
    @Email
    //@UniqueEmail
    private String email;
    private Gender gender;
    @Past(message = "Date of Birth should be in the past.")
    private LocalDate dob;

//    @JsonIgnore
//    @ManyToOne
//    private Profile master;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "followship", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
    @JsonIgnore
    private List<Profile> following;

    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Profile> followers;

    @JsonIgnore
    @OneToMany(mappedBy = "contentAuthor", fetch = FetchType.EAGER)
    private List<Post> publishedPosts;

    @JsonIgnore
    @OneToMany(mappedBy = "sharer", fetch = FetchType.EAGER)
    private List<SharedPost> sharedPosts; // TODO: Change to Post or SharedPost or Content

    // @OneToMany(mappedBy = "profile", fetch = FetchType.EAGER)
    // private Set<Chat> Inbox;

    public Profile(User user, String name, String profession, String email, String profilePic, Gender gender,
                   String dob) {
        this.owner = user;
        this.name = name;
        this.profession = profession;
        this.email = email;
        this.profilePic = profilePic;
        this.gender = gender;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.publishedPosts = new ArrayList<>();
        this.dob = LocalDate.parse(dob);
    }

    public String toString() {
        return "Username: " + getUsername() + " Name: " + name + " Profession: " + profession + " Email: " + email + "User: " + owner;
    }

    public List<Profile> getFollowing() {
        if (this.following == null) {
            this.following = new ArrayList<>();
        }
        return following;
    }


   @JsonProperty("ownerUsername")
   public String getUsername() {
       if (this.owner != null) {
           return this.owner.getUsername();
       } else {
           return null; // or handle the case where owner is null
       }
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
