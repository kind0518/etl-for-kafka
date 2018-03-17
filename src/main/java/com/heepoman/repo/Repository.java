package com.heepoman.repo;

import com.heepoman.repo.util.SQLSpec;

import java.util.concurrent.CompletableFuture;

public interface Repository<T> {

  CompletableFuture<Void> add(T item);

  CompletableFuture<Void> update(T item);

  CompletableFuture<Void> remove(T item);

  CompletableFuture<T> query(SQLSpec spec);

}
