package object_orienters.techspot.chat;

import object_orienters.techspot.chat.Chat;


import java.util.Set;


public interface ChatService {
    public Chat createChat(Chat chat);
    public Chat getChat(Long chatId);
    public Chat deleteChat(Long chatId);
    public Set<Chat> getAllChats(String userName);

}
