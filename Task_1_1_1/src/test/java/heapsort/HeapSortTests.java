package heapsort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class HeapSortTests {

    @Test
    void handlesEmptyAndSingleElementArrays() {
        int[] empty = {};
        int[] single = {6767};

        HeapSort.sort(empty);
        HeapSort.sort(single);

        assertArrayEquals(new int[]{}, empty);
        assertArrayEquals(new int[]{6767}, single);
    }

    @Test
    void handlesDuplicatesAndNegativeValues() {
        int[] actual = {0, -67, 67, -3, 2, 0, -10000, 52};

        HeapSort.sort(actual);

        assertArrayEquals(new int[]{-10000, -67, -3, 0, 0, 2, 52, 67}, actual);
    }

    @Test
    void sortsAlreadySortedAndReverseSortedArrays() {
        int[] sorted = {1, 2, 3, 4, 5};
        int[] reverse = {5, 4, 3, 2, 1};

        HeapSort.sort(sorted);
        HeapSort.sort(reverse);

        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, sorted);
        assertArrayEquals(new int[]{1, 2, 3, 4, 5}, reverse);
    }

    @Test
    void matchesJdkSortForManyRandomArrays() {

        Random random = new Random(12345);
        for (int size = 0; size <= 100; size++) {
            for (int repetition = 0; repetition < 20; repetition++) {
                int[] actual = random.ints(size, -1000, 1001).toArray();
                int[] expected = actual.clone();

                Arrays.sort(expected);
                HeapSort.sort(actual);
                assertArrayEquals(expected, actual, "size=" + size);
            }
        }
    }

    @Test
    void rejectsNull() {
        assertThrows(NullPointerException.class, () -> HeapSort.sort(null));
    }
}
