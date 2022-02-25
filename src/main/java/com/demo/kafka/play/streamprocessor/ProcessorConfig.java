package com.demo.kafka.play.streamprocessor;

import com.demo.kafka.play.producer.Producer;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;

import java.util.Properties;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

@Configuration
public class ProcessorConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorConfig.class);

  @Bean
  Properties getProperties() {
    LOGGER.info("In Processor config");
    Properties properties = new Properties();
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-test");
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9091");
    properties.put(SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    //    properties.put(StreamsConfig.STATE_DIR_CONFIG, TestUtils.tempDirectory().getAbsolutePath());

    return properties;
  }

}
