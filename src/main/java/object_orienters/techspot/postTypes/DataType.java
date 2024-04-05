package object_orienters.techspot.postTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "data_types")
@Data
public class DataType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "datatype_id")
    private Long id; 
    private String type; 
    @Lob
    @Column(name = "Data", length = 1000000000)
    private byte[] data;
}
