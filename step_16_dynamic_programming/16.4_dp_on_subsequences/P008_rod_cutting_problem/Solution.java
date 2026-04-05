import java.util.*;

/**
 * Problem: Rod Cutting Problem
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursion
    // Time: O(2^n)  |  Space: O(n) recursion stack
    // Model as unbounded knapsack: piece lengths are weights, prices are values.
    // ============================================================
    public static int bruteForce(int[] prices, int n) {
        return solveBrute(prices, n - 1, n);
    }

    private static int solveBrute(int[] prices, int idx, int remaining) {
        if (idx == 0) {
            return remaining * prices[0]; // only length-1 pieces
        }
        int notCut = solveBrute(prices, idx - 1, remaining);
        int cut = 0;
        int pieceLen = idx + 1;
        if (pieceLen <= remaining) {
            cut = prices[idx] + solveBrute(prices, idx, remaining - pieceLen);
        }
        return Math.max(notCut, cut);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- 2D DP Tabulation
    // Time: O(n^2)  |  Space: O(n^2)
    // dp[i][j] = max profit for rod of length j using pieces of length 1..i+1.
    // ============================================================
    public static int optimal(int[] prices, int n) {
        int[][] dp = new int[n][n + 1];

        // Base: only length-1 pieces
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j * prices[0];
        }

        for (int i = 1; i < n; i++) {
            int pieceLen = i + 1;
            for (int j = 0; j <= n; j++) {
                int notCut = dp[i - 1][j];
                int cut = 0;
                if (pieceLen <= j) {
                    cut = prices[i] + dp[i][j - pieceLen]; // same row = reuse
                }
                dp[i][j] = Math.max(notCut, cut);
            }
        }

        return dp[n - 1][n];
    }

    // ============================================================
    // APPROACH 3: BEST -- 1D Space-Optimized DP
    // Time: O(n^2)  |  Space: O(n)
    // Left-to-right traversal enables reuse (unbounded knapsack variant).
    // ============================================================
    public static int best(int[] prices, int n) {
        int[] dp = new int[n + 1];

        // Base: only length-1 pieces
        for (int j = 0; j <= n; j++) {
            dp[j] = j * prices[0];
        }

        for (int i = 1; i < n; i++) {
            int pieceLen = i + 1;
            for (int j = pieceLen; j <= n; j++) { // left to right
                dp[j] = Math.max(dp[j], prices[i] + dp[j - pieceLen]);
            }
        }

        return dp[n];
    }

    public static void main(String[] args) {
        System.out.println("=== Rod Cutting Problem ===\n");

        int[] prices1 = {1, 5, 8, 9, 10, 17, 17, 20};
        System.out.println("Brute:   " + bruteForce(prices1, 8));   // 22
        System.out.println("Optimal: " + optimal(prices1, 8));       // 22
        System.out.println("Best:    " + best(prices1, 8));          // 22

        int[] prices2 = {3, 5, 8, 9, 10, 17, 17, 20};
        System.out.println("\nBrute:   " + bruteForce(prices2, 8));  // 24
        System.out.println("Optimal: " + optimal(prices2, 8));       // 24
        System.out.println("Best:    " + best(prices2, 8));          // 24
    }
}
