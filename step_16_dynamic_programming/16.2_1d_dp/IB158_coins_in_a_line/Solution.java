import java.util.*;

/**
 * Problem: Coins in a Line
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Two players alternately pick coins from either end of an array.
 * Both play optimally. Return maximum coins the first player can collect.
 * Classic interval DP problem.
 *
 * @author DSA_Nova
 */

// ============================================================
// APPROACH 1: BRUTE FORCE
// Recursive with memoization: dp[i][j] = max coins player can collect from A[i..j]
// Time: O(N^2)  |  Space: O(N^2)
// ============================================================
class BruteForce {
    static int[] coins;
    static int[][] memo;

    static int dp(int i, int j) {
        if (i > j) return 0;
        if (i == j) return coins[i];
        if (memo[i][j] != -1) return memo[i][j];
        // Current player picks left or right; opponent then plays optimally
        // If player picks left (coins[i]), opponent picks from [i+1, j]
        // After opponent picks optimally, current player gets the remaining
        int pickLeft = coins[i] + Math.min(dp(i + 2, j), dp(i + 1, j - 1));
        int pickRight = coins[j] + Math.min(dp(i, j - 2), dp(i + 1, j - 1));
        return memo[i][j] = Math.max(pickLeft, pickRight);
    }

    static int maxCoins(int[] A) {
        coins = A;
        int n = A.length;
        memo = new int[n][n];
        for (int[] row : memo) Arrays.fill(row, -1);
        return dp(0, n - 1);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Bottom-up DP: dp[i][j] = max coins first player gets from subarray A[i..j]
// Time: O(N^2)  |  Space: O(N^2)
// ============================================================
class Optimal {
    static int maxCoins(int[] A) {
        int n = A.length;
        int[][] dp = new int[n][n];

        // Base case: single element
        for (int i = 0; i < n; i++) dp[i][i] = A[i];

        // Fill for increasing lengths
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                // Pick left: A[i] + min of what remains (opponent plays optimally)
                int pickLeft = A[i] + (len > 2 ? Math.min(dp[i + 2][j], dp[i + 1][j - 1]) : 0);
                // Pick right: A[j] + min of what remains
                int pickRight = A[j] + (len > 2 ? Math.min(dp[i][j - 2], dp[i + 1][j - 1]) : 0);
                dp[i][j] = Math.max(pickLeft, pickRight);
            }
        }
        return dp[0][n - 1];
    }
}

// ============================================================
// APPROACH 3: BEST
// Observation: if n is even, first player can always guarantee
// sum of either all even-indexed or all odd-indexed coins (whichever is larger).
// Time: O(N)  |  Space: O(1) for even-length arrays
// (For odd-length, falls back to DP since game doesn't split evenly)
// ============================================================
class Best {
    static int maxCoins(int[] A) {
        int n = A.length;
        if (n % 2 == 0) {
            // First player can always choose to take all even or all odd indexed coins
            int sumEven = 0, sumOdd = 0;
            for (int i = 0; i < n; i++) {
                if (i % 2 == 0) sumEven += A[i];
                else sumOdd += A[i];
            }
            return Math.max(sumEven, sumOdd);
        }
        // For odd length, use O(N^2) DP (same as Optimal)
        return Optimal.maxCoins(A);
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Coins in a Line ===");

        int[] A1 = {6, 9, 1, 2, 16, 8}; // even length
        System.out.println("A1 BruteForce: " + BruteForce.maxCoins(A1));  // should be max
        System.out.println("A1 Optimal:    " + Optimal.maxCoins(A1));
        System.out.println("A1 Best:       " + Best.maxCoins(A1));

        int[] A2 = {1, 2, 3, 4};
        System.out.println("A2 BruteForce: " + BruteForce.maxCoins(A2));  // 6
        System.out.println("A2 Optimal:    " + Optimal.maxCoins(A2));     // 6
        System.out.println("A2 Best:       " + Best.maxCoins(A2));        // 6

        int[] A3 = {5, 3, 7, 10}; // even: even-idx=12, odd-idx=13 => 13
        System.out.println("A3 Optimal: " + Optimal.maxCoins(A3));   // 13
        System.out.println("A3 Best:    " + Best.maxCoins(A3));      // 13

        int[] A4 = {8, 15, 3, 7}; // even: 11, odd: 22 => 22
        System.out.println("A4 Best: " + Best.maxCoins(A4));  // 22
    }
}
