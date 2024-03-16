package object_orienters.techspot.chat;

import object_orienters.techspot.profile.UserNotFoundException;
import object_orienters.techspot.profile.Profile;
import object_orienters.techspot.profile.ProfileController;
import object_orienters.techspot.profile.ImpleProfileService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.problem.Problem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/profiles")
public class ChatController {
    ImpleChatService chatService;
    ImpleProfileService userService;
    ChatModelAssembler assembler;

    public ChatController(ImpleChatService chatService, ImpleProfileService userService, ChatModelAssembler assembler) {
        this.chatService = chatService;
        this.userService = userService;
        this.assembler = assembler;
    }

    @GetMapping("/{userName}/inbox/chats/{chatId}")
    public ResponseEntity<?> getChat(@PathVariable String userName, @PathVariable Long chatId) {
        try {
            Profile user = userService.getUserByUsername(userName);
            Chat chat = chatService.getChat(chatId);
            EntityModel<Chat> chatModel = assembler.toModel(chat);
            return ResponseEntity.ok(chatModel);
        } catch (UserNotFoundException | ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }

    @PostMapping("/{userName}/inbox/chats")
    public ResponseEntity<?> createChat(@PathVariable String userName, @RequestBody Chat newChat) {
        try {
            Profile user = userService.getUserByUsername(userName);
            Chat createdChat = chatService.createChat(newChat);
            EntityModel<Chat> chatModel = assembler.toModel(createdChat);
            return ResponseEntity.status(HttpStatus.CREATED).body(chatModel);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        } catch (ChatAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Problem.create().withTitle("Chat Already Exists").withDetail(e.getMessage()));
        }
    }

    @DeleteMapping("/{userName}/inbox/chats/{chatId}")
    public ResponseEntity<?> deleteChat(@PathVariable String userName, @PathVariable Long chatId) {
        try {
            Profile user = userService.getUserByUsername(userName);
            Chat deletedChat = chatService.deleteChat(chatId);
            EntityModel<Chat> chatModel = assembler.toModel(deletedChat);
            return ResponseEntity.ok(chatModel);
        } catch (UserNotFoundException | ChatNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }

    @GetMapping("/{userName}/chats")
    public ResponseEntity<?> getChats(@PathVariable String userName) {
        // it should return ResponseEntity<CollectionModel<EntityModel<Chat>>>
        try {
            Set<Chat> chats = chatService.getAllChats(userName);
            if (chats.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            CollectionModel<EntityModel<Chat>> chatModels = CollectionModel.of(
                    chats.stream()
                            .map(assembler::toModel)
                            .collect(Collectors.toList()),
                    linkTo(methodOn(ChatController.class).getChats(userName)).withSelfRel(),
                    linkTo(methodOn(ProfileController.class).one(userName)).withRel("profile")
            );
            return ResponseEntity.ok(chatModels);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Problem.create().withTitle("Not Found").withDetail(e.getMessage()));
        }
    }

}
