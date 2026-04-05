import java.util.*;

/**
 * Problem: 0/1 Knapsack
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursion
    // Time: O(2^n)  |  Space: O(n) recursion stack
    // ============================================================
    public static int bruteForce(int[] weights, int[] values, int W) {
        return solveBrute(weights, values, weights.length - 1, W);
    }

    private static int solveBrute(int[] wt, int[] val, int idx, int cap) {
        if (idx == 0) {
            return wt[0] <= cap ? val[0] : 0;
        }
        int notTake = solveBrute(wt, val, idx - 1, cap);
        int take = 0;
        if (wt[idx] <= cap) {
            take = val[idx] + solveBrute(wt, val, idx - 1, cap - wt[idx]);
        }
        return Math.max(notTake, take);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- 2D DP Tabulation
    // Time: O(n * W)  |  Space: O(n * W)
    // ============================================================
    public static int optimal(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[][] dp = new int[n][W + 1];

        // Base: item 0
        for (int w = weights[0]; w <= W; w++) {
            dp[0][w] = values[0];
        }

        for (int i = 1; i < n; i++) {
            for (int w = 0; w <= W; w++) {
                int notTake = dp[i - 1][w];
                int take = 0;
                if (weights[i] <= w) {
                    take = values[i] + dp[i - 1][w - weights[i]];
                }
                dp[i][w] = Math.max(notTake, take);
            }
        }

        return dp[n - 1][W];
    }

    // ============================================================
    // APPROACH 3: BEST -- Space-Optimized 1D DP
    // Time: O(n * W)  |  Space: O(W)
    // Iterate capacity right-to-left to avoid reusing same-row values.
    // ============================================================
    public static int best(int[] weights, int[] values, int W) {
        int n = weights.length;
        int[] dp = new int[W + 1];

        // Base: item 0
        for (int w = weights[0]; w <= W; w++) {
            dp[w] = values[0];
        }

        for (int i = 1; i < n; i++) {
            for (int w = W; w >= weights[i]; w--) {
                dp[w] = Math.max(dp[w], values[i] + dp[w - weights[i]]);
            }
        }

        return dp[W];
    }

    public static void main(String[] args) {
        System.out.println("=== 0/1 Knapsack ===\n");

        int[] wt = {1, 2, 4, 5};
        int[] val = {1, 4, 5, 7};
        System.out.println("Brute:   " + bruteForce(wt, val, 7));   // 11
        System.out.println("Optimal: " + optimal(wt, val, 7));       // 11
        System.out.println("Best:    " + best(wt, val, 7));          // 11

        int[] wt2 = {3, 4, 5};
        int[] val2 = {30, 50, 60};
        System.out.println("\nBrute:   " + bruteForce(wt2, val2, 8));  // 90
        System.out.println("Optimal: " + optimal(wt2, val2, 8));       // 90
        System.out.println("Best:    " + best(wt2, val2, 8));          // 90
    }
}
