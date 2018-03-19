package com.heepoman;

import com.heepoman.consumer.KafkaConsumerImpl;
import com.heepoman.model.EventInfo;
import com.heepoman.repo.EventMySqlRepository;
import com.heepoman.repo.MySqlRepositoryTestKit;
import com.heepoman.stream.StreamContext;
import com.heepoman.stream.transform.DeduplicationTransForm;
import com.heepoman.window.EventSlidingWindow;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static junit.framework.TestCase.assertTrue;

public class IntergrationTest extends MySqlRepositoryTestKit {

  /**
   *
   * Test Scenarios
   *
   * - Integration testing makes the window size 10 seconds for smooth testing.
   * - The first payload and the second and last payload are in the same window.
   * - first payload and last payload are the same.
   * - In this case, last payload not loaded to database.
   *
   *   @author Ahn Heesuk
   **/

  private void schedulerWaitHelper(ScheduledExecutorService scheduler, long duration, TimeUnit unit) {
    try {
      scheduler.awaitTermination(duration, unit);
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }
  }

  private void checkRowHelper(int expectedRow) {
    try {
      conn = driver.getConnection();
      stmt = conn.createStatement();
      int actualRowSize = 0;
      ResultSet rs = stmt.executeQuery("SELECT * FROM event WHERE event_id = 12345678912");
      while(rs.next()) {
        actualRowSize += 1;
      }
      assertTrue(actualRowSize == expectedRow);
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
      if (stmt != null) {
        try {
          stmt.close();
        } catch (SQLException ex) {
          ex.printStackTrace();
        }
      }
    }
  }

  @Test
  public void intergrationTest() {
    final long TEN_SECONDS = 10 * 1000;

    String topic = "test";
    EventInfo eventInfo = EventInfo.getInstance();
    Set<String> filterKeys = new HashSet<String>(Arrays.asList(eventInfo.getEventIdField(), eventInfo.getServiceCodeField(), eventInfo.getEventContextField()));
    String firstPld = "event_id: 12345678912, event_timestamp: 2018-03-18T07:10:30+0000, service_code: SERVICE_CODE, event_context: EVENT_CONTEXT";
    String secondPld = "event_id: 12345678912, event_timestamp: 2018-03-18T07:10:35+0000";
    String lastPldForSameFirstPld = "event_id: 12345678912, event_timestamp: 2018-03-18T07:10:39+0000, service_code: SERVICE_CODE, event_context: EVENT_CONTEXT";

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

    schedulerWaitHelper(kafkaProducerScheduler, 11, TimeUnit.SECONDS);

    checkRowHelper(2);
  }

}
