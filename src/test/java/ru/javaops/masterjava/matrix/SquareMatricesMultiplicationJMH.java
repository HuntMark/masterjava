package ru.javaops.masterjava.matrix;

import static ru.javaops.masterjava.matrix.SquareMatricesMultiplicationTest.MATRIX_A;
import static ru.javaops.masterjava.matrix.SquareMatricesMultiplicationTest.MATRIX_B;
import static ru.javaops.masterjava.matrix.SquareMatricesMultiplicationTest.fillByRandomValues;
import static ru.javaops.masterjava.matrix.SquareMatricesMultiplicationTest.naiveMultiplication;
import static ru.javaops.masterjava.matrix.SquareMatricesMultiplicationTest.quickMultiplication;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.runner.RunnerException;

import java.io.IOException;

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
}
