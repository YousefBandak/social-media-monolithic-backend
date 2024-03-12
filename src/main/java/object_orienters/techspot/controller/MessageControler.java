package object_orienters.techspot.controller;

import object_orienters.techspot.exception.ChatNotFoundException;
import object_orienters.techspot.exception.MessageNotFoundException;
import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Chat;
import object_orienters.techspot.model.Message;
import object_orienters.techspot.service.ImpleChatService;
import object_orienters.techspot.service.ImpleMessageService;
import object_orienters.techspot.service.ImpleProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles")
public class MessageControler {
    ImpleMessageService messageService;
    ImpleChatService chatService;
    ImpleProfileService userService;

    @GetMapping("/{userName}/inbox/chats/{chatId}/messages/{messageId}")
    public ResponseEntity<String> getSpecificMessageForSpecificChat(@PathVariable String userName, @PathVariable Long chatId, @PathVariable Long messageId) {
        try {
            userService.getUserByUsername(userName);
            chatService.getChat(chatId);
            Message message = messageService.getMessage(messageId);
            return ResponseEntity.ok(message.toString());

        }catch (UserNotFoundException | ChatNotFoundException | MessageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{userName}/inbox/chats/{chatId}/messages")
    public ResponseEntity<String> getAllMessagesForSpecificChat(@PathVariable String userName, @PathVariable Long chatId) {
        try {
            userService.getUserByUsername(userName);
            Chat chat = chatService.getChat(chatId);
            List<Message> messages = chat.getMessages();
            return ResponseEntity.ok(messages.toString());

        } catch (UserNotFoundException | ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{userName}/inbox/chats/{chatId}/messages/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable String userName, @PathVariable Long chatId, @PathVariable Long messageId) {

        try {
            userService.getUserByUsername(userName);
            chatService.getChat(chatId);
            String deletionResult = messageService.deleteMessage(messageId);
            return ResponseEntity.ok(deletionResult);

        } catch (UserNotFoundException | ChatNotFoundException | MessageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }



    @PostMapping("/{userName}/inbox/chats/{chatId}/messages")
    public ResponseEntity<String> createMessage(@PathVariable String userName, @PathVariable Long chatId, @RequestBody Message message) {

        try {
            userService.getUserByUsername(userName);
            chatService.getChat(chatId);
            messageService.createMessage(message);
            return ResponseEntity.status(HttpStatus.CREATED).body("Message created successfully.");

        }catch (UserNotFoundException | ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    }
