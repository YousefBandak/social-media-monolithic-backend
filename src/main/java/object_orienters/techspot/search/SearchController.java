package object_orienters.techspot.search;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import object_orienters.techspot.profile.Profile;

@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<?> Search(
            @RequestBody String keyword,
            @RequestParam SearchType type,
            @RequestParam int offset,
            @RequestParam int limit) {
        List<Profile> users = searchService.search(keyword, type, offset, limit);
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(users);
        }

    }
}
