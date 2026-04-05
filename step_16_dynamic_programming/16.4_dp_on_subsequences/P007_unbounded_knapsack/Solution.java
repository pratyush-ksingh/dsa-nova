import java.util.*;

/**
 * Problem: Unbounded Knapsack
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursion with repetition
    // Time: O(n^W)  |  Space: O(W) recursion stack
    // ============================================================
    public static int bruteForce(int[] weights, int[] values, int W) {
        return solveBrute(weights, values, weights.length - 1, W);
    }

    private static int solveBrute(int[] wt, int[] val, int idx, int cap) {
        if (idx == 0) {
            return (cap / wt[0]) * val[0];
        }
        int notTake = solveBrute(wt, val, idx - 1, cap);
        int take = 0;
        if (wt[idx] <= cap) {
            // Stay at idx (not idx-1) to allow reuse
            take = val[idx] + solveBrute(wt, val, idx, cap - wt[idx]);
        }
        return Math.max(notTake, take);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- 2D DP Tabulation
    // Time: O(n * W)  |  Space: O(n * W)
    // dp[i][w] = max value using items 0..i with capacity w, each reusable.
    // ============================================================
    public static int optimal(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[][] dp = new int[n][W + 1];

        // Base: item 0 can be taken as many times as it fits
        for (int w = 0; w <= W; w++) {
            dp[0][w] = (w / weights[0]) * values[0];
        }

        for (int i = 1; i < n; i++) {
            for (int w = 0; w <= W; w++) {
                int notTake = dp[i - 1][w];
                int take = 0;
                if (weights[i] <= w) {
                    take = values[i] + dp[i][w - weights[i]]; // same row = reuse
                }
                dp[i][w] = Math.max(notTake, take);
            }
        }

        return dp[n - 1][W];
    }

    // ============================================================
    // APPROACH 3: BEST -- Space-Optimized 1D DP
    // Time: O(n * W)  |  Space: O(W)
    // LEFT to RIGHT traversal enables reuse of same item.
    // (Contrast with 0/1 knapsack which goes right to left.)
    // ============================================================
    public static int best(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[] dp = new int[W + 1];

        // Base: item 0
        for (int w = 0; w <= W; w++) {
            dp[w] = (w / weights[0]) * values[0];
        }

        for (int i = 1; i < n; i++) {
            for (int w = weights[i]; w <= W; w++) { // left to right
                dp[w] = Math.max(dp[w], values[i] + dp[w - weights[i]]);
            }
        }

        return dp[W];
    }

    public static void main(String[] args) {
        System.out.println("=== Unbounded Knapsack ===\n");

        int[] wt = {2, 4, 6};
        int[] val = {5, 11, 13};
        System.out.println("Brute:   " + bruteForce(wt, val, 10));   // 27
        System.out.println("Optimal: " + optimal(wt, val, 10));       // 27
        System.out.println("Best:    " + best(wt, val, 10));          // 27

        int[] wt2 = {1, 3, 4, 5};
        int[] val2 = {1, 4, 5, 7};
        System.out.println("\nBrute:   " + bruteForce(wt2, val2, 8));  // 11
        System.out.println("Optimal: " + optimal(wt2, val2, 8));       // 11
        System.out.println("Best:    " + best(wt2, val2, 8));          // 11
    }
}
