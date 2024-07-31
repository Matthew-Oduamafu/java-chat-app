package io.nerdbyteslns.chatapp.chatroom;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;


    public Optional<String> getChatRoomId(String senderId, String recipientId, boolean createIfNotExist) {
        // get chat room id
        Optional<ChatRoom> chatRoom = chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId);
        if (chatRoom.isPresent()) {
            return Optional.of(chatRoom.get().getChatId());
        } else if (createIfNotExist) {
            return Optional.of(createChatId(senderId, recipientId));
        }
        return Optional.empty();
    }

    private String createChatId(String senderId, String recipientId) {
        // create sender and recipient chatRoom
        ChatRoom senderRecipient = ChatRoom.builder()
                .senderId(senderId)
                .recipientId(recipientId)
                .chatId(String.format("%s_%s", senderId, recipientId))
                .build();
        chatRoomRepository.save(senderRecipient);

        ChatRoom recipientSender = ChatRoom.builder()
                .senderId(recipientId)
                .recipientId(senderId)
                .chatId(String.format("%s_%s", recipientId, senderId))
                .build();
        chatRoomRepository.save(recipientSender);

        return senderRecipient.getChatId();
    }
}
