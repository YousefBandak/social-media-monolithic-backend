package object_orienters.techspot.controller;

import object_orienters.techspot.exception.ChatNotFoundException;
import object_orienters.techspot.exception.ErrorResponse;
import object_orienters.techspot.exception.MessageNotFoundException;
import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Chat;
import object_orienters.techspot.model.Message;
import object_orienters.techspot.model.Profile;
import object_orienters.techspot.service.ImpleChatService;
import object_orienters.techspot.service.ImpleMessageService;
import object_orienters.techspot.service.ImpleProfileService;
import org.springframework.hateoas.EntityModel;
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
    MessageModelAssembler assembler;

    public MessageControler(ImpleMessageService messageService, ImpleChatService chatService, ImpleProfileService userService, MessageModelAssembler assembler) {
        this.messageService = messageService;
        this.chatService = chatService;
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping("/{userName}/inbox/chats/{chatId}/messages/{messageId}")
    public ResponseEntity<EntityModel<Message>> getMessage(@PathVariable String userName, @PathVariable Long chatId, @PathVariable Long messageId) {
        try {
            Profile user = userService.getUserByUsername(userName);
            Chat chat = chatService.getChat(chatId);
            Message message = messageService.getMessage(messageId);
            EntityModel<Message> messageModel = assembler.toModel(message);
            return ResponseEntity.ok(messageModel);

        } catch (UserNotFoundException | ChatNotFoundException | MessageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
        }
    }

    @GetMapping("/{userName}/inbox/chats/{chatId}/messages")
    public ResponseEntity<String> getMessages(@PathVariable String userName, @PathVariable Long chatId) {
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
    public ResponseEntity<?> deleteMessage(@PathVariable String userName, @PathVariable Long chatId, @PathVariable Long messageId) {

        try {
            Profile user  = userService.getUserByUsername(userName);
            Chat chat = chatService.getChat(chatId);
            Message message = messageService.deleteMessage(messageId);
            EntityModel<Message> messageModel = assembler.toModel(message);
            return ResponseEntity.ok(messageModel);

        } catch (UserNotFoundException | ChatNotFoundException | MessageNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PostMapping("/{userName}/inbox/chats/{chatId}/messages")
    public ResponseEntity<?> createMessage(@PathVariable String userName, @PathVariable Long chatId, @RequestBody Message message) {

        try {
            Profile user = userService.getUserByUsername(userName);
            Chat chat = chatService.getChat(chatId);
            Message createdMessage = messageService.createMessage(message);
            EntityModel<Message> messageModel = assembler.toModel(createdMessage);
            return ResponseEntity.status(HttpStatus.CREATED).body(messageModel);

        } catch (UserNotFoundException | ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
