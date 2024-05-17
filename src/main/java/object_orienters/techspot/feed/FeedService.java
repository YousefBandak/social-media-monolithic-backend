package object_orienters.techspot.feed;

import object_orienters.techspot.post.Post;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileNotFoundException;
import object_orienters.techspot.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class FeedService {


    private ProfileRepository profileRepository;
    private FeedByFollowingStrategy feedByFollowingStrategy;
    private FeedByTag feedByTag;
    private FeedByAuthor feedByAuthor;
    private ReactionsByContent reactionsByContent;
    private CommentsByContent commentsByContent;
    private SearchByName searchByName;


    @Autowired
    public FeedService(FeedByFollowingStrategy feedByFollowingStrategy,
                       ProfileRepository profileRepository,
                       FeedByTag feedByTag,
                       FeedByAuthor feedByAuthor,
                       CommentsByContent commentsByContent,
                       ReactionsByContent reactionsByContent,
                       SearchByName searchByName) {
        this.feedByFollowingStrategy = feedByFollowingStrategy;
        this.profileRepository = profileRepository;
        this.feedByTag = feedByTag;
        this.feedByAuthor = feedByAuthor;
        this.commentsByContent = commentsByContent;
        this.reactionsByContent = reactionsByContent;
        this.searchByName = searchByName;
    }

    public Page<?> feedContent(FeedType feedType, String value, int pageNumber, int pageSize, String clientUsername) {
        switch (feedType) {

            case ALL_USERS:
                Profile profile = profileRepository.findByUsername(clientUsername).orElseThrow(() -> new ProfileNotFoundException(clientUsername));
                return feedByFollowingStrategy.operate(profile, pageNumber, pageSize);
            case ONE_USER:
                return feedByAuthor.operate(profileRepository.findByUsername(value).orElseThrow(() -> new ProfileNotFoundException(value)), pageNumber, pageSize);
            case TOPIC:
                return feedByTag.operate(value, pageNumber, pageSize);
            case COMMENTS:
                return commentsByContent.operate(Long.parseLong(value), pageNumber, pageSize);
            case REACTIONS:
                return reactionsByContent.operate(Long.parseLong(value), pageNumber, pageSize);
            case PROFILES:
                return searchByName.operate(value, pageNumber, pageSize);
            default:
                return Page.empty();
        }


    }

    enum FeedType {
        ALL_USERS, ONE_USER, TOPIC, COMMENTS, REACTIONS, PROFILES
    }
}
