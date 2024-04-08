package object_orienters.techspot.message;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatNotification {
    private int id;
    private String senderId;
    private String recipientId;
    private String content;

}
