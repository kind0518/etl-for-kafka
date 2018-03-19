package com.heepoman.consumer;

import com.heepoman.repo.Repository;
import com.heepoman.stream.transform.TransForm;
import com.heepoman.window.Window;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;

public class KafkaConsumerImpl implements Consumer {

  private KafkaConsumer consumer;
  private List<String> topics;
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  public KafkaConsumerImpl(KafkaConsumerBuilder builder) {
    Properties props = new Properties();

    this.topics = builder.topics;
    props.put("bootstrap.servers", builder.kafkaHost + ":" + builder.kafkaPort);
    props.put("group.id", builder.groupId);
    props.put("key.deserializer", StringDeserializer.class.getName());
    props.put("value.deserializer", StringDeserializer.class.getName());

    this.consumer = new KafkaConsumer(props);
  }

  @Override
  public CompletableFuture<Void> subscribeAsync(TransForm transForm, Window window, Repository repo) {
    consumer.subscribe(this.topics);
    return CompletableFuture.runAsync(() -> {
      while(true) {
        ConsumerRecords<String, String> records = consumer.poll(1000);
        for (ConsumerRecord<String, String> record: records) {
          if(this.topics.contains(record.topic())) {
            transForm.process(record.value(), window, repo);
          }
        }
        consumer.commitAsync();
      }
    });
  }

  public static class KafkaConsumerBuilder {
    private String kafkaHost = "localhost";
    private String kafkaPort = "9092";
    private String groupId = "default-group-id";
    private List<String> topics;

    public KafkaConsumerBuilder(List<String> topics) {
      this.topics = topics;
    }

    public KafkaConsumerBuilder setKafkaHost(String host) {
      this.kafkaHost = host;
      return this;
    }

    public KafkaConsumerBuilder setGroupId(String groupId) {
      this.groupId = groupId;
      return this;
    }

    public KafkaConsumerImpl build() {
      return new KafkaConsumerImpl(this);
    }

  }

}

