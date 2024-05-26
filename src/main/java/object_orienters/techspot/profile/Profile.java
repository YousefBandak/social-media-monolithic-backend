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
import object_orienters.techspot.model.UserBase;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.postTypes.DataType;
import object_orienters.techspot.security.model.User;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


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

    @NotNull(message = "Name shouldn't be null.")
    @NotBlank(message = "Name cannot be left blank.")
    @Size(min = 3, max = 30, message = "Name size should be between 3 and 30 characters.")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_pic_datatype_id", referencedColumnName = "datatype_id", nullable = true)
    private DataType profilePic;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "background_img_datatype_id", referencedColumnName = "datatype_id", nullable = true)
    private DataType backgroundImg;

    private String profession;

    private Gender gender;

    @Past(message = "Date of Birth should be in the past.")
    private LocalDate dob;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "followship", joinColumns = @JoinColumn(name = "follower_id"), inverseJoinColumns = @JoinColumn(name = "following_id"))
    @JsonIgnore
    private List<Profile> following;

    @JsonIgnore
    @OneToMany(mappedBy = "contentAuthor", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> publishedPosts;

    @ManyToMany(mappedBy = "following", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Profile> followers;

    public Profile(User user, String name, String profession, String email, DataType profilePic, Gender gender,
            String dob) {
        this.owner = user;
        this.setName(name);
        this.profession = profession;
        this.setEmail(email);
        this.profilePic = profilePic;
        this.gender = gender;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.dob = LocalDate.parse(dob);
        this.setUsername(user.getUsername());
    }

    public String toString() {
        return "Username: " + getUsername() + " Name: " + getName() + " Profession: " + profession + " Email: "
                + getEmail();
    }

    public List<Profile> getFollowing() {
        if (this.following == null) {
            this.following = new ArrayList<>();
        }
        return following;
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

    @Override
    public int hashCode() {
        return Objects.hash(this.getUsername());
    }

    public enum Gender {
        MALE,
        FEMALE
    }

}