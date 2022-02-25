package com.demo.kafka.play.consumer;

import com.demo.kafka.Person;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.streams.StreamsConfig;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static io.confluent.kafka.serializers.KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

  private final KafkaProperties kafkaProperties;

  public KafkaConsumerConfig(KafkaProperties kafkaProperties) {
    this.kafkaProperties = kafkaProperties;
  }

 /* @Bean
  public Map<String, Object> consumerConfigs() {
    Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, "group_id");
    return props;
  }*/

  // string
  @Bean
  public ConsumerFactory<String, String> stringConsumerFactory() {
    return new DefaultKafkaConsumerFactory<>(
        kafkaProperties.buildConsumerProperties(), new StringDeserializer(), new StringDeserializer()
    );
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, String> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(stringConsumerFactory());

    return factory;
  }

  @Bean
  public ConsumerFactory<String, Person> personConsumerFactory() {
    Map<String, Object> props = new HashMap<>(kafkaProperties.buildConsumerProperties());
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9091");
    props.put(SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081");
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
    props.put(SPECIFIC_AVRO_READER_CONFIG, true);
    return new DefaultKafkaConsumerFactory<>(props);
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, Person> personKafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, Person> factory =
        new ConcurrentKafkaListenerContainerFactory<>();
    factory.setConsumerFactory(personConsumerFactory());

    return factory;
  }

}
