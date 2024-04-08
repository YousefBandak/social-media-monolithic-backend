package object_orienters.techspot.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.*;

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

    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public String toJsonString() throws JsonProcessingException {
        return objectMapper.writeValueAsString(this);
    }

    public static ChatMessage fromJsonString(String jsonString) throws JsonProcessingException {
        return objectMapper.readValue(jsonString, ChatMessage.class);
    }

    public static int incrementAndGet() {
        return count++;
    }
}
