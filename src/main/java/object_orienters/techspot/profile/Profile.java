package object_orienters.techspot.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @NotNull(message = "Username shouldn't be null.")
    @Size(min = 4, max = 20, message = "Name size should be between 4 and 20 characters.")
    private String username;
    private String profilePic;
    @NotNull(message = "Name shouldn't be null.")
    @NotBlank(message = "Name cannot be left blank.")
    @Size(min = 3, max = 30, message = "Name size should be between 3 and 30 characters.")
    private String name;
    private String profession;
    @NotNull(message = "Email shouldn't be null.")
    @Email
    private String email;
    private Gender gender;
    @NotNull(message = "Date of Birth shouldn't be null.")
    @Past
    private LocalDate dob;

    @JsonIgnore
    @ManyToOne
    private Profile master;



    @ManyToMany
    @JoinTable(
            name = "followship",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    @JsonIgnore
    private List<Profile> following;

    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Profile> followers;

    @JsonIgnore
    @OneToMany(mappedBy = "author", fetch = FetchType.EAGER)
    private List<Post> publishedPosts;

    @JsonIgnore
    @OneToMany(mappedBy = "sharer", fetch = FetchType.EAGER)
    private List<SharedPost> sharedPosts; //TODO: Change to Post or SharedPost or Content

//   @OneToMany(mappedBy = "profile", fetch = FetchType.EAGER)
//   private Set<Chat> Inbox;


    public Profile(String username, String name, String profession, String email, String profilePic, Gender gender, String dob) {
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
        this.dob = LocalDate.parse(dob);
    }

    public String toString() {
        return "Username: " + username + " Name: " + name + " Profession: " + profession + " Email: " + email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile)) return false;
        return username != null && username.equals(((Profile) o).getUsername());
    }

    public enum Gender {
        MALE,
        FEMALE
    }

}



