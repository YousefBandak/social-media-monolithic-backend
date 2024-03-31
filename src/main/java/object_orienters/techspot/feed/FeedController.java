package object_orienters.techspot.feed;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FeedController {
    private final FeedService feedService;

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/feed")
    public ResponseEntity<?> feed(@RequestParam(defaultValue = "ALL_USERS") String feedType, @RequestParam(defaultValue = "following") String value, @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") int limit, @RequestBody ObjectNode ClientUsername) {

        return ResponseEntity.ok(feedService.feedContent(FeedService.FeedType.valueOf(feedType), value, offset, limit, ClientUsername.get("username").asText()));
    }
}
