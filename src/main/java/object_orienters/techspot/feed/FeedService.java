package object_orienters.techspot.feed;

import object_orienters.techspot.content.Content;
import object_orienters.techspot.exceptions.ProfileNotFoundException;
import object_orienters.techspot.post.Post;
import object_orienters.techspot.post.PostModelAssembler;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileModelAssembler;
import object_orienters.techspot.profile.ProfileRepository;
import object_orienters.techspot.tag.Tag;
import object_orienters.techspot.tag.TagsAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class FeedService {


    private final ProfileRepository profileRepository;
    private final FeedByFollowingStrategy feedByFollowingStrategy;
    private final FeedByTag feedByTag;
    // private FeedByAuthor feedByAuthor;
    //private ReactionsByContent reactionsByContent;
    // private CommentsByContent commentsByContent;
    private final SearchByCode searchByCode;

    private final TopTags topTags;
    private final SearchByName searchByName;
    private final GetFollowingofFollowing getFollowingofFollowing;
    private final PostModelAssembler postModelAssembler;
    private final ProfileModelAssembler profileModelAssembler;

    private final TagsAssembler tagsAssembler;


    @Autowired
    public FeedService(ProfileRepository profileRepository,
                       FeedByFollowingStrategy feedByFollowingStrategy,
                       FeedByTag feedByTag,
//                     FeedByAuthor feedByAuthor,
//                     CommentsByContent commentsByContent,
//                      ReactionsByContent reactionsByContent,
                       SearchByName searchByName,
                       GetFollowingofFollowing getFollowingofFollowing,
                       PostModelAssembler postModelAssembler,
                       ProfileModelAssembler profileModelAssembler,
                       TagsAssembler tagsAssembler,
                       TopTags topTags,
                       SearchByCode searchByCode) {
        this.feedByFollowingStrategy = feedByFollowingStrategy;
        this.profileRepository = profileRepository;
        this.feedByTag = feedByTag;
        // this.feedByAuthor = feedByAuthor;
        // this.commentsByContent = commentsByContent;
//        this.reactionsByContent = reactionsByContent;
        this.searchByName = searchByName;
        this.getFollowingofFollowing = getFollowingofFollowing;
        this.postModelAssembler = postModelAssembler;
        this.profileModelAssembler = profileModelAssembler;
        this.topTags = topTags;
        this.tagsAssembler = tagsAssembler;
        this.searchByCode = searchByCode;
    }

    public PagedModel<?> feedContent(FeedType feedType, String value, int pageNumber, int pageSize) {
        switch (feedType) {

            case ALL_USERS:
                String clientUsername = SecurityContextHolder.getContext().getAuthentication().getName();
                Page<Content> feed = feedByFollowingStrategy.operate(profileRepository.findByUsername(clientUsername).orElseThrow(() -> new ProfileNotFoundException(clientUsername)), pageNumber, pageSize);
                return PagedModel.of(feed.stream().map(postModelAssembler::toModel).toList(), new PagedModel.PageMetadata(feed.getSize(), feed.getNumber(), feed.getTotalElements(), feed.getTotalPages()));
//            case ONE_USER:
//                return feedByAuthor.operate(profileRepository.findByUsername(value).orElseThrow(() -> new ProfileNotFoundException(value)), pageNumber, pageSize);
            case TOPIC:
                Page<Post> feedTags = feedByTag.operate(value, pageNumber, pageSize);
                return PagedModel.of(feedTags.stream().map(postModelAssembler::toModel).toList(), new PagedModel.PageMetadata(feedTags.getSize(), feedTags.getNumber(), feedTags.getTotalElements(), feedTags.getTotalPages()));
//            case COMMENTS:
//                return commentsByContent.operate(Long.parseLong(value), pageNumber, pageSize);
//            case REACTIONS:
//                return reactionsByContent.operate(Long.parseLong(value), pageNumber, pageSize);
            case CODE:
                List<String> languages = Arrays.asList(value.split(","));
                Page<Post> code = searchByCode.operate(languages, pageNumber, pageSize);
                return PagedModel.of(code.stream().map(postModelAssembler::toModel).toList(), new PagedModel.PageMetadata(code.getSize(), code.getNumber(), code.getTotalElements(), code.getTotalPages()));
            case PROFILES:
                Page<Profile> profiles = searchByName.operate(value, pageNumber, pageSize);
                return PagedModel.of(profiles.stream().map(profileModelAssembler::toModel).toList(), new PagedModel.PageMetadata(profiles.getSize(), profiles.getNumber(), profiles.getTotalElements(), profiles.getTotalPages()));
            case MUTUAL_FOLLOWING:
                Page<Profile> mutualFollowing = getFollowingofFollowing.operate(value, pageNumber, pageSize);
                return PagedModel.of(mutualFollowing.stream().map(profileModelAssembler::toModel).toList(), new PagedModel.PageMetadata(mutualFollowing.getSize(), mutualFollowing.getNumber(), mutualFollowing.getTotalElements(), mutualFollowing.getTotalPages()));
            default:
                return PagedModel.empty();
        }


    }

    public PagedModel<?> getTags(int pageNumber, int pageSize) {
        Page<Tag> tagsPage = topTags.operate("", pageNumber, pageSize);
        return PagedModel.of(tagsPage.stream().map(tagsAssembler::toModel).toList()
                , new PagedModel.PageMetadata(tagsPage.getSize(), tagsPage.getNumber(), tagsPage.getTotalElements(), tagsPage.getTotalPages()));
    }

    enum FeedType {
        ALL_USERS,
        //  ONE_USER,
        TOPIC,
        // COMMENTS,
        // REACTIONS,
        PROFILES,
        MUTUAL_FOLLOWING,
        CODE
    }
}
