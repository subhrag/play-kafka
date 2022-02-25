package com.demo.kafka.play.stream;

import com.demo.kafka.Person;
import io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

//@EnableKafkaStreams
@Configuration
public class Processor1 {

  private static final Logger LOGGER = LoggerFactory.getLogger(Processor1.class);

  @Bean
  Properties getProperties() {
    Properties properties = new Properties();
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-test");
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9091");
    properties.put(AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081");
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    //    properties.put(StreamsConfig.STATE_DIR_CONFIG, TestUtils.tempDirectory().getAbsolutePath());

    return properties;
  }

  @Bean
  Topology doSomething() {
    StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> stream =
        builder.stream("users", Consumed.with(new Serdes.StringSerde(), new Serdes.StringSerde()));

    SpecificAvroSerde<Person> personSpecificAvroSerde = new SpecificAvroSerde<>();
    Map<String, String> config = new HashMap<>();
    config.put(SCHEMA_REGISTRY_URL_CONFIG, "http://schema-registry:8081");
    config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "http://localhost:9091");

    personSpecificAvroSerde.configure(config, false);

    stream.mapValues((key, value) -> Person.newBuilder().setFirstName(value).setLastName("").setAge(10).build())
        .to("user", Produced.with(new Serdes.StringSerde(), personSpecificAvroSerde));

    Topology topology = builder.build();
    KafkaStreams kafkaStreams = new KafkaStreams(topology, getProperties());
    LOGGER.info("Starting stream...");
    kafkaStreams.start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      LOGGER.info("Shutting down stream");
      kafkaStreams.close();
    }));
    return topology;
  }

}
