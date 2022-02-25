package com.demo.kafka.play.producer;

import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
public class KafkaProducerConfig {

  private final KafkaProperties kafkaProperties;

  public KafkaProducerConfig(KafkaProperties kafkaProperties) {
    this.kafkaProperties = kafkaProperties;
  }

  @Bean
  public Map<String, Object> producerConfigs() {
    Map<String, Object> props = new HashMap<>(kafkaProperties.buildProducerProperties());
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9091");
    props.put(SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081");
    props.put(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    props.put(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
    return props;
  }

  @Bean
  public ProducerFactory<String, Object> producerFactory() {
    return new DefaultKafkaProducerFactory<>(producerConfigs());
  }

  @Bean
  public KafkaTemplate<String, Object> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }

}
