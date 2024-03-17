package object_orienters.techspot.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import object_orienters.techspot.profile.Profile;

@Entity
@NoArgsConstructor
public class SharedPost {
    @Id
    @GeneratedValue
    private Long sharedPostId;
    @ManyToOne
    @JoinColumn(name = "profile_id")
    @JsonBackReference
    private Profile sharer;
    @OneToOne
    private Post post;

    public SharedPost(Profile sharer, Post post) {
        this.sharer = sharer;
        this.post = post;
    }
}
