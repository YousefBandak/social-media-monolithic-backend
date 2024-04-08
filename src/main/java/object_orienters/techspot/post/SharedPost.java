package object_orienters.techspot.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import object_orienters.techspot.content.Content;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class SharedPost extends Content {
    @ManyToOne
    @JoinColumn(name = "profile_id")
    @JsonBackReference
    private Profile sharer;

    @ManyToOne(cascade = CascadeType.ALL)
    private Post post;

    private Privacy privacy;

    public SharedPost(Profile sharer, Post post, Privacy privacy) {
        this.sharer = sharer;
        this.post = post;
        this.privacy = privacy;
    }

    @Override
    public Profile getMainAuthor() {
        return getSharer();
    }
}
