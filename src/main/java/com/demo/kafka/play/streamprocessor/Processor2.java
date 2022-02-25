package com.demo.kafka.play.streamprocessor;

import com.demo.kafka.play.KafkaProperties;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
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
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Properties;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;

@Service
public class Processor2 {
  private static final Logger LOGGER = LoggerFactory.getLogger(Processor2.class);
  private static final String TOPIC = "users";

  private final ProcessorConfig processorConfig;
  private final KafkaProperties kafkaProperties;

  public Processor2(KafkaProperties kafkaProperties, ProcessorConfig processorConfig) {
    this.processorConfig = processorConfig;
    this.kafkaProperties = kafkaProperties;
  }

  @Bean
  public Topology getProcessedData() {
    LOGGER.info("Starting processing ..");
    String inputTopic = TOPIC;
    Properties properties = processorConfig.getProperties();
    StreamsBuilder streamsBuilder = new StreamsBuilder();

    Serde<Person> rawUserSpecificAvroSerde = new SpecificAvroSerde<>();
    Serde<Person> processedUserSpecificAvroSerde = new SpecificAvroSerde<>();

    Map<String, String> config = Map.of(
        SCHEMA_REGISTRY_URL_CONFIG,
        "http://localhost:8081",
        StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
        "http://localhost:9091"
    );
    rawUserSpecificAvroSerde.configure(config, false);
    processedUserSpecificAvroSerde.configure(config, false);

    KStream<String, Person> rawUserKStream =
        streamsBuilder.stream(inputTopic, Consumed.with(new Serdes.StringSerde(), rawUserSpecificAvroSerde));

    KStream<String, Person> outputKStream =
        rawUserKStream.filter((key, person) -> person.getName() != "")
            .mapValues((key, person) -> new Person(
                person.getName()
            ));

    outputKStream.to(
        "processedData",
        Produced.with(new Serdes.StringSerde(), processedUserSpecificAvroSerde)
    );
    LOGGER.info("Send processing data to topic");
    Topology topology = streamsBuilder.build();
    LOGGER.info("Topology: {}", topology.describe());

    @SuppressWarnings("java:S2095")
    KafkaStreams kafkaStreams = new KafkaStreams(topology, properties);
    LOGGER.info("Starting stream");
    kafkaStreams.start();

    Runtime.getRuntime().addShutdownHook(new Thread(() -> {
      LOGGER.info("Shutting down stream");
      kafkaStreams.close();
    }));

    return topology;
  }


}
