package heapsort;

/**
 * Realise heapsort and additional functions.
 */
public class HeapSort {

    /**
     * Sorts elements in the ascending order, use a binary tree conception.
     *
     * @param list array pointer, show all unsorted numbers. Must not be NULL
     */
    public static void sort(int[] list) {

        if (list == null) {
            throw new NullPointerException("The pointer must not be null");
        }

        int len = list.length;
        for (int parentId = (len - 2) / 2; parentId >= 0; parentId--) {
            buildHeap(list, parentId, len - 1);
        }

        for (int heapEnd = len - 1; heapEnd > 0; heapEnd--) {
            swap(list, 0, heapEnd);
            buildHeap(list, 0, heapEnd - 1);
        }
    }

    /**
     * Builds a heap where parents are greater than or equal to their children.
     *
     * @param list     array pointer, show all unsorted numbers.
     * @param parentId index of the parent element.
     * @param heapEnd  index of the last element in the heap.
     */
    private static void buildHeap(int[] list, int parentId, int heapEnd) {

        int childId;
        while (2 * parentId + 1 <= heapEnd) {

            childId = 2 * parentId + 1;

            if (childId < heapEnd) {
                if (list[childId] < list[childId + 1]) {
                    childId++;
                }
            }

            if (list[parentId] < list[childId]) {
                swap(list, parentId, childId);
                parentId = childId;
            } else {
                parentId = heapEnd;
            }
        }
    }

    /**
     * Swaps list elements by specific indexes.
     *
     * @param list  array pointer, show all unsorted numbers.
     * @param start index of the first element.
     * @param end   index of the second element.
     */
    public static void swap(int[] list, int start, int end) {
        int temp;
        temp = list[start];
        list[start] = list[end];
        list[end] = temp;
    }
}