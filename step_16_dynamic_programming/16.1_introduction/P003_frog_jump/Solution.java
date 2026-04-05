/**
 * Problem: Frog Jump (Striver's DP Series)
 * Difficulty: EASY | XP: 10
 *
 * Frog at stair 0, can jump 1 or 2 stairs. Cost = |height[i] - height[j]|.
 * Find minimum cost to reach stair N-1.
 * All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(2^n) | Space: O(n)
// ============================================================
class RecursiveFrog {
    public int minCost(int[] height) {
        int n = height.length;
        return solve(n - 1, height);
    }

    private int solve(int i, int[] height) {
        if (i == 0) return 0;

        int oneJump = solve(i - 1, height) + Math.abs(height[i] - height[i - 1]);

        int twoJump = Integer.MAX_VALUE;
        if (i >= 2) {
            twoJump = solve(i - 2, height) + Math.abs(height[i] - height[i - 2]);
        }

        return Math.min(oneJump, twoJump);
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n) | Space: O(n)
// ============================================================
class MemoFrog {
    public int minCost(int[] height) {
        int n = height.length;
        int[] dp = new int[n];
        Arrays.fill(dp, -1);
        return solve(n - 1, height, dp);
    }

    private int solve(int i, int[] height, int[] dp) {
        if (i == 0) return 0;
        if (dp[i] != -1) return dp[i];

        int oneJump = solve(i - 1, height, dp) + Math.abs(height[i] - height[i - 1]);

        int twoJump = Integer.MAX_VALUE;
        if (i >= 2) {
            twoJump = solve(i - 2, height, dp) + Math.abs(height[i] - height[i - 2]);
        }

        dp[i] = Math.min(oneJump, twoJump);
        return dp[i];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(n) | Space: O(n)
// ============================================================
class TabFrog {
    public int minCost(int[] height) {
        int n = height.length;
        if (n == 1) return 0;

        int[] dp = new int[n];
        dp[0] = 0;
        dp[1] = Math.abs(height[1] - height[0]);

        for (int i = 2; i < n; i++) {
            int oneJump = dp[i - 1] + Math.abs(height[i] - height[i - 1]);
            int twoJump = dp[i - 2] + Math.abs(height[i] - height[i - 2]);
            dp[i] = Math.min(oneJump, twoJump);
        }

        return dp[n - 1];
    }
}

// ============================================================
// Approach 4: Space Optimized
// Time: O(n) | Space: O(1)
// ============================================================
class SpaceFrog {
    public int minCost(int[] height) {
        int n = height.length;
        if (n == 1) return 0;

        int prev2 = 0;                                  // dp[0]
        int prev1 = Math.abs(height[1] - height[0]);    // dp[1]

        for (int i = 2; i < n; i++) {
            int oneJump = prev1 + Math.abs(height[i] - height[i - 1]);
            int twoJump = prev2 + Math.abs(height[i] - height[i - 2]);
            int curr = Math.min(oneJump, twoJump);
            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Frog Jump ===\n");

        RecursiveFrog rec = new RecursiveFrog();
        MemoFrog memo = new MemoFrog();
        TabFrog tab = new TabFrog();
        SpaceFrog space = new SpaceFrog();

        int[][] heights = {
            {10, 20, 30, 10},
            {10, 50, 10},
            {20, 30, 40, 20},
            {7},
            {10, 10, 10, 10},
            {10, 20, 10, 20, 10},
        };
        int[] expected = {20, 0, 20, 0, 0, 0};

        for (int t = 0; t < heights.length; t++) {
            int r = rec.minCost(heights[t]);
            int m = memo.minCost(heights[t]);
            int tb = tab.minCost(heights[t]);
            int s = space.minCost(heights[t]);

            boolean pass = r == expected[t] && m == expected[t] && tb == expected[t] && s == expected[t];
            System.out.println("height = " + Arrays.toString(heights[t]));
            System.out.println("  Recursive: " + r + " | Memo: " + m + " | Tab: " + tb + " | Space: " + s);
            System.out.println("  Expected: " + expected[t] + " | Pass: " + pass + "\n");
        }
    }
}
