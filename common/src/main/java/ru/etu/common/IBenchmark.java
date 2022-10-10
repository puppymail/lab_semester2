package ru.etu.common;

import java.util.Collection;

public interface IBenchmark<F> {
    
    int DEFAULT_BENCHMARK_RUNS = 100;

    String getName();

    F getFunction();

    Collection<Long> getBenchmarks();

    long getMedian();

    void benchmark();    
    
}
