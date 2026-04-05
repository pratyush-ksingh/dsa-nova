/**
 * Problem: Kth Missing Positive Number (LeetCode #1539)
 * Difficulty: EASY | XP: 10
 *
 * Find the k-th missing positive integer from a sorted array.
 * Key insight: missing count at index i = arr[i] - (i+1), which is monotonic.
 * Binary search on this derived property.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Count One by One)
// Time: O(n + k) | Space: O(1)
// ============================================================
class BruteForce {
    public static int findKthPositive(int[] arr, int k) {
        int missing = 0;
        int current = 0; // index into arr
        int num = 0;

        while (true) {
            num++;
            if (current < arr.length && arr[current] == num) {
                current++; // num is in the array, skip
            } else {
                missing++;
                if (missing == k) return num;
            }
        }
    }
}

// ============================================================
// Approach 2: Optimal (Binary Search on Missing Count)
// Time: O(log n) | Space: O(1)
// ============================================================
class Optimal {
    public static int findKthPositive(int[] arr, int k) {
        int lo = 0, hi = arr.length - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            // Number of missing integers before arr[mid]
            int missingCount = arr[mid] - (mid + 1);

            if (missingCount < k) {
                lo = mid + 1; // need more missing numbers
            } else {
                hi = mid - 1;
            }
        }
        // lo = first index where missingCount >= k
        // Answer = k + lo
        return k + lo;
    }
}

// ============================================================
// Approach 3: Best (Same logic, cleaner lower-bound style)
// Time: O(log n) | Space: O(1)
// ============================================================
class Best {
    public static int findKthPositive(int[] arr, int k) {
        // Lower bound: find first index where arr[i] - (i+1) >= k
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] - (mid + 1) < k) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return k + lo;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Kth Missing Positive Number ===\n");

        int[][] arrays = {
            {2, 3, 4, 7, 11},
            {1, 2, 3, 4},
            {5, 6, 7, 8, 9},
            {1},
            {2},
            {1, 3},
        };
        int[] ks =       {5, 2, 3, 1, 1, 2};
        int[] expected = {9, 6, 3, 2, 1, 4};

        for (int t = 0; t < arrays.length; t++) {
            int[] arr = arrays[t];
            int k = ks[t];
            int b = BruteForce.findKthPositive(arr, k);
            int o = Optimal.findKthPositive(arr, k);
            int r = Best.findKthPositive(arr, k);
            boolean pass = (b == expected[t] && o == expected[t] && r == expected[t]);

            System.out.println("Input:    " + Arrays.toString(arr) + ", k=" + k);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + r);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
