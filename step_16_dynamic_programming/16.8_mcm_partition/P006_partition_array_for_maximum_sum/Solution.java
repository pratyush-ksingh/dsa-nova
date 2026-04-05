import java.util.*;

/**
 * Problem: Partition Array for Maximum Sum (LeetCode 1043)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursion
    // Time: O(k^n)  |  Space: O(n) recursion stack
    // At each index, try all partition lengths 1..k recursively.
    // ============================================================
    public static int bruteForce(int[] arr, int k) {
        return solveBrute(arr, 0, k);
    }

    private static int solveBrute(int[] arr, int idx, int k) {
        int n = arr.length;
        if (idx == n) return 0;
        int maxVal = 0, best = 0;
        for (int len = 1; len <= k && idx + len <= n; len++) {
            maxVal = Math.max(maxVal, arr[idx + len - 1]);
            int curr = maxVal * len + solveBrute(arr, idx + len, k);
            best = Math.max(best, curr);
        }
        return best;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- DP Tabulation (right to left)
    // Time: O(n * k)  |  Space: O(n)
    // dp[i] = max sum for arr[i..n-1].
    // ============================================================
    public static int optimal(int[] arr, int k) {
        int n = arr.length;
        int[] dp = new int[n + 1]; // dp[n] = 0

        for (int i = n - 1; i >= 0; i--) {
            int maxVal = 0, best = 0;
            for (int len = 1; len <= k && i + len <= n; len++) {
                maxVal = Math.max(maxVal, arr[i + len - 1]);
                int curr = maxVal * len + dp[i + len];
                best = Math.max(best, curr);
            }
            dp[i] = best;
        }

        return dp[0];
    }

    // ============================================================
    // APPROACH 3: BEST -- DP (forward direction, same complexity)
    // Time: O(n * k)  |  Space: O(n)
    // dp[i] = max sum for arr[0..i-1]. Look back up to k steps.
    // ============================================================
    public static int best(int[] arr, int k) {
        int n = arr.length;
        int[] dp = new int[n + 1]; // dp[0] = 0

        for (int i = 1; i <= n; i++) {
            int maxVal = 0, bestVal = 0;
            for (int len = 1; len <= k && i - len >= 0; len++) {
                maxVal = Math.max(maxVal, arr[i - len]);
                int curr = maxVal * len + dp[i - len];
                bestVal = Math.max(bestVal, curr);
            }
            dp[i] = bestVal;
        }

        return dp[n];
    }

    public static void main(String[] args) {
        System.out.println("=== Partition Array for Maximum Sum ===\n");

        int[] arr1 = {1, 15, 7, 9, 2, 5, 10};
        System.out.println("Brute:   " + bruteForce(arr1, 3));   // 84
        System.out.println("Optimal: " + optimal(arr1, 3));       // 84
        System.out.println("Best:    " + best(arr1, 3));          // 84

        int[] arr2 = {1, 4, 1, 5, 7, 3, 6, 1, 9, 9, 3};
        System.out.println("\nBrute:   " + bruteForce(arr2, 4));  // 83
        System.out.println("Optimal: " + optimal(arr2, 4));       // 83
        System.out.println("Best:    " + best(arr2, 4));          // 83

        int[] arr3 = {1};
        System.out.println("\nSingle: " + best(arr3, 1));  // 1
    }
}
