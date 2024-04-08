package object_orienters.techspot.feed;

import com.fasterxml.jackson.databind.node.ObjectNode;

import object_orienters.techspot.content.Content;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class FeedController {
    private final FeedService feedService;
    private static final Logger logger = LoggerFactory.getLogger(FeedController.class);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping("/feed")
    //@PreAuthorize("@ClientUsername.get(\"username\").asText() == authentication.principal.username")
    public ResponseEntity<?> feed(@RequestParam(defaultValue = "ALL_USERS") String feedType,
            @RequestParam(defaultValue = "following") String value, @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit, @RequestBody ObjectNode ClientUsername) {
        logger.info(">>>>Loading Feed... @ " + getTimestamp() + "<<<<");
        List<Content> feed = feedService.feedContent(FeedService.FeedType.valueOf(feedType), value, offset, limit,
                ClientUsername.get("username").asText());
        logger.info(">>>> Feed Loaded Successfully... @ " + getTimestamp() + "<<<<");
        return ResponseEntity.ok(feed);
    }

    private static String getTimestamp() {
        return LocalDateTime.now().format(formatter) + " ";
    }
}
