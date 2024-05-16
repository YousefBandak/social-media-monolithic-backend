package object_orienters.techspot.tag;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Tag {
    @Id
    private String tagName;
    private String posts;
}

