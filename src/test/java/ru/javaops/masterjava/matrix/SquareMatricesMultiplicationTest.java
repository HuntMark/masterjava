package ru.javaops.masterjava.matrix;

import static org.junit.Assert.assertArrayEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

public class SquareMatricesMultiplicationTest {

    private static final int MATRIX_SIZE = 1000;
    static final int THREAD_NUMBER = 10;
    static final int[][] MATRIX_A = new int[MATRIX_SIZE][MATRIX_SIZE];
    static final int[][] MATRIX_B = new int[MATRIX_SIZE][MATRIX_SIZE];
    private static final int[][] ACTUAL_MATRIX = new int[MATRIX_SIZE][MATRIX_SIZE];
    private static final int[][] EXPECTED_MATRIX = new int[MATRIX_SIZE][MATRIX_SIZE];

    static ExecutorService executor = Executors.newFixedThreadPool(THREAD_NUMBER);

    @BeforeClass
    public static void setUp() {
        fillByRandomValues(MATRIX_A);
        fillByRandomValues(MATRIX_B);
        naiveMultiplication();
    }

    @AfterClass
    public static void tearDown() {
        executor.shutdown();
    }

    @Test
    public void testQuickMultiplication() {
        quickMultiplication();
        assertArrayEquals(EXPECTED_MATRIX, ACTUAL_MATRIX);
    }

    @Test
    public void testParallelMultiplication() throws InterruptedException {
        parallelMultiplication();
        assertArrayEquals(EXPECTED_MATRIX, ACTUAL_MATRIX);
    }

    static void fillByRandomValues(int[][] matrix) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                matrix[i][j] = random.nextInt(1, 10);
            }
        }
    }

    static void naiveMultiplication() {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                int sum = 0;
                for (int k = 0; k < MATRIX_SIZE; k++) {
                    sum += MATRIX_A[i][k] * MATRIX_B[k][j];
                }
                EXPECTED_MATRIX[i][j] = sum;
            }
        }
    }

    /**
     * @see <a href="https://habr.com/ru/post/114797/">История одной оптимизации</a>
     */
    static void quickMultiplication() {
        final int[] column = new int[MATRIX_SIZE];
        try {
            //noinspection InfiniteLoopStatement
            for (int j = 0; ; j++) {
                for (int k = 0; k < MATRIX_SIZE; k++) {
                    column[k] = MATRIX_B[k][j];
                }
                for (int i = 0; i < MATRIX_SIZE; i++) {
                    int[] row = MATRIX_A[i];
                    int sum = 0;
                    for (int k = 0; k < MATRIX_SIZE; k++) {
                        sum += row[k] * column[k];
                    }
                    ACTUAL_MATRIX[i][j] = sum;
                }
            }
        } catch (IndexOutOfBoundsException ignore) {
        }
    }

    static void parallelMultiplication() throws InterruptedException {
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            final int k = (int) Math.pow(THREAD_NUMBER, 2);
            final int from = i * k;
            final int to = (i + 1) * k;
            Callable<Void> task = () -> partMultiplication(from, to);
            tasks.add(task);
        }
        executor.invokeAll(tasks);
    }

    private static Void partMultiplication(int startJ, int endJ) {
        final int[] column = new int[MATRIX_SIZE];
        for (int j = startJ; j < endJ; j++) {
            for (int k = 0; k < MATRIX_SIZE; k++) {
                column[k] = MATRIX_B[k][j];
            }
            for (int i = 0; i < MATRIX_SIZE; i++) {
                int[] row = MATRIX_A[i];
                int sum = 0;
                for (int k = 0; k < MATRIX_SIZE; k++) {
                    sum += row[k] * column[k];
                }
                ACTUAL_MATRIX[i][j] = sum;
            }
        }
        return null;
    }
}
