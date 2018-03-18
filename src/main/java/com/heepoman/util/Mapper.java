package com.heepoman.util;

public interface Mapper<From, To> {
  To map(From from);
}