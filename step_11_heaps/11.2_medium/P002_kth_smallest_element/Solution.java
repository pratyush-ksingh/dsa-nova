import java.util.*;

/**
 * Kth Smallest Element
 *
 * Find the kth smallest element in an unsorted array.
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Sort + Index
    // Time: O(N log N)  |  Space: O(1) if in-place sort
    // ============================================================
    public static int bruteForce(int[] arr, int k) {
        int[] copy = arr.clone();
        Arrays.sort(copy);
        return copy[k - 1];
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Max-Heap of size k
    // Time: O(N log k)  |  Space: O(k)
    // ============================================================
    public static int optimal(int[] arr, int k) {
        // Max-heap: largest element at the top
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Collections.reverseOrder());

        for (int num : arr) {
            if (maxHeap.size() < k) {
                maxHeap.offer(num);
            } else if (num < maxHeap.peek()) {
                maxHeap.poll();
                maxHeap.offer(num);
            }
        }

        return maxHeap.peek(); // root = kth smallest
    }

    // ============================================================
    // APPROACH 3: BEST -- Quickselect (Lomuto partition)
    // Time: O(N) average  |  Space: O(1)
    // ============================================================
    public static int best(int[] arr, int k) {
        int[] copy = arr.clone(); // don't modify original
        return quickselect(copy, 0, copy.length - 1, k - 1);
    }

    private static int quickselect(int[] arr, int lo, int hi, int targetIdx) {
        if (lo == hi) return arr[lo];

        // Random pivot to avoid worst case
        int randomIdx = lo + new Random().nextInt(hi - lo + 1);
        swap(arr, randomIdx, hi);

        int pivotIdx = partition(arr, lo, hi);

        if (pivotIdx == targetIdx) {
            return arr[pivotIdx];
        } else if (pivotIdx < targetIdx) {
            return quickselect(arr, pivotIdx + 1, hi, targetIdx);
        } else {
            return quickselect(arr, lo, pivotIdx - 1, targetIdx);
        }
    }

    private static int partition(int[] arr, int lo, int hi) {
        int pivot = arr[hi];
        int i = lo; // pointer for smaller elements

        for (int j = lo; j < hi; j++) {
            if (arr[j] <= pivot) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, hi);
        return i;
    }

    private static void swap(int[] arr, int a, int b) {
        int tmp = arr[a];
        arr[a] = arr[b];
        arr[b] = tmp;
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        int[] arr = {7, 10, 4, 3, 20, 15};
        int k = 3;

        System.out.println("=== Kth Smallest Element ===");
        System.out.println("Array: " + Arrays.toString(arr) + ", k=" + k);
        System.out.println("Brute:   " + bruteForce(arr, k));  // 7
        System.out.println("Optimal: " + optimal(arr, k));      // 7
        System.out.println("Best:    " + best(arr, k));          // 7

        int[] arr2 = {7, 10, 4, 20, 15};
        System.out.println("\nArray: " + Arrays.toString(arr2) + ", k=4");
        System.out.println("Brute:   " + bruteForce(arr2, 4));  // 15
        System.out.println("Optimal: " + optimal(arr2, 4));      // 15
        System.out.println("Best:    " + best(arr2, 4));          // 15
    }
}
