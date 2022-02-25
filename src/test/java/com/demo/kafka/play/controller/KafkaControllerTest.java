package com.demo.kafka.play.controller;

import com.demo.kafka.play.producer.Producer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(KafkaController.class)
public class KafkaControllerTest {

  private static final String URL = "/kafka/publish";

  @MockBean private Producer producer;

  @Autowired private MockMvc mockMvc;

  @Test
  void publish() throws Exception {
    mockMvc.perform(post(URL).param("message", "hello")).andExpect(status().isOk());
  }

}

