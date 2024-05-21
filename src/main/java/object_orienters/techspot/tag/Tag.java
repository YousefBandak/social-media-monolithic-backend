package object_orienters.techspot.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Tag {
    @Id
    private String tagName;
    @JsonIgnore
    private String posts;
}

