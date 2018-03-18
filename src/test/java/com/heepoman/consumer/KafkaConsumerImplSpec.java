package com.heepoman.consumer;

import com.heepoman.KafkaProducerForTest;
import com.heepoman.repo.EventMySqlRepository;
import com.heepoman.repo.Repository;
import com.heepoman.stream.transform.DeduplicationTransForm;
import com.heepoman.stream.transform.TransForm;
import com.heepoman.window.EventSlidingWindow;
import com.heepoman.window.Window;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class KafkaConsumerImplSpec extends KafkaConsumerTestKit {

  final String topic = "test";
  List<String> topics = Arrays.asList(topic);
  KafkaConsumerImpl consumer = new KafkaConsumerImpl.KafkaConsumerBuilder(topics).build();
  String payload = "{event_id: 1, event_timestamp: '2018-01-01', service_code: 'SERVICE_CODE', event_context: 'EVENT_CONTEXT'}";
  TransForm<String> deduplicationTransform = mock(DeduplicationTransForm.class);
  Window eventSlidingWindow = mock(EventSlidingWindow.class);
  Repository repo = mock(EventMySqlRepository.class);
  KafkaProducerForTest producer = new KafkaProducerForTest(topic);

  @Ignore
  @Test
  public void subscribeAsync() throws InterruptedException, ExecutionException {
    consumer.subscribeAsync(deduplicationTransform, eventSlidingWindow, repo);
    verify(deduplicationTransform, times(1)).process(any(), any(), any());
  }
}
