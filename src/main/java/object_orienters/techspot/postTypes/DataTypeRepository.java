package object_orienters.techspot.postTypes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataTypeRepository extends JpaRepository<DataType, Long> {

}
