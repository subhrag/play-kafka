package com.demo.kafka.play.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class Producer {

  private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
  private static final String TOPIC = "users";

  private final KafkaTemplate<String, Object> kafkaTemplate;

  public Producer(KafkaTemplate<String, Object> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMessage(String message) {
    ListenableFuture send = kafkaTemplate.send(TOPIC, message);
    LOGGER.info(String.format("#### -> Sent message -> %s", message));
  }

}
