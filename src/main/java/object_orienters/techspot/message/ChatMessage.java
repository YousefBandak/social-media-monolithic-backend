package object_orienters.techspot.message;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {

    private String id;
    private String chatRoomId;
    private String senderId;
    private String recipientId;
    private String content;
    private LocalDateTime timestamp;
}
