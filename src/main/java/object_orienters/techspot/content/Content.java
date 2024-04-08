package object_orienters.techspot.content;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;

import java.sql.Timestamp;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "content")
@Data
public abstract class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "content_id", updatable = false, nullable = false, insertable = false)
    @Getter
    private Long contentID;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Timestamp timestamp;

    public Content() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public abstract Privacy getPrivacy();

    @JsonIgnore
    public abstract Profile getMainAuthor();

}
