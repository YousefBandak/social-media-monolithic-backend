package object_orienters.techspot.postTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import object_orienters.techspot.content.ReactableContent;

@Entity
@Table(name = "data_types")
@Data
public class DataType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "datatype_id")
    private Long id;

    private String fileName;
    private String fileUrl;
    private String type;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id")
    private ReactableContent content;

    public String toString() {
        return "DataType{" +
                "id=" + id +
                ", fileName='" + fileName + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
