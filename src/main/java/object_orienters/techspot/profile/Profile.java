package object_orienters.techspot.profile;

import jakarta.persistence.*;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import lombok.Data;
import object_orienters.techspot.chat.Chat;
import object_orienters.techspot.post.Post;

@Entity
@Data
@Table(name = "Profile")
public class Profile {
    private @Id String username;
    private BufferedImage profilePic;
    private String name;
    private String proffesion; 
    private String email;
    private Gender gender;
    private LocalDate dob;
    @OneToMany
    private List<Profile> followers;
    @OneToMany
    private List<Profile> following;
    @OneToMany
    private List<Post> publishedPosts;
    @OneToMany
    private List<Post> sharedPosts;
    private Set<Chat> Inbox;
}

enum Privacy {
    PUBLIC,
    PRIVATE
}

