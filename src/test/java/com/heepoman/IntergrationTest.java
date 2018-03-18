package com.heepoman;

import com.heepoman.consumer.KafkaConsumerImpl;
import com.heepoman.repo.EventMySqlRepository;
import com.heepoman.stream.StreamContext;
import com.heepoman.stream.transform.DeduplicationTransForm;
import com.heepoman.window.EventSlidingWindow;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IntergrationTest {

  /*
  <Test scenarios>

   - Integration testing makes the window size 10 seconds for smooth testing.
   - The first payload and the second payload are in the same window,
     and the data is different. In this case, both data are loaded into the database.
   - The first payload and The last payload are exactly the same data values ​​except for the timestamp.
     Since both payloads timestamp difference is more than 10 seconds, the last payload is loaded into the database in this case.
  */

  public EventMySqlRepository repo =  new EventMySqlRepository();
  @Test
  public void intergrationTest() {
    final long TEN_SECONDS = 10 * 1000;

    final String topic = "test";
    Set<String> filterKeys = new HashSet<String>(Arrays.asList("eventId", "serviceCodeOpt", "eventContextOpt"));
    String firstPld = "event_id: 12345678911, event_timestamp: 2018-03-18T07:10:30+0000, service_code: SERVICE_CODE, event_context: EVENT_CONTEXT";
    String secondPld = "event_id: 12345678911, event_timestamp: 2018-03-18T07:10:35+0000";
    String lastPldForSameFirstPld = "event_id: 12345678911, event_timestamp: 2018-03-18T07:10:41+0000, service_code: SERVICE_CODE, event_context: EVENT_CONTEXT";

    KafkaProducerForTest producer = new KafkaProducerForTest(topic);
        ScheduledExecutorService kafkaProducerScheduler = Executors.newScheduledThreadPool(5);


    new StreamContext
            .StreamContextBuilder()
            .setConsumer(new KafkaConsumerImpl.KafkaConsumerBuilder(Arrays.asList(topic)).build())
            .setTransform(new DeduplicationTransForm(filterKeys, "event-window"))
            .setWindow(new EventSlidingWindow(TEN_SECONDS))
            .setOutput(new EventMySqlRepository())
            .startPipeline();

    kafkaProducerScheduler.schedule(() -> {
      producer.kafkaProducer.send(new ProducerRecord<String, String>(topic, firstPld));
    }, 0, TimeUnit.SECONDS);

    kafkaProducerScheduler.schedule(() -> {
      producer.kafkaProducer.send(new ProducerRecord<String, String>(topic, secondPld));
    }, 5, TimeUnit.SECONDS);

    kafkaProducerScheduler.schedule(() -> {
      producer.kafkaProducer.send(new ProducerRecord<String, String>(topic, lastPldForSameFirstPld));
    }, 10, TimeUnit.SECONDS);

  }
}