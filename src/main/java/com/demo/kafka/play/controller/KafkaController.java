package com.demo.kafka.play.controller;

import com.demo.kafka.play.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kafka")
public class KafkaController {

  private static final Logger LOGGER = LoggerFactory.getLogger(KafkaController.class);
  private final Producer producer;

  public KafkaController(Producer producer) {
    this.producer = producer;
  }

  @PostMapping(value = "/publish")
  public ResponseEntity sendMessageToKafkaTopic(
      @RequestParam(name = "message", required = false, defaultValue = "shell") String message
  ) {
    this.producer.sendMessage(message);
    return new ResponseEntity(HttpStatus.OK);
  }

}
