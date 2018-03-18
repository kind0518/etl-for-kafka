package com.heepoman.stream.transform;

import com.heepoman.repo.Repository;
import com.heepoman.window.Window;

public interface TransForm<T>{

  public void process(T data, Window window, Repository repo);

}
