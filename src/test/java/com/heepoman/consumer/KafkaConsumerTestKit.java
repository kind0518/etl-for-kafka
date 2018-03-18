package com.heepoman.consumer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Before;

import java.util.Properties;

public class KafkaConsumerTestKit {

  private KafkaProducer producer;
  private String topic;

  @Before
  public void initForTest() {
    createProducer();
    publishData();
  }

  private void createProducer() {
    Properties props = new Properties();

    this.topic = "test";
    props.put("bootstrap.servers", "localhost:9092");
    props.put("client.id", "test-producer1");
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    this.producer = new KafkaProducer(props);
  }

  public void publishData() {
    String pld = "event_id: 12345678911, event_timestamp: 2018-03-18T07:10:30+0000, service_code: SERVICE_CODE, event_context: EVENT_CONTEXT";
    producer.send(new ProducerRecord(topic, pld));
  }



}
