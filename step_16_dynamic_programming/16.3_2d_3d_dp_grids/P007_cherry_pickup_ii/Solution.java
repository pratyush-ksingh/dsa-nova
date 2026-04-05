/**
 * Problem: Cherry Pickup II (LeetCode 1463)
 * Difficulty: HARD | XP: 50
 *
 * Given an rows x cols grid, robot1 starts at (0,0) and robot2 at (0,cols-1).
 * Both move downward simultaneously, each to one of 3 cells in the next row
 * (diag-left, straight down, diag-right). Cherries at both positions are
 * collected; if both on same cell, count once. Maximize total cherries.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Pure Recursion
    // Time: O(9^rows)  |  Space: O(rows) recursion stack
    // No memoization; tries all 9 direction combos at each row.
    // ============================================================
    static class BruteForce {

        private static int rows, cols;
        private static int[][] grid;
        private static int[] dirs = {-1, 0, 1};

        private static int rec(int row, int c1, int c2) {
            int cherries = grid[row][c1] + (c1 != c2 ? grid[row][c2] : 0);
            if (row == rows - 1) return cherries;
            int best = 0;
            for (int d1 : dirs) {
                for (int d2 : dirs) {
                    int nc1 = c1 + d1, nc2 = c2 + d2;
                    if (nc1 >= 0 && nc1 < cols && nc2 >= 0 && nc2 < cols) {
                        best = Math.max(best, rec(row + 1, nc1, nc2));
                    }
                }
            }
            return cherries + best;
        }

        public static int solve(int[][] g) {
            grid = g;
            rows = g.length;
            cols = g[0].length;
            return rec(0, 0, cols - 1);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Top-Down 3D Memoization
    // Time: O(rows * cols^2)  |  Space: O(rows * cols^2)
    // State: (row, c1, c2). At most rows*cols^2 unique states.
    // ============================================================
    static class Optimal {

        private static int rows, cols;
        private static int[][] grid;
        private static int[][][] memo;
        private static int[] dirs = {-1, 0, 1};

        private static int dp(int row, int c1, int c2) {
            if (memo[row][c1][c2] != -1) return memo[row][c1][c2];
            int cherries = grid[row][c1] + (c1 != c2 ? grid[row][c2] : 0);
            if (row == rows - 1) return memo[row][c1][c2] = cherries;
            int best = 0;
            for (int d1 : dirs) {
                for (int d2 : dirs) {
                    int nc1 = c1 + d1, nc2 = c2 + d2;
                    if (nc1 >= 0 && nc1 < cols && nc2 >= 0 && nc2 < cols) {
                        best = Math.max(best, dp(row + 1, nc1, nc2));
                    }
                }
            }
            return memo[row][c1][c2] = cherries + best;
        }

        public static int solve(int[][] g) {
            grid = g;
            rows = g.length;
            cols = g[0].length;
            memo = new int[rows][cols][cols];
            for (int[][] a : memo) for (int[] b : a) Arrays.fill(b, -1);
            return dp(0, 0, cols - 1);
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Bottom-Up DP, Space-Optimized to 2D
    // Time: O(rows * cols^2)  |  Space: O(cols^2)
    // Process from last row upward. Roll curr/nxt 2D arrays.
    // ============================================================
    static class Best {

        public static int solve(int[][] grid) {
            int rows = grid.length, cols = grid[0].length;
            int[] dirs = {-1, 0, 1};
            final int NEG_INF = Integer.MIN_VALUE / 2;

            // Initialize last row
            int[][] curr = new int[cols][cols];
            for (int c1 = 0; c1 < cols; c1++) {
                for (int c2 = 0; c2 < cols; c2++) {
                    curr[c1][c2] = grid[rows - 1][c1]
                                 + (c1 != c2 ? grid[rows - 1][c2] : 0);
                }
            }

            // Fill from second-last row up to row 0
            for (int row = rows - 2; row >= 0; row--) {
                int[][] nxt = new int[cols][cols];
                for (int[] r : nxt) Arrays.fill(r, NEG_INF);

                for (int c1 = 0; c1 < cols; c1++) {
                    for (int c2 = 0; c2 < cols; c2++) {
                        int bestNext = NEG_INF;
                        for (int d1 : dirs) {
                            for (int d2 : dirs) {
                                int nc1 = c1 + d1, nc2 = c2 + d2;
                                if (nc1 >= 0 && nc1 < cols
                                 && nc2 >= 0 && nc2 < cols) {
                                    bestNext = Math.max(bestNext, curr[nc1][nc2]);
                                }
                            }
                        }
                        if (bestNext != NEG_INF) {
                            int cherries = grid[row][c1]
                                         + (c1 != c2 ? grid[row][c2] : 0);
                            nxt[c1][c2] = cherries + bestNext;
                        }
                    }
                }
                curr = nxt;
            }

            return curr[0][cols - 1];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Cherry Pickup II ===");

        int[][][] grids = {
            {{3,1,1},{2,5,1},{1,5,5},{2,1,1}},
            {{1,0,0,0,0,0,1},{2,0,0,0,0,3,0},{2,0,9,0,0,0,0},{0,3,0,5,4,0,0},{1,0,2,3,0,0,6}}
        };
        int[] expected = {24, 28};

        for (int i = 0; i < grids.length; i++) {
            int b = BruteForce.solve(grids[i]);
            int o = Optimal.solve(grids[i]);
            int bt = Best.solve(grids[i]);
            String ok = (b == expected[i] && o == expected[i] && bt == expected[i])
                        ? "OK" : "FAIL";
            System.out.printf("  Brute=%d, Optimal=%d, Best=%d, Expected=%d [%s]%n",
                b, o, bt, expected[i], ok);
        }
    }
}
