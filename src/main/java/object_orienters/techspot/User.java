package object_orienters.techspot;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import lombok.Data;

@Entity
@Data
@Table(name = "User")
public class User {
    private @Id String username;
    private BufferedImage profilePic;
    private String name;
    private String proffesion; // could be enum?
    private String email;
    private Gender gender;
    private LocalDate dob;
    private List<User> followers;
    private List<User> following;
    // private List<Post> publishedPosts;
    // private List<Post> sharedPosts;
}

enum Privacy {
    PUBLIC,
    PRIVATE
}

@Data
class Inbox {
    private Set<Chat> conversations;
}
