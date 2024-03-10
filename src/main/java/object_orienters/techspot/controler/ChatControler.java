package object_orienters.techspot.controler;

import object_orienters.techspot.exception.ChatAlreadyExistsException;
import object_orienters.techspot.exception.ChatNotFoundException;
import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Chat;
import object_orienters.techspot.model.User;
import object_orienters.techspot.service.ImpleChatService;
import object_orienters.techspot.service.ImpleUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/profiles")
public class ChatControler {
    ImpleChatService chatService;
    ImpleUserService userService;

    public ChatControler(ImpleChatService chatService, ImpleUserService userService) {
        this.chatService = chatService;
        this.userService = userService;
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
    public ResponseEntity<String> getSpecificChat(@PathVariable String userName, @PathVariable Long chatId) {
        try {

            User user = userService.getUserByUsername(userName);
            Chat chat = chatService.getChat(chatId);
            return ResponseEntity.ok(chat.toString());

        } catch (UserNotFoundException | ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{userName}/inbox/chats")
    public ResponseEntity<String> createChat(@PathVariable String userName, @RequestBody Chat newChat) {
        try {

            userService.getUserByUsername(userName);
            chatService.createChat(newChat);
            return ResponseEntity.status(HttpStatus.CREATED).body("Chat created successfully with ID: " + newChat.getChatId());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ChatAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
    @DeleteMapping("/{userName}/inbox/chats/{chatId}")
    public ResponseEntity<String> deleteChat(@PathVariable String userName, @PathVariable Long chatId) {
        try {

            User user = userService.getUserByUsername(userName);
            chatService.deleteChat(chatId);
            return ResponseEntity.ok("Chat deleted successfully");
        } catch (UserNotFoundException | ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


}
