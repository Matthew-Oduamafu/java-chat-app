package io.nerdbyteslns.chatapp.chat;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findAllByChatId(String chatId);

    List<ChatMessage> findAllBySenderIdAndRecipientId(String senderId, String recipientId);
}
