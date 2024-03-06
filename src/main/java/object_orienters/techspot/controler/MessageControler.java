package object_orienters.techspot.controler;

import object_orienters.techspot.model.Message;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class MessageControler {
    @GetMapping("/{userName}/inbox/chats/{chatId}/messages/{messageId}")
    public String getSpecificMessageForSpecificChat(@PathVariable String userName, @PathVariable Long chatId, @PathVariable Long messageId) {
        return "Retrieving message with id " + messageId + " for user " + userName + " in chat " + chatId;
    }
    @GetMapping("/{userName}/inbox/chats/{chatId}/messages")
    public String getAllMessagesForSpecificChat(@PathVariable String userName, @PathVariable Long chatId, @PathVariable Long messageId) {
        return "Retrieving message with id " + messageId + " for user " + userName + " in chat " + chatId;
    }
    @PostMapping("/{userName}/inbox/chats/{chatId}/messages")
    public Message createMessage(@PathVariable String userName, @PathVariable Long chatId, @RequestBody String messageContent) {

        return null;
    }
    @DeleteMapping("/{userName}/inbox/chats/{chatId}/messages/{messageId}")
    public String deleteMessage(@PathVariable String userName, @PathVariable Long chatId, @PathVariable Long messageId) {
        return "Deleted message with id " + messageId + " for user " + userName + " in chat " + chatId;
    }
}
