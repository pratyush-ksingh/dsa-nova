import java.util.*;

/**
 * Problem: Minimum Falling Path Sum (LeetCode #931)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursion
    // Time: O(3^n)  |  Space: O(n) recursion stack
    // ============================================================
    public static int bruteForce(int[][] matrix) {
        int n = matrix.length;
        int ans = Integer.MAX_VALUE;
        for (int c = 0; c < n; c++) {
            ans = Math.min(ans, solveBrute(matrix, n - 1, c));
        }
        return ans;
    }

    private static int solveBrute(int[][] matrix, int r, int c) {
        if (c < 0 || c >= matrix.length) return Integer.MAX_VALUE;
        if (r == 0) return matrix[0][c];
        int up = solveBrute(matrix, r - 1, c);
        int upLeft = solveBrute(matrix, r - 1, c - 1);
        int upRight = solveBrute(matrix, r - 1, c + 1);
        return matrix[r][c] + Math.min(up, Math.min(upLeft, upRight));
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- DP Tabulation
    // Time: O(n^2)  |  Space: O(n^2)
    // ============================================================
    public static int optimal(int[][] matrix) {
        int n = matrix.length;
        int[][] dp = new int[n][n];

        for (int c = 0; c < n; c++) dp[0][c] = matrix[0][c];

        for (int r = 1; r < n; r++) {
            for (int c = 0; c < n; c++) {
                int up = dp[r - 1][c];
                int upLeft = (c > 0) ? dp[r - 1][c - 1] : Integer.MAX_VALUE;
                int upRight = (c < n - 1) ? dp[r - 1][c + 1] : Integer.MAX_VALUE;
                dp[r][c] = matrix[r][c] + Math.min(up, Math.min(upLeft, upRight));
            }
        }

        int ans = Integer.MAX_VALUE;
        for (int c = 0; c < n; c++) ans = Math.min(ans, dp[n - 1][c]);
        return ans;
    }

    // ============================================================
    // APPROACH 3: BEST -- Space-Optimized 1D DP
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    public static int best(int[][] matrix) {
        int n = matrix.length;
        int[] prev = Arrays.copyOf(matrix[0], n);

        for (int r = 1; r < n; r++) {
            int[] curr = new int[n];
            for (int c = 0; c < n; c++) {
                int up = prev[c];
                int upLeft = (c > 0) ? prev[c - 1] : Integer.MAX_VALUE;
                int upRight = (c < n - 1) ? prev[c + 1] : Integer.MAX_VALUE;
                curr[c] = matrix[r][c] + Math.min(up, Math.min(upLeft, upRight));
            }
            prev = curr;
        }

        int ans = Integer.MAX_VALUE;
        for (int v : prev) ans = Math.min(ans, v);
        return ans;
    }

    public static void main(String[] args) {
        System.out.println("=== Minimum Falling Path Sum ===\n");

        int[][] m1 = {{2, 1, 3}, {6, 5, 4}, {7, 8, 9}};
        System.out.println("Brute:   " + bruteForce(m1));   // 13
        System.out.println("Optimal: " + optimal(m1));       // 13
        System.out.println("Best:    " + best(m1));          // 13

        int[][] m2 = {{-19, 57}, {-40, -5}};
        System.out.println("\nBrute:   " + bruteForce(m2));  // -59
        System.out.println("Optimal: " + optimal(m2));       // -59
        System.out.println("Best:    " + best(m2));          // -59
    }
}
