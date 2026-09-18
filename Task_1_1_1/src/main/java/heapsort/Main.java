package heapsort;

import java.util.Arrays;
import java.util.Random;

/**
 * enter the program.
 */
public class Main {

    /**
     * check heapsort by hands.
     */
    public static void main(String[] args) {
        int[] values = {1, 8, 39, 1, 0, -4, 5, 92};
        System.out.println("Before: " + Arrays.toString(values));
        HeapSort.sort(values);
        System.out.println("After:  " + Arrays.toString(values));

        benchmark();
    }

    /**
     * Measures sorting time for arrays whose size doubles each iteration.
     * For O(n log n), the time ratio should grow much more slowly than 2^2.
     */
    private static void benchmark() {
        System.out.println();
        System.out.println("Практическая проверка O(n log n):");
        System.out.printf("%10s %15s %15s%n", "n", "time, ms", "T/(n log2 n)");

        Random random = new Random(42);

        for (int n = 10_000; n <= 320_000; n *= 2) {
            int[] data = new int[n];
            for (int i = 0; i < n; i++) {
                data[i] = random.nextInt();
            }

            // Warm-up/JIT and GC noise are reduced by measuring several repetitions.
            long best = Long.MAX_VALUE;
            for (int repetition = 0; repetition < 3; repetition++) {
                int[] copy = data.clone();
                long start = System.nanoTime();
                HeapSort.sort(copy);
                long elapsed = System.nanoTime() - start;

                if (!isSorted(copy)) {
                    throw new AssertionError("Heap sort produced an invalid result");
                }
                best = Math.min(best, elapsed);
            }

            double ms = best / 1_000_000.0;
            double normalized = best / (n * (Math.log(n) / Math.log(2.0)));

            System.out.printf("%10d %15.3f %15.6f%n", n, ms, normalized);
        }

        System.out.println();
        System.out.println("Примечание: измерения зависят от CPU, JVM и фоновой нагрузки.");
        System.out.println("Для O(n log n) нормированное время T/(n log2 n) должно");
        System.out.println("оставаться примерно одного порядка при росте n.");
    }

    /**
     * checks the correctness of sorting
     * @param array
     * @return true or false result
     */
    private static boolean isSorted(int[] array) {
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) {
                return false;
            }
        }
        return true;
    }
}