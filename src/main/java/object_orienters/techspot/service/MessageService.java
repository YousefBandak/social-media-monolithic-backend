package object_orienters.techspot.service;

import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Chat;
import object_orienters.techspot.model.Message;
import object_orienters.techspot.model.User;

import java.util.List;
import java.util.Set;

public interface MessageService {
    public Message createMessage(Message message);
    public String deleteMessage(Long MessageId);
    public Message getMessage(Long MessageId);
    public List<Message> getAllMessage(Long chatId);



}
