package ru.etu.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class RunnableBenchmark implements IBenchmark<Runnable> {

    public static final int DEFAULT_BENCHMARK_RUNS = 100;

    private final String name;

    private final List<Long> benchmarks;

    private final Runnable function;

    private RunnableBenchmark(String name, Runnable func) {
        this.name = name;
        this.function = func;
        this.benchmarks = new ArrayList<>();
    }

    public static RunnableBenchmark of(String name, Runnable func) {
        final RunnableBenchmark benchmark = new RunnableBenchmark(name, func);

        return benchmark;
    }

    public String getName() {
        return this.name;
    }

    public List<Long> getBenchmarks() {
        return Collections.unmodifiableList(this.benchmarks);
    }

    public long getMedian() {
        return this.benchmarks.stream().mapToLong(Long::longValue).sum() / this.benchmarks.size();
    }

    public Runnable getFunction() {
        return this.function;
    }

    public void benchmark() {
        this.benchmark(DEFAULT_BENCHMARK_RUNS);
    }

    public void benchmarkWithReset(Runnable resetFunction) {
        this.benchmarkWithReset(DEFAULT_BENCHMARK_RUNS, resetFunction);
    }

    public void benchmark(int runs) {
        for (int i = 0; i < runs; ++i) {
            this.benchmarks.add(this.logExecTime(function));
        }
    }

    public void benchmarkWithReset(int runs, Runnable resetFunction) {
        for (int i = 0; i < runs; ++i) {
            this.benchmarks.add(this.logExecTime(function));
            resetFunction.run();
        }
    }

    private long logExecTime(Runnable func) {
        final long start = System.nanoTime();
        func.run();
        return System.nanoTime() - start;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        if (!super.equals(object)) return false;
        RunnableBenchmark benchmark = (RunnableBenchmark) object;
        return Objects.equals(name, benchmark.name);
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("RunnableBenchmark [name=").append(name).append(", median=").append(this.getMedian())
                .append(" nanoseconds]");
        return builder.toString();
    }    
    
}
