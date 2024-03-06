package object_orienters.techspot.controler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profiles")
public class ChatControler {
    @GetMapping("/{userName}/inbox/chats/{chatId}/messages/{messageId}")
    public String getSpicifecMessage(@PathVariable String userName, @PathVariable Long chatId, @PathVariable Long messageId) {
        return "Retrieving message with id " + messageId + " for user " + userName + " in chat " + chatId;
    }
    @GetMapping("/{userName}/inbox/chats/{chatId}/messages/{messageId}")
    public String getAllMessages(@PathVariable String userName, @PathVariable Long chatId, @PathVariable Long messageId) {
        return "Retrieving message with id " + messageId + " for user " + userName + " in chat " + chatId;
    }

    @GetMapping("/{userName}/inbox/chats")
    public String getAllChats(@PathVariable String userName) {
        return "Fetching chats for user: " + userName;
    }
    @GetMapping("/{userName}/inbox/chats/{chatId}")
    public String getSpicifecChat(@PathVariable String userName) {
        return "Fetching chats for user: " + userName;
    }

}
