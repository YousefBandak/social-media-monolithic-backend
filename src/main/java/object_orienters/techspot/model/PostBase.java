package object_orienters.techspot.model;

import object_orienters.techspot.comment.Comment;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.reaction.Reaction;

public interface PostBase {

    public void like(Reaction reaction);
    public void comment(Comment comment);
    public void share(Profile sharer);

    public long getPostId();

    public void editPrivacy(Privacy privacy);

    public void delete();



}
