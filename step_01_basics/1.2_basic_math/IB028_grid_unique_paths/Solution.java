/**
 * Problem: Grid Unique Paths
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given an m x n grid, count the number of unique paths from the top-left
 * to the bottom-right corner. Only right and down moves are allowed.
 *
 * Mathematical insight: We need (m-1) down and (n-1) right moves.
 * Total moves = m+n-2. Answer = C(m+n-2, m-1).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Recursion
    // Time: O(2^(m+n))  |  Space: O(m+n) stack
    // ============================================================
    static class BruteForce {
        /**
         * At each cell, recurse down and right.
         * Base case: at bottom row or rightmost column, exactly 1 path.
         * No memoization => exponential due to overlapping subproblems.
         */
        public static int solve(int m, int n) {
            return recurse(0, 0, m, n);
        }

        private static int recurse(int r, int c, int m, int n) {
            if (r == m - 1 || c == n - 1) return 1;
            return recurse(r + 1, c, m, n) + recurse(r, c + 1, m, n);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Dynamic Programming
    // Time: O(m * n)  |  Space: O(m * n)
    // ============================================================
    static class Optimal {
        /**
         * dp[i][j] = number of paths to reach cell (i, j).
         * dp[i][j] = dp[i-1][j] + dp[i][j-1]
         * First row and column are all 1s (only one way to reach them).
         */
        public static long solve(int m, int n) {
            long[][] dp = new long[m][n];
            for (int i = 0; i < m; i++) dp[i][0] = 1;
            for (int j = 0; j < n; j++) dp[0][j] = 1;
            for (int i = 1; i < m; i++) {
                for (int j = 1; j < n; j++) {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
            return dp[m - 1][n - 1];
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Combinatorics C(m+n-2, min(m,n)-1)
    // Time: O(min(m, n))  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * C(N, k) computed multiplicatively: N*(N-1)*...*(N-k+1) / k!
         * where N = m+n-2, k = min(m,n)-1.
         * Use long arithmetic and divide step-by-step to stay in integer range.
         */
        public static long solve(int m, int n) {
            int N = m + n - 2;
            int k = Math.min(m, n) - 1;
            long result = 1;
            for (int i = 0; i < k; i++) {
                result = result * (N - i) / (i + 1);
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Grid Unique Paths ===");
        int[][] tests = {{3, 7}, {3, 3}, {2, 2}, {1, 1}, {4, 4}};
        for (int[] t : tests) {
            int m = t[0], n = t[1];
            // Brute only for small grids
            long brute = (m + n <= 14) ? BruteForce.solve(m, n) : -1;
            System.out.printf("m=%d, n=%d: Brute=%d, Optimal=%d, Best=%d%n",
                m, n, brute, Optimal.solve(m, n), Best.solve(m, n));
        }
    }
}
