package com.example.chatroomkafkabackenddbreactive.controller;

import com.example.chatroomkafkabackenddbreactive.event.ChatRoomMessageEvent;
import com.example.chatroomkafkabackenddbreactive.event.WordCountEvent;
import com.example.chatroomkafkabackenddbreactive.pojo.ChatRoomMessage;
import com.example.chatroomkafkabackenddbreactive.pojo.WordCount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.cors.CorsConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sse")
@CrossOrigin(CorsConfiguration.ALL)
@Slf4j
public class ReactiveController {

    private final Sinks.Many<ChatRoomMessageEvent> sinkMessageData = Sinks.many().multicast().directAllOrNothing();
    private final Sinks.Many<WordCountEvent> sinkWordCountData = Sinks.many().multicast().directAllOrNothing();

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
        return sinkMessageData.asFlux().map(event -> ServerSentEvent.<ChatRoomMessage>builder()
                .id(String.valueOf(System.currentTimeMillis()))
                .event("messageEvent")
                .data(event.chatRoomMessage())
                .retry(Duration.ofMillis(200))
                .build());

        //        return sinkMessageData.asFlux().map(
//                event -> ServerSentEvent.<ChatRoomMessage>builder(event.chatRoomMessage()).build());
    }

    @GetMapping(value = "/wordCountData", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<WordCount>> getWordCount() {
        log.info("from On Change Word Method");

        return sinkWordCountData.asFlux().map(event -> ServerSentEvent.<WordCount>builder()
                .id(String.valueOf(System.currentTimeMillis()))
                .event("wordCountEvent")
                .data(event.wordCount())
                .retry(Duration.ofMillis(200))
                .build());

//        return sinkWordCountData.asFlux().map(
//                event -> ServerSentEvent.<WordCount>builder(event.wordCount()).build());
    }
}
