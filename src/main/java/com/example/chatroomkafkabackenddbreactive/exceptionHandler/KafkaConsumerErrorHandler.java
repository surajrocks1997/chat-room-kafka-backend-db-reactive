package com.example.chatroomkafkabackenddbreactive.exceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.RecordDeserializationException;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.MessageListenerContainer;

@Slf4j
public class KafkaConsumerErrorHandler implements CommonErrorHandler {


    @Override
    public void handleOtherException(Exception thrownException, Consumer<?, ?> consumer, MessageListenerContainer container, boolean batchListener) {
        log.info("FROM HANDLE OTHER EXCEPTION");
        handle(thrownException, consumer);
    }

    @Override
    public boolean handleOne(Exception thrownException, ConsumerRecord<?, ?> record, Consumer<?, ?> consumer, MessageListenerContainer container) {
        handle(thrownException, consumer);
        log.info("FROM HANDLE ONE");
        return this.seeksAfterHandling();
    }

    private void handle(Exception exception, Consumer<?, ?> consumer) {
        log.error("Exception Thrown: ", exception);

        if (exception instanceof RecordDeserializationException ex) {
            consumer.seek(ex.topicPartition(), ex.offset() + 1L);
            consumer.commitSync();
        } else {
            log.error("Exception not handled: ", exception);
        }
    }
}
