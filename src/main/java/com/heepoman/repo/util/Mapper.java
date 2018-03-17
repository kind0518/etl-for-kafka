package com.heepoman.repo.util;

public interface Mapper<From, To> {
  To map(From from);
}
