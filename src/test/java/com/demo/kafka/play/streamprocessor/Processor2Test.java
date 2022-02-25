package com.demo.kafka.play.streamprocessor;

import com.demo.kafka.play.KafkaProperties;
import io.confluent.kafka.schemaregistry.client.MockSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.testutil.MockSchemaRegistry;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.TestInputTopic;
import org.apache.kafka.streams.TestOutputTopic;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.TopologyTestDriver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import static io.confluent.kafka.serializers.AbstractKafkaSchemaSerDeConfig.SCHEMA_REGISTRY_URL_CONFIG;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class Processor2Test {

  private static final String INPUT_TOPIC = "users";
  private static final String OUTPUT_TOPIC = "processedData";
  private static final String SCHEMA_REGISTRY_SCOPE = Processor2Test.class.getName();
  private static final String SCHEMA_REGISTRY_URL = "mock://" + SCHEMA_REGISTRY_SCOPE;
  private static final String BOOTSTRAP_SERVER_URL = "http://localhost:9092";

  private final Serde<String> stringSerde = new Serdes.StringSerde();
  private final Serde<Long> longSerde = new Serdes.LongSerde();

  @Mock private ProcessorConfig processorConfig;

  private Processor2 processor2;
  private TopologyTestDriver topologyTestDriver;

  @BeforeEach
  void setUp() {
    KafkaProperties kafkaProperties = givenKafkaProperties();
    processor2 = new Processor2(kafkaProperties, processorConfig);
  }

  @AfterEach
  void tearDown() {
    topologyTestDriver.close();
    MockSchemaRegistry.dropScope(SCHEMA_REGISTRY_SCOPE);
  }

  @Test
  void getProcessedData() {
    // given
    MockSchemaRegistryClient schemaRegistryClient = givenSchemaRegistryClient();
    SpecificAvroSerde<Person> personSpecificAvroSerde = new SpecificAvroSerde<>();
    SpecificAvroSerde<Person> processedDataSpecificAvroSerde = new SpecificAvroSerde<>();

    Properties props = givenProperties();
    given(processorConfig.getProperties()).willReturn(props);

    Map<String, String> config = Map.of(
        SCHEMA_REGISTRY_URL_CONFIG,
        SCHEMA_REGISTRY_URL,
        StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,
        BOOTSTRAP_SERVER_URL
    );
    personSpecificAvroSerde.configure(config, false);
    processedDataSpecificAvroSerde.configure(config, false);

    // when
    Topology topology = processor2.getProcessedData();
    topologyTestDriver = new TopologyTestDriver(topology, props);

    TestInputTopic<String, Person> inputTopic = topologyTestDriver.createInputTopic(
        INPUT_TOPIC,
        stringSerde.serializer(),
        personSpecificAvroSerde.serializer()
    );
    TestOutputTopic<String, Person> outputTopic = topologyTestDriver.createOutputTopic(
        OUTPUT_TOPIC,
        stringSerde.deserializer(),
        processedDataSpecificAvroSerde.deserializer()
    );

    inputTopic.pipeInput(givenPerson("name1"));
    inputTopic.pipeInput(givenPerson("name2"));
    inputTopic.pipeInput(givenPerson("name3"));
    inputTopic.pipeInput(givenPerson("name3"));

    // then
    assertThat(outputTopic.isEmpty()).isFalse();
    List<Person> processedData = outputTopic.readValuesToList();
    assertThat(processedData).containsExactlyInAnyOrder(
        Person.newBuilder()
            .build(),
        Person.newBuilder()
            .build()
    );
    assertThat(outputTopic.isEmpty()).isTrue();
  }

  private Properties givenProperties() {
    Properties properties = new Properties();
    properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "stream-test");
    properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER_URL);
    properties.put(SCHEMA_REGISTRY_URL_CONFIG, SCHEMA_REGISTRY_URL);
    properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, SpecificAvroSerde.class);
    return properties;
  }

  private Person givenPerson(String name) {
    return Person.newBuilder()
     .build();
  }

  private KafkaProperties givenKafkaProperties() {
    KafkaProperties kafkaProperties = new KafkaProperties();
    KafkaProperties.Topic inputTopic = new KafkaProperties.Topic();
    inputTopic.setName(INPUT_TOPIC);
    kafkaProperties.setTopic(inputTopic);
    KafkaProperties.Topic outputTopic = new KafkaProperties.Topic();
    outputTopic.setName(OUTPUT_TOPIC);
    kafkaProperties.setOutputTopic(outputTopic);
    kafkaProperties.setBootstrapAddress(BOOTSTRAP_SERVER_URL);
    kafkaProperties.setSchemaRegistryAddress(SCHEMA_REGISTRY_URL);
    return kafkaProperties;
  }

  private MockSchemaRegistryClient givenSchemaRegistryClient() {
    return new MockSchemaRegistryClient();
  }

}

