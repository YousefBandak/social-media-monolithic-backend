package object_orienters.techspot.service;

import object_orienters.techspot.model.Chat;


import java.util.Set;


public interface ChatService {
    public Chat createChat(Chat chat);
    public Chat getChat(Long chatId);
    public Chat deleteChat(Long chatId);
    public Set<Chat> getAllChats(String userName);

}
