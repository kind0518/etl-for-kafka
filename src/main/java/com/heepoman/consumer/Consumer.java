package com.heepoman.consumer;

import java.util.concurrent.CompletableFuture;

import com.heepoman.repo.Repository;
import com.heepoman.stream.transform.TransForm;
import com.heepoman.window.Window;

public interface Consumer {

  CompletableFuture<Void> subscribeAsync(TransForm transform, Window window, Repository repo);

}