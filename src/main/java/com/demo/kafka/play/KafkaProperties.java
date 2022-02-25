package com.demo.kafka.play;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka")
public class KafkaProperties {

  private String bootstrapAddress;
  private String schemaRegistryAddress;
  private Consumer consumer;
  private Producer producer;
  private Topic topic;
  private Topic outputTopic;

  public String getBootstrapAddress() {
    return bootstrapAddress;
  }

  public void setBootstrapAddress(String bootstrapAddress) {
    this.bootstrapAddress = bootstrapAddress;
  }

  public String getSchemaRegistryAddress() {
    return schemaRegistryAddress;
  }

  public void setSchemaRegistryAddress(String schemaRegistryAddress) {
    this.schemaRegistryAddress = schemaRegistryAddress;
  }

  public Consumer getConsumer() {
    return consumer;
  }

  public void setConsumer(Consumer consumer) {
    this.consumer = consumer;
  }

  public Producer getProducer() {
    return producer;
  }

  public void setProducer(Producer producer) {
    this.producer = producer;
  }

  public Topic getTopic() {
    return topic;
  }

  public void setTopic(Topic topic) {
    this.topic = topic;
  }

  public Topic getOutputTopic() {
    return outputTopic;
  }

  public void setOutputTopic(Topic outputTopic) {
    this.outputTopic = outputTopic;
  }

  public static class Consumer {

    private String groupId;
    private String autoOffsetReset;
    private String fetchMinBytes;
    private String fetchMaxWaitMs;

    public String getGroupId() {
      return groupId;
    }

    public void setGroupId(String groupId) {
      this.groupId = groupId;
    }

    public String getAutoOffsetReset() {
      return autoOffsetReset;
    }

    public void setAutoOffsetReset(String autoOffsetReset) {
      this.autoOffsetReset = autoOffsetReset;
    }

    public String getFetchMinBytes() {
      return fetchMinBytes;
    }

    public void setFetchMinBytes(String fetchMinBytes) {
      this.fetchMinBytes = fetchMinBytes;
    }

    public String getFetchMaxWaitMs() {
      return fetchMaxWaitMs;
    }

    public void setFetchMaxWaitMs(String fetchMaxWaitMs) {
      this.fetchMaxWaitMs = fetchMaxWaitMs;
    }

  }

  public static class Producer {

    private String acks;
    private int batchSizeInBytes;
    private int lingerMs;
    private String compressionType;

    public String getAcks() {
      return acks;
    }

    public void setAcks(String acks) {
      this.acks = acks;
    }

    public int getBatchSizeInBytes() {
      return batchSizeInBytes;
    }

    public void setBatchSizeInBytes(int batchSizeInBytes) {
      this.batchSizeInBytes = batchSizeInBytes;
    }

    public int getLingerMs() {
      return lingerMs;
    }

    public void setLingerMs(int lingerMs) {
      this.lingerMs = lingerMs;
    }

    public String getCompressionType() {
      return compressionType;
    }

    public void setCompressionType(String compressionType) {
      this.compressionType = compressionType;
    }
  }

  public static class Topic {

    private String name;
    private int partitionNum;
    private int replicationFactor;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getPartitionNum() {
      return partitionNum;
    }

    public void setPartitionNum(int partitionNum) {
      this.partitionNum = partitionNum;
    }

    public int getReplicationFactor() {
      return replicationFactor;
    }

    public void setReplicationFactor(int replicationFactor) {
      this.replicationFactor = replicationFactor;
    }
  }

}
