package com.example.chatroomkafkabackenddbreactive.controller;

import com.example.chatroomkafkabackenddbreactive.event.ChatRoomMessageEvent;
import com.example.chatroomkafkabackenddbreactive.event.WordCountEvent;
import com.example.chatroomkafkabackenddbreactive.pojo.ChatRoomMessage;
import com.example.chatroomkafkabackenddbreactive.pojo.WordCount;
import com.example.chatroomkafkabackenddbreactive.service.KafkaConsumerService_MessagePerRoom;
import com.example.chatroomkafkabackenddbreactive.service.KafkaConsumerService_WordCount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
@Slf4j
public class ReactiveController {

    private final Sinks.Many<ChatRoomMessageEvent> sinkMessageData = Sinks.many().multicast().onBackpressureBuffer();
    private final Sinks.Many<WordCountEvent> sinkWordCountData = Sinks.many().multicast().onBackpressureBuffer();

    @EventListener
    public void onChangeChatRoomMessage(ChatRoomMessageEvent event) {
        log.info("from On Change Chat Room Message Event");
        sinkMessageData.tryEmitNext(event);
    }

    @EventListener
    public void onChangeWordCount(WordCountEvent event) {
        log.info("from On Word Count Event");
        sinkWordCountData.tryEmitNext(event);
    }

    @GetMapping(value = "/messageData", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<ChatRoomMessage>> getMessageEvent() {
        log.info("from On Change Chat Room Message Method");
        return sinkMessageData.asFlux().map(
                event -> ServerSentEvent.<ChatRoomMessage>builder(event.chatRoomMessage()).build());
    }

    @GetMapping(value = "/wordCountData", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<WordCount>> getWordCount() {
        log.info("from On Change Word Method");
        return sinkWordCountData.asFlux().map(
                event -> ServerSentEvent.<WordCount>builder(event.wordCount()).build());
    }
}
