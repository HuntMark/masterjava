package ru.javaops.masterjava.matrix;

import static ru.javaops.masterjava.matrix.SquareMatricesMultiplicationTest.MATRIX_A;
import static ru.javaops.masterjava.matrix.SquareMatricesMultiplicationTest.MATRIX_B;
import static ru.javaops.masterjava.matrix.SquareMatricesMultiplicationTest.THREAD_NUMBER;
import static ru.javaops.masterjava.matrix.SquareMatricesMultiplicationTest.executor;
import static ru.javaops.masterjava.matrix.SquareMatricesMultiplicationTest.fillByRandomValues;
import static ru.javaops.masterjava.matrix.SquareMatricesMultiplicationTest.naiveMultiplication;
import static ru.javaops.masterjava.matrix.SquareMatricesMultiplicationTest.parallelMultiplication;
import static ru.javaops.masterjava.matrix.SquareMatricesMultiplicationTest.quickMultiplication;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;
import java.util.concurrent.Executors;

public class SquareMatricesMultiplicationJMH {

    public static void main(String[] args) throws IOException, RunnerException {
        setUp();
        org.openjdk.jmh.Main.main(args);
    }

    private static void setUp() {
        fillByRandomValues(MATRIX_A);
        fillByRandomValues(MATRIX_B);
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 2, warmups = 2)
    @Warmup(iterations = 5)
    public void callNaiveMultiplication() {
        naiveMultiplication();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 2, warmups = 2)
    @Warmup(iterations = 5)
    public void callQuickMultiplication() {
        quickMultiplication();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    @Fork(value = 2, warmups = 2)
    @Warmup(iterations = 5)
    public void callParallelMultiplication() {
        try {
            executor = Executors.newFixedThreadPool(THREAD_NUMBER);
            parallelMultiplication();
            executor.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
