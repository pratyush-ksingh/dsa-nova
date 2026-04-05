/**
 * Problem: Minimum Path Sum (LeetCode #64)
 * Difficulty: MEDIUM | XP: 25
 *
 * Min path sum from top-left to bottom-right, moving right or down only.
 * All 4 DP approaches.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(2^(m+n)) | Space: O(m+n) stack
// ============================================================
class Recursive {
    public int minPathSum(int[][] grid) {
        return solve(grid, grid.length - 1, grid[0].length - 1);
    }

    private int solve(int[][] grid, int i, int j) {
        if (i == 0 && j == 0) return grid[0][0];
        if (i < 0 || j < 0) return Integer.MAX_VALUE;

        int fromAbove = solve(grid, i - 1, j);
        int fromLeft = solve(grid, i, j - 1);

        return grid[i][j] + Math.min(fromAbove, fromLeft);
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(m * n) | Space: O(m * n)
// ============================================================
class Memoization {
    public int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] dp = new int[m][n];
        for (int[] row : dp) Arrays.fill(row, -1);
        return solve(grid, m - 1, n - 1, dp);
    }

    private int solve(int[][] grid, int i, int j, int[][] dp) {
        if (i == 0 && j == 0) return grid[0][0];
        if (i < 0 || j < 0) return Integer.MAX_VALUE;
        if (dp[i][j] != -1) return dp[i][j];

        int fromAbove = solve(grid, i - 1, j, dp);
        int fromLeft = solve(grid, i, j - 1, dp);

        dp[i][j] = grid[i][j] + Math.min(fromAbove, fromLeft);
        return dp[i][j];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(m * n) | Space: O(m * n)
// ============================================================
class Tabulation {
    public int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[][] dp = new int[m][n];

        dp[0][0] = grid[0][0];

        // First row: can only come from left
        for (int j = 1; j < n; j++) {
            dp[0][j] = dp[0][j - 1] + grid[0][j];
        }

        // First column: can only come from above
        for (int i = 1; i < m; i++) {
            dp[i][0] = dp[i - 1][0] + grid[i][0];
        }

        // Inner cells: min of above and left
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[i][j] = grid[i][j] + Math.min(dp[i - 1][j], dp[i][j - 1]);
            }
        }

        return dp[m - 1][n - 1];
    }
}

// ============================================================
// Approach 4: Space Optimized (1D array)
// Time: O(m * n) | Space: O(n)
// ============================================================
class SpaceOptimized {
    public int minPathSum(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        int[] dp = new int[n];

        // First row
        dp[0] = grid[0][0];
        for (int j = 1; j < n; j++) {
            dp[j] = dp[j - 1] + grid[0][j];
        }

        // Remaining rows
        for (int i = 1; i < m; i++) {
            dp[0] += grid[i][0]; // first col: only from above
            for (int j = 1; j < n; j++) {
                // dp[j] (before update) = value from above (previous row)
                // dp[j-1] (already updated) = value from left (current row)
                dp[j] = grid[i][j] + Math.min(dp[j], dp[j - 1]);
            }
        }

        return dp[n - 1];
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Minimum Path Sum (LeetCode #64) ===\n");

        Recursive rec = new Recursive();
        Memoization memo = new Memoization();
        Tabulation tab = new Tabulation();
        SpaceOptimized space = new SpaceOptimized();

        int[][][] grids = {
            {{1, 3, 1}, {1, 5, 1}, {4, 2, 1}},
            {{1, 2, 3}, {4, 5, 6}},
            {{5}},
            {{1, 2}, {1, 1}},
            {{0, 0, 0}, {0, 0, 0}},
        };
        int[] expected = {7, 12, 5, 3, 0};

        System.out.printf("%-25s %-8s %-8s %-8s %-8s %-8s %-6s%n",
                "Grid", "Rec", "Memo", "Tab", "Space", "Expect", "Pass");
        System.out.println("-".repeat(80));

        for (int t = 0; t < grids.length; t++) {
            int r = rec.minPathSum(grids[t]);
            int m = memo.minPathSum(grids[t]);
            int tb = tab.minPathSum(grids[t]);
            int s = space.minPathSum(grids[t]);

            boolean pass = (r == expected[t]) && (m == expected[t]) &&
                           (tb == expected[t]) && (s == expected[t]);

            String gridStr = grids[t].length + "x" + grids[t][0].length;
            System.out.printf("%-25s %-8d %-8d %-8d %-8d %-8d %-6s%n",
                    gridStr, r, m, tb, s, expected[t], pass ? "PASS" : "FAIL");
        }

        // Show the dp table
        System.out.println("\n--- DP Table Visualization ---");
        int[][] grid = {{1, 3, 1}, {1, 5, 1}, {4, 2, 1}};
        System.out.println("Input grid:");
        for (int[] row : grid) System.out.println("  " + Arrays.toString(row));

        int[][] dp = new int[3][3];
        dp[0][0] = grid[0][0];
        for (int j = 1; j < 3; j++) dp[0][j] = dp[0][j - 1] + grid[0][j];
        for (int i = 1; i < 3; i++) dp[i][0] = dp[i - 1][0] + grid[i][0];
        for (int i = 1; i < 3; i++)
            for (int j = 1; j < 3; j++)
                dp[i][j] = grid[i][j] + Math.min(dp[i - 1][j], dp[i][j - 1]);

        System.out.println("DP table:");
        for (int[] row : dp) System.out.println("  " + Arrays.toString(row));
        System.out.println("Min path sum = " + dp[2][2]);

        // Path reconstruction
        System.out.println("\n--- Path Reconstruction ---");
        List<int[]> path = new ArrayList<>();
        int pi = 2, pj = 2;
        path.add(new int[]{pi, pj});
        while (pi > 0 || pj > 0) {
            if (pi == 0) pj--;
            else if (pj == 0) pi--;
            else if (dp[pi - 1][pj] <= dp[pi][pj - 1]) pi--;
            else pj--;
            path.add(new int[]{pi, pj});
        }
        Collections.reverse(path);
        StringBuilder sb = new StringBuilder();
        for (int[] p : path) sb.append("(" + p[0] + "," + p[1] + ") ");
        System.out.println("Path: " + sb.toString().trim());
    }
}
