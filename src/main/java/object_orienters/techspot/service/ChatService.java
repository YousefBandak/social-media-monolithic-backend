package object_orienters.techspot.service;

import object_orienters.techspot.exception.UserNotFoundException;
import object_orienters.techspot.model.Chat;
import object_orienters.techspot.model.User;


import java.util.Set;


public interface ChatService {
    public Chat createChat(Chat chat);
    public Chat getChat(Long chatId);
    public String deleteChat(Long chatId);
    public Set<Chat> getAllChats(String userName);
    public User getUserByUsername(String userName) throws UserNotFoundException;


}
