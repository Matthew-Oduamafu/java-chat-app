package io.nerdbyteslns.chatapp.chat;


import io.nerdbyteslns.chatapp.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    public ChatMessage saveMessage(ChatMessage chatMessage) {
        var chatId = chatRoomService
                .getChatRoomId(chatMessage.getSenderId(), chatMessage.getRecipientId(), true);
        chatMessage.setChatId(chatId.orElseThrow());
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findMessagesByChatId(String chatId) {
        // find messages by chat id
        return chatMessageRepository.findAllByChatId(chatId);
    }

    public List<ChatMessage> findMessages(String senderId, String recipientId) {
        var chatId = chatRoomService
                .getChatRoomId(senderId, recipientId, false);
        return chatId.map(chatMessageRepository::findAllByChatId)   // if chatId is present, find messages by chat id
                .orElseGet(List::of);   // if chatId is not present, return empty list}
    }
}