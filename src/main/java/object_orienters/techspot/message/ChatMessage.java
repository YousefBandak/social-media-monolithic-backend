package object_orienters.techspot.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;

import java.time.LocalDateTime;

// Your imports for @Getter, @Setter, @AllArgsConstructor, @NoArgsConstructor, @Builder

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private static int count = 0;
    private int id;
    private String chatRoomId;
    private String senderId;
    private String recipientId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String timestamp;

    // Add a static ObjectMapper field for reuse across instances of ChatMessage
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    // Getter for the objectMapper field
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    // Serialize method to convert ChatMessage object to JSON string
    public String toJsonString() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }

    // Deserialize method to convert JSON string to ChatMessage object
    public static ChatMessage fromJsonString(String jsonString) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, ChatMessage.class);
    }

    public static int incrementAndGet() {
        return count++;
    }
}
