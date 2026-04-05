/**
 * Problem: Climbing Stairs (LeetCode #70)
 * Difficulty: EASY | XP: 10
 *
 * N stairs, can climb 1 or 2 steps. How many distinct ways to reach the top?
 * All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(2^n) | Space: O(n)
// ============================================================
class RecursiveClimb {
    public int climbStairs(int n) {
        if (n <= 2) return n;
        return climbStairs(n - 1) + climbStairs(n - 2);
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n) | Space: O(n)
// ============================================================
class MemoClimb {
    public int climbStairs(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, -1);
        return solve(n, dp);
    }

    private int solve(int n, int[] dp) {
        if (n <= 2) return n;
        if (dp[n] != -1) return dp[n];

        dp[n] = solve(n - 1, dp) + solve(n - 2, dp);
        return dp[n];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(n) | Space: O(n)
// ============================================================
class TabClimb {
    public int climbStairs(int n) {
        if (n <= 2) return n;

        int[] dp = new int[n + 1];
        dp[1] = 1;
        dp[2] = 2;

        for (int i = 3; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
}

// ============================================================
// Approach 4: Space Optimized
// Time: O(n) | Space: O(1)
// ============================================================
class SpaceClimb {
    public int climbStairs(int n) {
        if (n <= 2) return n;

        int prev2 = 1; // ways(1)
        int prev1 = 2; // ways(2)

        for (int i = 3; i <= n; i++) {
            int curr = prev1 + prev2;
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
        System.out.println("=== Climbing Stairs ===\n");

        RecursiveClimb rec = new RecursiveClimb();
        MemoClimb memo = new MemoClimb();
        TabClimb tab = new TabClimb();
        SpaceClimb space = new SpaceClimb();

        int[] testCases = {1, 2, 3, 4, 5, 10, 20, 45};
        int[] expected =  {1, 2, 3, 5, 8, 89, 10946, 1836311903};

        System.out.printf("%-5s %-12s %-12s %-12s %-14s %-14s %-6s%n",
                "n", "Recursive", "Memo", "Tab", "SpaceOpt", "Expected", "Pass");
        System.out.println("-".repeat(75));

        for (int t = 0; t < testCases.length; t++) {
            int n = testCases[t];
            int r = (n <= 25) ? rec.climbStairs(n) : -1;
            int m = memo.climbStairs(n);
            int tb = tab.climbStairs(n);
            int s = space.climbStairs(n);

            boolean pass = m == expected[t] && tb == expected[t] && s == expected[t];
            if (n <= 25) pass = pass && (r == expected[t]);

            System.out.printf("%-5d %-12s %-12d %-12d %-14d %-14d %-6s%n",
                    n, (n <= 25 ? String.valueOf(r) : "skip"), m, tb, s, expected[t], pass);
        }
    }
}
