//package com.heepoman.stream;
//
//import com.heepoman.consumer.Consumer;
//import com.heepoman.repo.EventMySqlRepository;
//import com.heepoman.repo.Repository;
//import com.heepoman.window.Window;
//
//import java.util.concurrent.CompletableFuture;
//import com.heepoman.stream.transform.;
//
//public class StreamContext {
//
//  Consumer consumer;
//  Window window;
//  Repository repo;
//
//  public StreamContext(StreamContextBuilder builder) {
//    this.consumer = builder.consumer;
//    this.window = builder.window;
//    this.repo = builder.repo;
//  }
//
//  public CompletableFuture<Void> startPipeline() {
//    consumer.subscribeAsync();
//    return CompletableFuture.runAsync(() -> {
//      //Step1) 무한으로 consumer로 부터 데이터를 받는다.
////      while(true) {
////        consumer
////          .getDataFromConsumer()
////          .thenApply(data -> parseToEvent(data))
////      }
//
//      //Blocking Queue -> Pool -> Data one Take -> parse Event Model -> Check Window Table -> Insert DB
//      //Step2) 받은 consumer data를 parse하여 event model 로 만든다
//
//      //Step3) window에서 10분전 까지 데이터를 살펴봐서 중복된 것이 있는지 확인한다.
//
//      //* window에서 transform 로직은 stream api를 사용하면 좋으려나~
//
//      //Step4) 중복된 것이 없다면 저장하고 중복된 것이 있다면 버린다.
//
//    });
//  }
//
//  public static class StreamContextBuilder {
//
//    private Consumer consumer;
//    private TransF transform;
//    private Window window;
//    private Repository repo;
//
//    public StreamContextBuilder setConsumer(Consumer consumer) {
//      this.consumer = consumer;
//      return this;
//    }
//
//    public StreamContextBuilder setWindow(Window window) {
//      this.window = window;
//      return this;
//    }
//
//    public StreamContextBuilder setOutput(Repository repo) {
//      this.repo = repo;
//      return this;
//    }
//
//    public CompletableFuture<Void> startPipeline() {
//      return new StreamContext(this).startPipeline();
//    }
//  }
//
//}
