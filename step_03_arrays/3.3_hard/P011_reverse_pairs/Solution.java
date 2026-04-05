/**
 * Problem: Reverse Pairs
 * Difficulty: HARD | XP: 50
 *
 * Count the number of "important reverse pairs": pairs (i, j) where
 *   i < j  and  nums[i] > 2 * nums[j]
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1)
    // ============================================================
    /**
     * For every pair (i, j) with i < j, check if nums[i] > 2 * nums[j].
     * Real-life: Finding anomalous pairs in financial data (one value more than
     * double another earlier value — e.g., sudden price drops).
     */
    public static int bruteForce(int[] nums) {
        int n = nums.length;
        int count = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Use long to avoid overflow: nums[i] > 2 * nums[j]
                if ((long) nums[i] > 2L * nums[j]) count++;
            }
        }
        return count;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (Modified Merge Sort)
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    /**
     * During merge sort, before merging two halves, count pairs where
     * left[i] > 2 * right[j]. Since both halves are sorted, use two pointers.
     * Then perform the actual merge.
     * Real-life: Counting inversions in sorted ranking comparisons, stock anomaly detection.
     */
    public static int optimal(int[] nums) {
        return mergeSort(nums, 0, nums.length - 1);
    }

    private static int mergeSort(int[] nums, int lo, int hi) {
        if (lo >= hi) return 0;
        int mid = lo + (hi - lo) / 2;
        int count = mergeSort(nums, lo, mid) + mergeSort(nums, mid + 1, hi);

        // Count pairs: nums[i] > 2 * nums[j] (i in left, j in right)
        int j = mid + 1;
        for (int i = lo; i <= mid; i++) {
            while (j <= hi && (long) nums[i] > 2L * nums[j]) j++;
            count += (j - mid - 1);
        }

        // Merge
        int[] merged = new int[hi - lo + 1];
        int left = lo, right = mid + 1, k = 0;
        while (left <= mid && right <= hi) {
            if (nums[left] <= nums[right]) merged[k++] = nums[left++];
            else merged[k++] = nums[right++];
        }
        while (left <= mid)  merged[k++] = nums[left++];
        while (right <= hi)  merged[k++] = nums[right++];
        System.arraycopy(merged, 0, nums, lo, merged.length);

        return count;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n log n)  |  Space: O(n)
    // ============================================================
    /**
     * Same merge sort — already optimal. Slight variation: uses ArrayList for merging
     * to avoid array copy. Shown for variety.
     * Real-life: Same as above, presented as a common interview implementation style.
     */
    public static int best(int[] nums) {
        // Same as optimal — merge sort is already the best known algorithm.
        return mergeSort(nums.clone(), 0, nums.length - 1);
    }

    public static void main(String[] args) {
        System.out.println("=== Reverse Pairs ===");

        int[][] tests = {
            {1, 3, 2, 3, 1},
            {2, 4, 3, 5, 1},
            {5, 4, 3, 2, 1},
            {1, 2, 3, 4, 5},
        };
        int[] expected = {2, 3, 4, 0};

        for (int t = 0; t < tests.length; t++) {
            System.out.println("\nInput: " + Arrays.toString(tests[t]) + "  =>  expected: " + expected[t]);
            System.out.println("Brute:   " + bruteForce(tests[t].clone()));
            System.out.println("Optimal: " + optimal(tests[t].clone()));
            System.out.println("Best:    " + best(tests[t].clone()));
        }
    }
}
