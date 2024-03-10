package object_orienters.techspot.controler;

import object_orienters.techspot.exception.ChatNotFoundException;
import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Message;
import object_orienters.techspot.repository.UserRepository;
import object_orienters.techspot.service.ImpleChatService;
import object_orienters.techspot.service.ImpleMessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class MessageControler {
    ImpleMessageService messageService;
    ImpleChatService chatService;
    UserRepository userRepository;

    @GetMapping("/{userName}/inbox/chats/{chatId}/messages/{messageId}")
    public String getSpecificMessageForSpecificChat(@PathVariable String userName, @PathVariable Long chatId, @PathVariable Long messageId) {
        return "Retrieving message with id " + messageId + " for user " + userName + " in chat " + chatId;
    }
    @GetMapping("/{userName}/inbox/chats/{chatId}/messages")
    public String getAllMessagesForSpecificChat(@PathVariable String userName, @PathVariable Long chatId, @PathVariable Long messageId) {
        return "Retrieving message with id " + messageId + " for user " + userName + " in chat " + chatId;
    }

    @DeleteMapping("/{userName}/inbox/chats/{chatId}/messages/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable String userName, @PathVariable Long chatId, @PathVariable Long messageId) {

        try {
            chatService.getUserByUsername(userName);
            chatService.getChat(chatId);
            String deletionResult = messageService.deleteMessage(messageId);
            if (deletionResult.equals("Message deleted successfully.")) {
                return ResponseEntity.ok("Deleted message with id " + messageId + " for user " + userName + " in chat " + chatId);
            }else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found.");
            }
        } catch (UserNotFoundException | ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

    }



    @PostMapping("/{userName}/inbox/chats/{chatId}/messages")
    public ResponseEntity<String> createMessage(@PathVariable String userName, @PathVariable Long chatId, @RequestBody Message message) {

        try {
            chatService.getUserByUsername(userName);
            chatService.getChat(chatId);
            messageService.createMessage(message);
            return ResponseEntity.status(HttpStatus.CREATED).body("Message created successfully.");

        }catch (UserNotFoundException | ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
