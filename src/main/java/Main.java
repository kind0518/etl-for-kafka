import com.heepoman.consumer.KafkaConsumerImpl;
import com.heepoman.repo.EventMySqlRepository;
import com.heepoman.stream.StreamContext;
import com.heepoman.stream.transform.DeduplicationTransForm;
import com.heepoman.window.EventSlidingWindow;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class Main {

  public static void main(String[] args) {
    final long TEN_MINUTE = 10 * 60 * 1000;
    final List<String> topics = Arrays.asList("test");
    final String targetWindow = "event-window";
    HashSet<String> filterByKeys = new HashSet<String>(Arrays.asList("eventId", "serviceCodeOpt", "eventContextOpt"));

    new StreamContext
      .StreamContextBuilder()
      .setConsumer(new KafkaConsumerImpl.KafkaConsumerBuilder(topics).build())
      .setTransform(new DeduplicationTransForm(filterByKeys,targetWindow))
      .setWindow(new EventSlidingWindow(TEN_MINUTE))
      .setOutput(new EventMySqlRepository())
      .startPipeline();
  }
}
