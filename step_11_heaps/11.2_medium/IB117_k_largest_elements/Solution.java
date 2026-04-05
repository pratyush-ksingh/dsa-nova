/**
 * Problem: K Largest Elements
 * InterviewBit | Difficulty: EASY | XP: 10
 *
 * Given an array of N integers and a number k, find the k largest elements
 * and return them in descending order.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE  (Sort descending)
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        /**
         * Sort the entire array in descending order and return the first k elements.
         * Simple and correct; sorts elements we never need.
         */
        public int[] kLargest(int[] nums, int k) {
            Integer[] arr = new Integer[nums.length];
            for (int i = 0; i < nums.length; i++) arr[i] = nums[i];
            Arrays.sort(arr, Collections.reverseOrder());
            int[] result = new int[k];
            for (int i = 0; i < k; i++) result[i] = arr[i];
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (Min-Heap of size k)
    // Time: O(n log k)  |  Space: O(k)
    // ============================================================
    static class Optimal {
        /**
         * Maintain a min-heap of at most k elements.
         * If heap size exceeds k, evict the minimum (smallest of the k+1 candidates).
         * After processing all elements, the heap holds the k largest.
         * Sort descending before returning.
         */
        public int[] kLargest(int[] nums, int k) {
            PriorityQueue<Integer> minHeap = new PriorityQueue<>();
            for (int num : nums) {
                minHeap.offer(num);
                if (minHeap.size() > k) {
                    minHeap.poll();  // remove smallest
                }
            }
            int[] result = new int[k];
            int idx = k - 1;
            while (!minHeap.isEmpty()) {
                result[idx--] = minHeap.poll();
            }
            return result;  // already in ascending order from heap, reversed above
        }
    }

    // ============================================================
    // APPROACH 3: BEST  (Quickselect — O(n) average)
    // Time: O(n) average, O(n^2) worst  |  Space: O(1) in-place (plus output)
    // ============================================================
    static class Best {
        private static final Random RNG = new Random();

        /**
         * Partition the array so that elements at indices [n-k, n-1] are the
         * k largest (in any order). Then sort just those k elements descending.
         * Random pivot avoids worst-case O(n^2) on sorted input.
         */
        public int[] kLargest(int[] nums, int k) {
            int[] arr = nums.clone();
            int n = arr.length;
            quickselect(arr, 0, n - 1, n - k);
            int[] result = Arrays.copyOfRange(arr, n - k, n);
            // sort descending
            Arrays.sort(result);
            for (int i = 0, j = result.length - 1; i < j; i++, j--) {
                int tmp = result[i]; result[i] = result[j]; result[j] = tmp;
            }
            return result;
        }

        private void quickselect(int[] arr, int lo, int hi, int target) {
            if (lo >= hi) return;
            int pivot = partition(arr, lo, hi);
            if (pivot == target) return;
            else if (pivot < target) quickselect(arr, pivot + 1, hi, target);
            else                     quickselect(arr, lo, pivot - 1, target);
        }

        private int partition(int[] arr, int lo, int hi) {
            int pivotIdx = lo + RNG.nextInt(hi - lo + 1);
            swap(arr, pivotIdx, hi);
            int pivot = arr[hi], store = lo;
            for (int i = lo; i < hi; i++) {
                if (arr[i] <= pivot) swap(arr, store++, i);
            }
            swap(arr, store, hi);
            return store;
        }

        private void swap(int[] arr, int i, int j) {
            int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
        }
    }

    public static void main(String[] args) {
        int[][] tests = {
            {3, 1, 4, 1, 5, 9, 2, 6},
            {1, 2, 3, 4, 5},
            {7, 7, 7},
            {10}
        };
        int[] ks   = {3, 2, 2, 1};
        int[][] expected = {{9, 6, 5}, {5, 4}, {7, 7}, {10}};

        BruteForce bf  = new BruteForce();
        Optimal    opt = new Optimal();
        Best       bst = new Best();

        System.out.println("=== K Largest Elements ===");
        for (int t = 0; t < tests.length; t++) {
            int[] b  = bf.kLargest(tests[t], ks[t]);
            int[] o  = opt.kLargest(tests[t], ks[t]);
            int[] be = bst.kLargest(tests[t], ks[t]);
            System.out.printf("  brute=%s  optimal=%s  best=%s  (expected %s)%n",
                Arrays.toString(b), Arrays.toString(o),
                Arrays.toString(be), Arrays.toString(expected[t]));
        }
    }
}
