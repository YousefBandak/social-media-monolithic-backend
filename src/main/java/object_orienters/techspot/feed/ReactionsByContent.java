package object_orienters.techspot.feed;

import object_orienters.techspot.content.ReactableContentRepository;
import object_orienters.techspot.exceptions.ContentNotFoundException;
import object_orienters.techspot.exceptions.PostNotFoundException;
import object_orienters.techspot.reaction.Reaction;
import object_orienters.techspot.reaction.ReactionRepository;
import object_orienters.techspot.utilities.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class ReactionsByContent extends Strategy<Reaction, Long> {
    private final PermissionService permissionService;
    @Autowired
    public ReactionsByContent(ReactableContentRepository reactableContentRepository, ReactionRepository reactionRepository, PermissionService permissionService) {
        this.reactableContentRepository = reactableContentRepository;
        this.reactionRepository = reactionRepository;
        this.permissionService = permissionService;
    }

    public Page<Reaction> operate(Long contentID, int pageNumber, int pageSize) {
        String currentUserPrincipal = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            if (!permissionService.canAccessPost(contentID, currentUserPrincipal))
                return Page.empty();
        } catch (PostNotFoundException e) {
            throw new ContentNotFoundException(contentID);
        }
        return reactionRepository.findByContent(reactableContentRepository.findByContentID(contentID).orElseThrow(() -> new ContentNotFoundException(contentID)), PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").descending()));
    }

}