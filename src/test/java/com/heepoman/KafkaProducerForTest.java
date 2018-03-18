package com.heepoman;

import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

public class KafkaProducerForTest {

  public KafkaProducer kafkaProducer;

  public KafkaProducerForTest(String topic) {
    Properties props = new Properties();

    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("bootstrap.servers", "localhost:9092");
    props.put("client.id", "test-producer1");

    this.kafkaProducer = new KafkaProducer(props);
  }
}
