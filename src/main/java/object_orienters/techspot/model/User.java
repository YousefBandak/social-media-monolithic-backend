package object_orienters.techspot.model;

import jakarta.persistence.*;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Entity
@Data
@Table(name = "User")
public class User {
    private @Id String userName;
    private BufferedImage profilePic;
    private String name;
    private String proffesion; // could be enum?
    private String email;
    private Gender gender;
    private LocalDate dob;
    @OneToMany
    private List<User> followers;
    @OneToMany
    private List<User> following;
    @OneToMany
    private List<Post> publishedPosts;
    @OneToMany
    private List<Post> sharedPosts;
}

enum Privacy {
    PUBLIC,
    PRIVATE
}

@Data
class Inbox {
    private Set<Chat> conversations;
}
