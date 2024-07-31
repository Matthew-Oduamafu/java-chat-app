package io.nerdbyteslns.chatapp.schedule;


import io.nerdbyteslns.chatapp.chat.ChatMessage;
import io.nerdbyteslns.chatapp.chat.ChatNotification;
import io.nerdbyteslns.chatapp.chatroom.ChatRoom;
import io.nerdbyteslns.chatapp.chatroom.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
@RequiredArgsConstructor
@EnableAsync
public class Scheduler {

    private final ChatRoomRepository chatRoomRepository;
    private final SimpMessagingTemplate messagingTemplate;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mma EEE. dd MMM, yyyy");

    @Async
    @Scheduled(fixedRate = 1, timeUnit = TimeUnit.SECONDS)
    public void scheduleTask() throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        String formatDateTime = now.format(formatter);

        log.info("Scheduler executed at: {}", formatDateTime);
    }

    @Async
    @Scheduled(cron = "*/2 * * * * *")
    public void scheduleTaskCron() throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        String formatDateTime = now.format(formatter);

        log.info("Scheduled cron for 2s executed at: {}", formatDateTime);
    }

    @Async
    @Scheduled(cron = "0 */2 * * * *")
    public void scheduleTaskCron2minutes() throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        String formatDateTime = now.format(formatter);

        log.info("Scheduled cron for 2 minutes executed at: {}", formatDateTime);
    }

    @Async
    @Scheduled(cron = "${cron.expression.value}")
    public void scheduleTaskCron8pmTuesdays() throws InterruptedException {
        LocalDateTime now = LocalDateTime.now();
        String formatDateTime = now.format(formatter);

        log.info("Scheduled cron for 8pm Tuesdays executed at: {}", formatDateTime);
    }

    // using chatRoomRepository and messagingTemplate find any chat rooms and send a message to the recipient
    @Scheduled(fixedRate = 3, timeUnit = TimeUnit.SECONDS)
    public void sendScheduledMessage() {
        // Retrieve all chat rooms
        List<ChatRoom> chatRooms = chatRoomRepository.findAll();

        // Iterate through each chat room
        for (ChatRoom chatRoom : chatRooms) {
            // Create a message
            ChatMessage message = ChatMessage.builder()
                    .senderId(chatRoom.getSenderId())
                    .recipientId(chatRoom.getRecipientId())
                    .content("Scheduled message at " + LocalDateTime.now().format(formatter))
                    .timestamp(LocalDateTime.now().format(formatter))
                    .build();

            // Send the message to the recipient
            messagingTemplate.convertAndSendToUser(
                    chatRoom.getRecipientId(), "/queue/messages",
                    ChatNotification.builder()
                            .id(message.getId())
                            .senderId(message.getSenderId())
                            .recipientId(message.getRecipientId())
                            .content(message.getContent())
                            .timestamp(message.getTimestamp())
                            .build());
        }
    }
}
