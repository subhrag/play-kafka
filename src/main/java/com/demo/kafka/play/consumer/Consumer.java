package com.demo.kafka.play.consumer;

import com.demo.kafka.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class Consumer {

  private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

  @KafkaListener(topics = "users", groupId = "group_id"/*, containerFactory = "kafkaListenerContainerFactory"*/)
  public void consume(String message) {
    LOGGER.info(String.format("#### -> Consumed message -> %s", message));
  }

  @KafkaListener(topics = "user", groupId = "group_id", containerFactory = "personKafkaListenerContainerFactory")
  public void consume1(Person person) {

    LOGGER.info(String.format("#### -> Consumed person -> %s", person));
  }

}
