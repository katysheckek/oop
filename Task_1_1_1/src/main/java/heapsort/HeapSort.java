package heapsort;

public class HeapSort {

    /**
     * sorts elements in the ascending order, use a binary tree as the conception
     * @param list  array pointer, show all unsorted numbers. Must not be NULL
     */
    public static void sort(int[] list){

        if (list == null) {
            throw new NullPointerException("The pointer must not be null");
        }

        int len = list.length;
        for (int parentID = (len - 2)/2; parentID >= 0; parentID--) {
            buildHeap(list, parentID, len-1);
        }

        for(int heapEnd = len - 1; heapEnd > 0; heapEnd--) {
            swap(list, 0, heapEnd);
            buildHeap(list, 0, heapEnd-1);
        }
    }

    /**
     * builds a heap where parents are greater than or equal to their children
     * @param list  array pointer, show all unsorted numbers.
     * @param parentID
     * @param heapEnd
     */
    private static void buildHeap(int[] list, int parentID, int heapEnd){

        int childID;
        while ( 2 * parentID + 1 <= heapEnd ){

            childID = 2 * parentID + 1;

            if (childID < heapEnd) {
                if (list[childID] < list[childID+1])
                    childID++;
            }

            if (list[parentID] < list[childID]) {
                swap(list, parentID, childID);
                parentID = childID;
            } else
                parentID = heapEnd;
        }
    }

    /**
     * swaps list elements by specific indexes
     * @param list  array pointer, show all unsorted numbers.
     * @param start
     * @param end
     */
    public static void swap(int[] list, int start, int end) {
        int temp;
        temp = list[start];
        list[start] = list[end];
        list[end] = temp;
    }


}
