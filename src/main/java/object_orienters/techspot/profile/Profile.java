package object_orienters.techspot.profile;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import object_orienters.techspot.chat.Chat;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.SharedPost;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Profile")
public class Profile {

    @Id
    @Column(name = "profile_id")
    private String username;
    private String profilePic;
    private String name;
    private String profession;
    private String email;
    private Gender gender;
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


    public Profile(String username, String name, String profession, String email, String profilePic) {
        this.username = username;
        this.profilePic = profilePic;
        this.name = name;
        this.profession = profession;
        this.email = email;
        this.profilePic = profilePic;
        this.gender = Gender.FEMALE;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.publishedPosts = new ArrayList<>();
    }

    public String toString() {
        return "Username: " + username + " Name: " + name + " Profession: " + profession + " Email: " + email;
    }

}


enum Gender {
    MALE,
    FEMALE
}
