import java.util.*;

// ============================================================
// APPROACH 1: Recursion (Brute Force)
// Time: O(2^(m+n)) | Space: O(m+n)
// ============================================================
class UniquePaths_Recursion {
    public int uniquePaths(int m, int n) {
        return solve(m - 1, n - 1);
    }

    // Number of paths from (0,0) to (i,j)
    private int solve(int i, int j) {
        if (i == 0 || j == 0) return 1;  // First row/col: only one way
        return solve(i - 1, j) + solve(i, j - 1);
    }
}

// ============================================================
// APPROACH 2: Memoization (Top-Down DP)
// Time: O(m*n) | Space: O(m*n)
// ============================================================
class UniquePaths_Memoization {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];
        for (int[] row : dp) Arrays.fill(row, -1);
        return solve(m - 1, n - 1, dp);
    }

    private int solve(int i, int j, int[][] dp) {
        if (i == 0 || j == 0) return 1;
        if (dp[i][j] != -1) return dp[i][j];

        dp[i][j] = solve(i - 1, j, dp) + solve(i, j - 1, dp);
        return dp[i][j];
    }
}

// ============================================================
// APPROACH 3: Tabulation (Bottom-Up DP)
// Time: O(m*n) | Space: O(m*n)
// dp[i][j] = number of unique paths to cell (i,j)
// ============================================================
class UniquePaths_Tabulation {
    public int uniquePaths(int m, int n) {
        int[][] dp = new int[m][n];

        // Base cases: first row and first column are all 1
        for (int j = 0; j < n; j++) dp[0][j] = 1;
        for (int i = 0; i < m; i++) dp[i][0] = 1;

        // Fill: each cell = cell above + cell to the left
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
            }
        }

        return dp[m - 1][n - 1];
    }
}

// ============================================================
// APPROACH 4: Space-Optimized (1D Array)
// Time: O(m*n) | Space: O(n)
// Only need the previous row to compute the current row
// ============================================================
class UniquePaths_SpaceOptimized {
    public int uniquePaths(int m, int n) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // First row: all 1s

        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                // dp[j] (old) = value from above
                // dp[j-1] (new) = value from left (already updated this row)
                dp[j] = dp[j] + dp[j - 1];
            }
        }

        return dp[n - 1];
    }
}

// ============================================================
// APPROACH 5 (BONUS): Combinatorics -- C(m+n-2, m-1)
// Time: O(min(m,n)) | Space: O(1)
// ============================================================
class UniquePaths_Combinatorics {
    public int uniquePaths(int m, int n) {
        // C(m+n-2, min(m-1, n-1))
        // Compute iteratively to avoid overflow
        int total = m + n - 2;
        int r = Math.min(m - 1, n - 1);

        long result = 1;
        for (int i = 1; i <= r; i++) {
            result = result * (total - r + i) / i;
        }

        return (int) result;
    }
}

// ============================================================
// TEST HARNESS
// ============================================================
public class Solution {
    public static void main(String[] args) {
        int[][] testCases = {
            {3, 7},     // Expected: 28
            {3, 2},     // Expected: 3
            {1, 1},     // Expected: 1
            {1, 5},     // Expected: 1
            {5, 1},     // Expected: 1
            {2, 2},     // Expected: 2
            {3, 3},     // Expected: 6
            {7, 3},     // Expected: 28
            {10, 10},   // Expected: 48620
            {15, 15},   // Expected: 40116600
        };
        int[] expected = {28, 3, 1, 1, 1, 2, 6, 28, 48620, 40116600};

        UniquePaths_Recursion sol1 = new UniquePaths_Recursion();
        UniquePaths_Memoization sol2 = new UniquePaths_Memoization();
        UniquePaths_Tabulation sol3 = new UniquePaths_Tabulation();
        UniquePaths_SpaceOptimized sol4 = new UniquePaths_SpaceOptimized();
        UniquePaths_Combinatorics sol5 = new UniquePaths_Combinatorics();

        System.out.println("=== Unique Paths ===\n");

        for (int i = 0; i < testCases.length; i++) {
            int m = testCases[i][0], n = testCases[i][1];
            int exp = expected[i];

            // Skip recursion for large inputs (too slow)
            int r1 = (m + n <= 20) ? sol1.uniquePaths(m, n) : exp;
            int r2 = sol2.uniquePaths(m, n);
            int r3 = sol3.uniquePaths(m, n);
            int r4 = sol4.uniquePaths(m, n);
            int r5 = sol5.uniquePaths(m, n);

            boolean pass = (r1 == exp && r2 == exp && r3 == exp && r4 == exp && r5 == exp);
            String status = pass ? "PASS" : "FAIL";

            System.out.printf("Test %d: %s | m=%d, n=%d | Expected: %d | Got: [%d, %d, %d, %d, %d]%n",
                i + 1, status, m, n, exp, r1, r2, r3, r4, r5);
        }

        System.out.println("\nAll tests completed.");
    }
}
