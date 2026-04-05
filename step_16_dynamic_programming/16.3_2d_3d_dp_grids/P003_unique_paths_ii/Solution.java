/**
 * Problem: Unique Paths II (LeetCode #63)
 * Difficulty: MEDIUM | XP: 25
 *
 * Grid with obstacles. Count paths from top-left to bottom-right.
 * Only move right or down. Obstacles block paths.
 * All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(2^(m+n)) | Space: O(m+n)
// ============================================================
class RecursivePaths {
    public int uniquePathsWithObstacles(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        if (grid[0][0] == 1 || grid[m - 1][n - 1] == 1) return 0;
        return solve(grid, m - 1, n - 1);
    }

    private int solve(int[][] grid, int i, int j) {
        if (i < 0 || j < 0) return 0;
        if (grid[i][j] == 1) return 0;
        if (i == 0 && j == 0) return 1;

        return solve(grid, i - 1, j) + solve(grid, i, j - 1);
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(m * n) | Space: O(m * n)
// ============================================================
class MemoPaths {
    public int uniquePathsWithObstacles(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        if (grid[0][0] == 1 || grid[m - 1][n - 1] == 1) return 0;

        int[][] dp = new int[m][n];
        for (int[] row : dp) Arrays.fill(row, -1);
        return solve(grid, m - 1, n - 1, dp);
    }

    private int solve(int[][] grid, int i, int j, int[][] dp) {
        if (i < 0 || j < 0) return 0;
        if (grid[i][j] == 1) return 0;
        if (i == 0 && j == 0) return 1;
        if (dp[i][j] != -1) return dp[i][j];

        dp[i][j] = solve(grid, i - 1, j, dp) + solve(grid, i, j - 1, dp);
        return dp[i][j];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(m * n) | Space: O(m * n)
// ============================================================
class TabPaths {
    public int uniquePathsWithObstacles(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        if (grid[0][0] == 1 || grid[m - 1][n - 1] == 1) return 0;

        int[][] dp = new int[m][n];
        dp[0][0] = 1;

        // First row: propagate, stop at obstacle
        for (int j = 1; j < n; j++) {
            dp[0][j] = (grid[0][j] == 0) ? dp[0][j - 1] : 0;
        }

        // First column: propagate, stop at obstacle
        for (int i = 1; i < m; i++) {
            dp[i][0] = (grid[i][0] == 0) ? dp[i - 1][0] : 0;
        }

        // Fill rest of table
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                if (grid[i][j] == 1) {
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = dp[i - 1][j] + dp[i][j - 1];
                }
            }
        }

        return dp[m - 1][n - 1];
    }
}

// ============================================================
// Approach 4: Space Optimized
// Time: O(m * n) | Space: O(n)
// ============================================================
class SpacePaths {
    public int uniquePathsWithObstacles(int[][] grid) {
        int m = grid.length, n = grid[0].length;
        if (grid[0][0] == 1 || grid[m - 1][n - 1] == 1) return 0;

        int[] prev = new int[n];
        // First row
        prev[0] = 1;
        for (int j = 1; j < n; j++) {
            prev[j] = (grid[0][j] == 0) ? prev[j - 1] : 0;
        }

        for (int i = 1; i < m; i++) {
            int[] curr = new int[n];
            curr[0] = (grid[i][0] == 0) ? prev[0] : 0;

            for (int j = 1; j < n; j++) {
                if (grid[i][j] == 1) {
                    curr[j] = 0;
                } else {
                    curr[j] = prev[j] + curr[j - 1];
                }
            }
            prev = curr;
        }

        return prev[n - 1];
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Unique Paths II ===\n");

        RecursivePaths rec = new RecursivePaths();
        MemoPaths memo = new MemoPaths();
        TabPaths tab = new TabPaths();
        SpacePaths space = new SpacePaths();

        int[][][] grids = {
            {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}},
            {{0, 1}, {0, 0}},
            {{1, 0}},
            {{0, 0}, {0, 1}},
            {{0}},
            {{1}},
            {{0, 1, 0}},
            {{0, 0, 0, 0}},
        };
        int[] expected = {2, 1, 0, 0, 1, 0, 0, 1};

        for (int t = 0; t < grids.length; t++) {
            int r = rec.uniquePathsWithObstacles(grids[t]);
            int m = memo.uniquePathsWithObstacles(grids[t]);
            int tb = tab.uniquePathsWithObstacles(grids[t]);
            int s = space.uniquePathsWithObstacles(grids[t]);

            boolean pass = r == expected[t] && m == expected[t]
                    && tb == expected[t] && s == expected[t];

            System.out.println("Grid " + (t + 1) + ":");
            for (int[] row : grids[t]) {
                System.out.println("  " + Arrays.toString(row));
            }
            System.out.println("  Rec=" + r + " | Memo=" + m + " | Tab=" + tb + " | Space=" + s);
            System.out.println("  Expected=" + expected[t] + " | Pass=" + pass + "\n");
        }
    }
}
