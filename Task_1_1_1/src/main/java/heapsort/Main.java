package heapsort;

import java.util.Arrays;

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
    }
}