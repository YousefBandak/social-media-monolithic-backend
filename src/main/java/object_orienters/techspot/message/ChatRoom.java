package object_orienters.techspot.message;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatRoom {
    private String id;
    private String chatId;
    private String senderId;
    private String recipientId;

    public void setId(String id) {
        this.id = id;
    }
}
