package object_orienters.techspot.feed;

import object_orienters.techspot.exceptions.ProfileNotFoundException;
import object_orienters.techspot.profile.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class FeedService {


    private final ProfileRepository profileRepository;
    private final FeedByFollowingStrategy feedByFollowingStrategy;
    private final FeedByTag feedByTag;
    // private FeedByAuthor feedByAuthor;
    //private ReactionsByContent reactionsByContent;
    // private CommentsByContent commentsByContent;
    private final SearchByName searchByName;
    private final GetFollowingofFollowing getFollowingofFollowing;


    @Autowired
    public FeedService(ProfileRepository profileRepository,
                       FeedByFollowingStrategy feedByFollowingStrategy,
                       FeedByTag feedByTag,
//                     FeedByAuthor feedByAuthor,
//                     CommentsByContent commentsByContent,
//                      ReactionsByContent reactionsByContent,
                       SearchByName searchByName,
                       GetFollowingofFollowing getFollowingofFollowing) {
        this.feedByFollowingStrategy = feedByFollowingStrategy;
        this.profileRepository = profileRepository;
        this.feedByTag = feedByTag;
        // this.feedByAuthor = feedByAuthor;
        // this.commentsByContent = commentsByContent;
//        this.reactionsByContent = reactionsByContent;
        this.searchByName = searchByName;
        this.getFollowingofFollowing = getFollowingofFollowing;
    }

    public Page<?> feedContent(FeedType feedType, String value, int pageNumber, int pageSize, String clientUsername) {
        switch (feedType) {

            case ALL_USERS:
                return feedByFollowingStrategy.operate(profileRepository.findByUsername(clientUsername).orElseThrow(() -> new ProfileNotFoundException(clientUsername)), pageNumber, pageSize);
//            case ONE_USER:
//                return feedByAuthor.operate(profileRepository.findByUsername(value).orElseThrow(() -> new ProfileNotFoundException(value)), pageNumber, pageSize);
            case TOPIC:
                return feedByTag.operate(value, pageNumber, pageSize);
//            case COMMENTS:
//                return commentsByContent.operate(Long.parseLong(value), pageNumber, pageSize);
//            case REACTIONS:
//                return reactionsByContent.operate(Long.parseLong(value), pageNumber, pageSize);
            case PROFILES:
                return searchByName.operate(value, pageNumber, pageSize);
            case MUTUAL_FOLLOWING:
                return getFollowingofFollowing.operate(value, pageNumber, pageSize);
            default:
                return Page.empty();
        }


    }

    enum FeedType {
        ALL_USERS,
        //  ONE_USER,
        TOPIC,
        // COMMENTS,
       // REACTIONS,
        PROFILES,
        MUTUAL_FOLLOWING
    }
}
