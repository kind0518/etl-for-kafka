package com.heepoman.stream;

import com.heepoman.consumer.Consumer;
import com.heepoman.repo.Repository;
import com.heepoman.stream.transform.DeduplicationTransForm;
import com.heepoman.stream.transform.TransForm;
import com.heepoman.window.Window;

import java.util.concurrent.CompletableFuture;

public class StreamContext {

  private final Consumer consumer;
  private final Window window;
  private final Repository repo;
  private final TransForm transform;

  public StreamContext(StreamContextBuilder builder) {
    this.consumer = builder.consumer;
    this.window = builder.window;
    this.repo = builder.repo;
    this.transform = builder.transform;
  }

  public CompletableFuture<Void> startPipeline() {
    return CompletableFuture.runAsync(() -> {
      consumer.subscribeAsync(this.transform, this.window, this.repo);
    });
  }

  public static class StreamContextBuilder {

    private Consumer consumer;
    private TransForm transform;
    private Window window;
    private Repository repo;

    public StreamContextBuilder setConsumer(Consumer consumer) {
      this.consumer = consumer;
      return this;
    }

    public StreamContextBuilder setWindow(Window window) {
      this.window = window;
      return this;
    }

    public StreamContextBuilder setTransform(TransForm transform) {
      this.transform = transform;
      return this;
    }

    public StreamContextBuilder setOutput(Repository repo) {
      this.repo = repo;
      return this;
    }

    public CompletableFuture<Void> startPipeline() {
      return new StreamContext(this).startPipeline();
    }
  }

}
