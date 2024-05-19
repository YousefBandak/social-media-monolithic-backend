package object_orienters.techspot.post;

import java.util.List;

import object_orienters.techspot.model.Privacy;
import object_orienters.techspot.profile.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedPostRepository extends JpaRepository<SharedPost, Long> {

    List<SharedPost> findByPost(Post post);
    void deleteAllByPost(Post post);
}
