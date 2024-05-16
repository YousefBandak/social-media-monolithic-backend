//package object_orienters.techspot.tag;
//
//
//import jakarta.persistence.*;
//import object_orienters.techspot.post.Post;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//@Entity
//@IdClass(PostTagId.class)
//@Table(name = "post_tags")
//public class PostTag implements Serializable {
//    @Id
//    @ManyToOne
//    @JoinColumn(name = "post_id")
//    private Post post;
//
//    @Id
//    @ManyToOne
//    @JoinColumn(name = "tag_id")
//    private Tag tag;
//
//    // Getters and Setters
//}
//
//class PostTagId implements Serializable {
//    private Long post;
//    private Long tag;
//
//    @Override
//    public int hashCode() {
//        int result = post != null ? post.hashCode() : 0;
//        result = 31 * result + (tag != null ? tag.hashCode() : 0);
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//        if (obj == null || getClass() != obj.getClass()) return false;
//        PostTagId that = (PostTagId) obj;
//        if (!Objects.equals(post, that.post)) return false;
//        return Objects.equals(tag, that.tag);
//    }
//
//
//}
//
