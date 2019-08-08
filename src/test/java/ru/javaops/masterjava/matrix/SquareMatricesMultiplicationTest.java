package ru.javaops.masterjava.matrix;

import static org.junit.Assert.assertArrayEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class SquareMatricesMultiplicationTest {

    private static final int MATRIX_SIZE = 1000;
    static final int[][] MATRIX_A = new int[MATRIX_SIZE][MATRIX_SIZE];
    static final int[][] MATRIX_B = new int[MATRIX_SIZE][MATRIX_SIZE];
    private static final int[][] ACTUAL_MATRIX = new int[MATRIX_SIZE][MATRIX_SIZE];
    private static final int[][] EXPECTED_MATRIX = new int[MATRIX_SIZE][MATRIX_SIZE];

    @BeforeClass
    public static void setUp() {
        fillByRandomValues(MATRIX_A);
        fillByRandomValues(MATRIX_B);
        naiveMultiplication();
    }

    @Test
    public void testQuickMultiplication() {
        quickMultiplication();
        assertArrayEquals(EXPECTED_MATRIX, ACTUAL_MATRIX);
    }

    static void fillByRandomValues(int[][] matrix) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
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
}
