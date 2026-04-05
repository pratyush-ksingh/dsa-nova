/**
 * Problem: Lower Bound
 * Difficulty: EASY | XP: 10
 *
 * Find the lower bound (first element >= target) in a sorted array.
 * Return array length if no such element exists.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Linear Scan)
// Time: O(n) | Space: O(1)
// ============================================================
class BruteForce {
    public static int lowerBound(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] >= target) {
                return i;
            }
        }
        return arr.length;
    }
}

// ============================================================
// Approach 2: Optimal (Binary Search with Answer Tracking)
// Time: O(log n) | Space: O(1)
// ============================================================
class Optimal {
    public static int lowerBound(int[] arr, int target) {
        int lo = 0, hi = arr.length - 1;
        int ans = arr.length; // default: no element qualifies
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] >= target) {
                ans = mid;       // candidate found, search left for smaller index
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return ans;
    }
}

// ============================================================
// Approach 3: Best (Compact Binary Search -- half-open interval)
// Time: O(log n) | Space: O(1)
// ============================================================
class Best {
    public static int lowerBound(int[] arr, int target) {
        int lo = 0, hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Lower Bound ===\n");

        int[][] arrays = {
            {1, 2, 2, 3, 3, 5},
            {3, 5, 8, 15, 19},
            {1, 2, 3},
            {3, 5, 8},
            {5, 5, 5, 5},
            {10}
        };
        int[] targets = {2, 9, 4, 1, 5, 10};
        int[] expected = {1, 3, 3, 0, 0, 0};

        for (int t = 0; t < arrays.length; t++) {
            int b = BruteForce.lowerBound(arrays[t], targets[t]);
            int o = Optimal.lowerBound(arrays[t], targets[t]);
            int c = Best.lowerBound(arrays[t], targets[t]);
            boolean pass = (b == expected[t] && o == expected[t] && c == expected[t]);

            System.out.println("Input:    " + Arrays.toString(arrays[t]) + ", target=" + targets[t]);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Compact:  " + c);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
