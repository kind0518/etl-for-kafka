package com.heepoman.window;

import com.heepoman.model.Event;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public interface Window<T> {

  boolean setData(T data);

  List<T> getDataForDuration(T data);

}
