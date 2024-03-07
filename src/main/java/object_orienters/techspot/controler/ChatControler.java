package object_orienters.techspot.controler;

import object_orienters.techspot.model.Chat;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/profiles")
public class ChatControler {
    /////////////////////////////////////////////
    // Does this methods the same??????????????
    @GetMapping("/{userName}/inbox/chats")
    public Set<Chat> getAllChats(@PathVariable String userName) {
        return null;
    }
    // I think it's should return Inbox object
    @GetMapping("/{userName}/inbox")
    public Set<Chat> getInbox(@PathVariable String userName) {return null;}
    /////////////////////////////////////////////
    @GetMapping("/{userName}/inbox/chats/{chatId}")
    public Chat getSpecificChat(@PathVariable String userName) {
        return null;
    }

    @PostMapping("/{userName}/inbox/chats")
    public String createChat(@PathVariable String userName, @RequestBody Chat newChat) {return null;}
    @DeleteMapping("/{userName}/inbox/chats/{chatId}")
    public String deleteChat(@PathVariable String userName, @PathVariable Long chatId) {return null;}


}
