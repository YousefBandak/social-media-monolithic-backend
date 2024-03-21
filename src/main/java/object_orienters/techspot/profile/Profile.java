package object_orienters.techspot.profile;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.SharedPost;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Profile")
public class Profile {

    @Id
    @Column(name = "profile_id")
    @NotNull
    @Size(min = 4, max = 20)
    private String username;
    private String profilePic;
    @NotNull
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;
    private String profession;
    @NotNull
    @Email
    private String email;
    private Gender gender;
    @NotNull
    @Past
    private LocalDate dob;
    @ManyToOne
    private Profile master;

    @OneToMany(mappedBy = "master", fetch = FetchType.EAGER)
    private List<Profile> followers;

    @OneToMany(mappedBy = "master", fetch = FetchType.EAGER)
    private List<Profile> following;

    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<Post> publishedPosts;

    @OneToMany(mappedBy = "sharer", fetch = FetchType.EAGER)
    private List<SharedPost> sharedPosts; //TODO: Change to Post or SharedPost or Content
//   @OneToMany(mappedBy = "profile", fetch = FetchType.EAGER)
//   private Set<Chat> Inbox;


    public Profile(String username, String name, String profession, String email, String profilePic, Gender gender) {
        this.username = username;
        this.profilePic = profilePic;
        this.name = name;
        this.profession = profession;
        this.email = email;
        this.profilePic = profilePic;
        this.gender = gender;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.publishedPosts = new ArrayList<>();
    }

    public String toString() {
        return "Username: " + username + " Name: " + name + " Profession: " + profession + " Email: " + email;
    }

    public enum Gender {
        MALE,
        FEMALE
    }

}



