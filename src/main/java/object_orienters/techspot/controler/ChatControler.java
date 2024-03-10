package object_orienters.techspot.controler;

import object_orienters.techspot.exception.ChatAlreadyExistsException;
import object_orienters.techspot.exception.ChatNotFoundException;
import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Chat;
import object_orienters.techspot.model.User;
import object_orienters.techspot.service.ImpleChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/profiles")
public class ChatControler {
    ImpleChatService chatService;

    public ChatControler(ImpleChatService chatService) {
        this.chatService = chatService;
    }


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
    public ResponseEntity<Chat> getSpecificChat(@PathVariable String userName, @PathVariable Long chatId) {
        try {

            User user = chatService.getUserByUsername(userName);
            Chat specificChat = chatService.getChat(chatId);

            // Check if the user is part of the chat
            if (!specificChat.getSender().equals(user) && !specificChat.getReceiver().equals(user)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

            return ResponseEntity.ok(specificChat);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/{userName}/inbox/chats")
    public ResponseEntity<String> createChat(@PathVariable String userName, @RequestBody Chat newChat) {
        try {

            User sender = chatService.getUserByUsername(userName);
            User receiver = newChat.getReceiver();

            return ResponseEntity.status(HttpStatus.CREATED).body("Chat created successfully with ID: " + newChat.getChatId());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        } catch (ChatAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Chat creation failed: " + e.getMessage());
        }
    }
    @DeleteMapping("/{userName}/inbox/chats/{chatId}")
    public ResponseEntity<String> deleteChat(@PathVariable String userName, @PathVariable Long chatId) {
        try {

            User user = chatService.getUserByUsername(userName);
            chatService.deleteChat(chatId);
            return ResponseEntity.ok("Chat deleted successfully");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found: " + e.getMessage());
        } catch (ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chat not found: " + e.getMessage());
        }
    }


}
