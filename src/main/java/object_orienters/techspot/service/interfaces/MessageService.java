package object_orienters.techspot.service.interfaces;


import object_orienters.techspot.model.Message;

import java.util.List;


public interface MessageService {
    public Message createMessage(Message message);
    public Message deleteMessage(Long MessageId);
    public Message getMessage(Long MessageId);
    public List<Message> getAllMessage(Long chatId);



}
