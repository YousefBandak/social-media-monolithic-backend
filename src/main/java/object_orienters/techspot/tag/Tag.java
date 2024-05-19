package object_orienters.techspot.tag;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Tag {
    @Id
    private String tagName;
    private String posts;
}

