package object_orienters.techspot.message;


import object_orienters.techspot.message.Message;
import org.springframework.stereotype.Service;

import java.util.List;

public interface MessageService {
    public Message createMessage(Message message);
    public Message deleteMessage(Long MessageId);
    public Message getMessage(Long MessageId);
    public List<Message> getAllMessage(Long chatId);



}
