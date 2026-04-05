/**
 * Problem: Find the Smallest Divisor (LeetCode #1283)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an array of integers nums and an integer threshold, find the smallest
 * positive integer divisor such that the sum of ceil(nums[i] / divisor) for
 * all i is <= threshold.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Linear scan from 1 to max)
// Time: O(max(nums) * n) | Space: O(1)
// ============================================================
class BruteForce {
    private static long computeSum(int[] nums, int divisor) {
        long total = 0;
        for (int x : nums) {
            total += (x + divisor - 1) / divisor; // ceil(x / divisor)
        }
        return total;
    }

    public static int smallestDivisor(int[] nums, int threshold) {
        int maxVal = 0;
        for (int x : nums) maxVal = Math.max(maxVal, x);

        for (int d = 1; d <= maxVal; d++) {
            if (computeSum(nums, d) <= threshold) return d;
        }
        return maxVal;
    }
}

// ============================================================
// Approach 2: Optimal (Binary search on divisor)
// Time: O(n * log(max(nums))) | Space: O(1)
// ============================================================
class Optimal {
    private static long computeSum(int[] nums, int divisor) {
        long total = 0;
        for (int x : nums) {
            total += (x + divisor - 1) / divisor;
        }
        return total;
    }

    public static int smallestDivisor(int[] nums, int threshold) {
        int lo = 1, hi = 0;
        for (int x : nums) hi = Math.max(hi, x);
        int ans = hi;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (computeSum(nums, mid) <= threshold) {
                ans = mid;      // feasible; try smaller divisor
                hi = mid - 1;
            } else {
                lo = mid + 1;   // sum too large; need bigger divisor
            }
        }
        return ans;
    }
}

// ============================================================
// Approach 3: Best (Binary search + integer ceil + early exit)
// Time: O(n * log(max(nums))) | Space: O(1)
// ============================================================
class Best {
    private static boolean feasible(int[] nums, int divisor, int threshold) {
        long total = 0;
        for (int x : nums) {
            total += (x + divisor - 1) / divisor;
            if (total > threshold) return false; // early exit
        }
        return true;
    }

    public static int smallestDivisor(int[] nums, int threshold) {
        int lo = 1, hi = 0;
        for (int x : nums) hi = Math.max(hi, x);

        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (feasible(nums, mid, threshold)) {
                hi = mid;       // feasible; try smaller
            } else {
                lo = mid + 1;   // not feasible; need larger
            }
        }
        return lo;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Find the Smallest Divisor ===\n");

        int[][] inputs     = {{1, 2, 5, 9}, {44, 22, 33, 11, 1}, {1, 1, 1, 1}, {2, 3, 5, 7, 11}, {1000000}};
        int[]   thresholds = {6,             5,                    4,             11,                1};
        int[]   expected   = {5,             44,                   1,             3,                 1000000};

        for (int t = 0; t < inputs.length; t++) {
            int[] nums = inputs[t];
            int   thr  = thresholds[t];

            int b = BruteForce.smallestDivisor(nums, thr);
            int o = Optimal.smallestDivisor(nums, thr);
            int n = Best.smallestDivisor(nums, thr);
            boolean pass = b == expected[t] && o == expected[t] && n == expected[t];

            System.out.println("Input:    " + Arrays.toString(nums) + ", threshold=" + thr);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + n);
            System.out.println("Expected: " + expected[t] + "  [" + (pass ? "PASS" : "FAIL") + "]");
            System.out.println();
        }
    }
}
