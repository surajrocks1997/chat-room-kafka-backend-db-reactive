package com.example.chatroomkafkabackenddbreactive.service;

import com.example.chatroomkafkabackenddbreactive.dao.WordCountRepository;
import com.example.chatroomkafkabackenddbreactive.event.WordCountEvent;
import com.example.chatroomkafkabackenddbreactive.pojo.ChatRoomMessage;
import com.example.chatroomkafkabackenddbreactive.pojo.WordCount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@KafkaListener(topics = "chat-room-aggregator-word-count", groupId = "my-consumer-group", containerFactory = "messageListenerContainerKafkaListenerContainerFactory")
public class KafkaConsumerService_WordCount {

    private final ApplicationEventPublisher eventPublisher;
    private final WordCountRepository wordCountRepository;

    @KafkaHandler
    public void consumerWordCount(ConsumerRecord<String, Long> consumerRecord) {
        WordCount currentWordCount = wordCountRepository.findById(consumerRecord.key()).orElse(new WordCount(consumerRecord.key(), 0L));
        currentWordCount.setCount(currentWordCount.getCount() + Long.parseLong(String.valueOf(consumerRecord.value())));
        WordCount finalWordCount = wordCountRepository.save(currentWordCount);

        eventPublisher.publishEvent(new WordCountEvent(finalWordCount));
    }

    @KafkaHandler(isDefault = true)
    public void handleMessage(ConsumerRecord<String, Object> consumerRecord) {
        WordCount currentWordCount = wordCountRepository.findById(consumerRecord.key()).orElse(new WordCount(consumerRecord.key(), 0L));
        currentWordCount.setCount(currentWordCount.getCount() + Long.parseLong(String.valueOf(consumerRecord.value())));
        WordCount finalWordCount = wordCountRepository.save(currentWordCount);

        eventPublisher.publishEvent(new WordCountEvent(finalWordCount));
    }

}
