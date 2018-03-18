package com.heepoman.window;

import com.heepoman.model.Event;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class Window<T> {

  protected BlockingQueue<T> eventWindowTable = new LinkedBlockingQueue<T>();

  public abstract boolean setData(T data);

  public abstract void keepWindowSizeForDuration(T data);

  public abstract BlockingQueue<T> getWindowTable();

}
