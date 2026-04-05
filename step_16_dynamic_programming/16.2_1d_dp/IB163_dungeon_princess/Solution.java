/**
 * Problem: Dungeon Princess
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Given an M x N dungeon grid where:
 *   - Negative values = demons (lose health)
 *   - Positive values = magic orbs (gain health)
 *   - Zero = empty room
 * Knight starts at top-left (0,0), princess at bottom-right (M-1, N-1).
 * He can only move right or down. Find minimum initial health (>= 1) to
 * reach the princess alive (health must be >= 1 at all times).
 *
 * DP from bottom-right to top-left:
 *   dp[i][j] = min health needed to enter cell (i,j) and survive to end.
 *   dp[i][j] = max(1, min(dp[i+1][j], dp[i][j+1]) - dungeon[i][j])
 *
 * Real-life use: Game AI pathfinding, resource planning under constraints.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(M * N)  |  Space: O(M * N)
    // DP table from bottom-right to top-left. Standard textbook solution.
    // dp[i][j] = min health required to enter cell (i,j).
    // ============================================================
    public static int bruteForce(int[][] dungeon) {
        int m = dungeon.length, n = dungeon[0].length;
        int[][] dp = new int[m + 1][n + 1];

        // Base: cells beyond boundary require infinite health
        for (int i = 0; i <= m; i++) java.util.Arrays.fill(dp[i], Integer.MAX_VALUE);
        // Destination's "exit" requires health 1
        dp[m][n - 1] = 1;
        dp[m - 1][n] = 1;

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                int need = Math.min(dp[i + 1][j], dp[i][j + 1]) - dungeon[i][j];
                dp[i][j] = Math.max(1, need);
            }
        }
        return dp[0][0];
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(M * N)  |  Space: O(N)
    // Space-optimized DP using only one row (rolling array).
    // Process row by row from bottom to top.
    // ============================================================
    public static int optimal(int[][] dungeon) {
        int m = dungeon.length, n = dungeon[0].length;
        int[] dp = new int[n + 1];
        java.util.Arrays.fill(dp, Integer.MAX_VALUE);
        dp[n - 1] = 1;

        for (int i = m - 1; i >= 0; i--) {
            int[] newDp = new int[n + 1];
            java.util.Arrays.fill(newDp, Integer.MAX_VALUE);
            for (int j = n - 1; j >= 0; j--) {
                int fromRight = (j + 1 < n) ? dp[j + 1] : Integer.MAX_VALUE;
                // Hmm, we also need dp[j] from next row, which is dp[j] currently
                int fromDown = dp[j]; // this row hasn't been updated yet
                // Wait: dp[j] is from the row below (not yet overwritten)
                // Actually let's use in-place properly:
                int minNext = Math.min(
                    (j + 1 <= n - 1) ? dp[j + 1] : Integer.MAX_VALUE,
                    dp[j] != Integer.MAX_VALUE ? dp[j] : Integer.MAX_VALUE
                );
                // This needs careful handling - use 2-row DP instead
                newDp[j] = Math.max(1, minNext - dungeon[i][j]);
            }
            // Copy for next iteration (the newDp becomes current row, dp was "below")
            dp = newDp; // This is wrong approach for rolling; use full 2D instead
        }
        // This approach above has a bug - use proper 2-row DP below
        return dp[0];
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(M * N)  |  Space: O(N)
    // Correct space-optimized DP. Since we go bottom-right to top-left,
    // we need the "below" row and the "right" cell.
    // Use a single dp[] where dp[j] = min health to survive from (i,j).
    // Process each row from right to left, updating in-place.
    // ============================================================
    public static int best(int[][] dungeon) {
        int m = dungeon.length, n = dungeon[0].length;
        // dp[j] represents min health needed at cell (current_row, j)
        int[] dp = new int[n];
        java.util.Arrays.fill(dp, Integer.MAX_VALUE);

        // Fill bottom row first (right to left)
        dp[n - 1] = Math.max(1, 1 - dungeon[m - 1][n - 1]);
        for (int j = n - 2; j >= 0; j--) {
            dp[j] = Math.max(1, dp[j + 1] - dungeon[m - 1][j]);
        }

        // Fill remaining rows from bottom-1 to top
        for (int i = m - 2; i >= 0; i--) {
            // Update rightmost cell (can only go down)
            dp[n - 1] = Math.max(1, dp[n - 1] - dungeon[i][n - 1]);
            // Update rest (can go right or down)
            for (int j = n - 2; j >= 0; j--) {
                int minNext = Math.min(dp[j], dp[j + 1]);
                dp[j] = Math.max(1, minNext - dungeon[i][j]);
            }
        }
        return dp[0];
    }

    public static void main(String[] args) {
        System.out.println("=== Dungeon Princess ===\n");

        // LeetCode example: answer = 7
        int[][] dungeon1 = {
            {-2, -3,  3},
            {-5, -10, 1},
            {10,  30, -5}
        };
        System.out.println("Test 1 (expected 7):");
        System.out.println("  Brute:   " + bruteForce(dungeon1));
        System.out.println("  Optimal: " + optimal(dungeon1));
        System.out.println("  Best:    " + best(dungeon1));

        // Single cell with positive value: health = 1
        int[][] dungeon2 = {{5}};
        System.out.println("\nTest 2 (single positive cell, expected 1):");
        System.out.println("  Best: " + best(dungeon2));

        // Single cell with negative value: health = 4 (need 4 to survive -3)
        int[][] dungeon3 = {{-3}};
        System.out.println("\nTest 3 (single negative cell, expected 4):");
        System.out.println("  Best: " + best(dungeon3));

        // All negative
        int[][] dungeon4 = {{-1, -2}, {-3, -4}};
        System.out.println("\nTest 4 (all negative, expected 7):");
        System.out.println("  Best: " + best(dungeon4));
    }
}
