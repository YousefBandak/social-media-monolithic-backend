package object_orienters.techspot.profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;


import java.time.LocalDate;
import java.util.ArrayList;
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
    private String username;
    private String profilePic;
    private String name;
    private String profession;
    private String email;
    private Gender gender;
    private LocalDate dob;

    @JsonIgnore
    @ManyToOne
    private Profile master;

    @OneToMany(mappedBy = "master", fetch = FetchType.EAGER)
    @JsonIgnore//NOTE: These are present in hateos links no need to include them in the response
    private List<Profile> followers;

    @JsonIgnore
    @OneToMany(mappedBy = "master", fetch = FetchType.EAGER)
    private List<Profile> following;

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



