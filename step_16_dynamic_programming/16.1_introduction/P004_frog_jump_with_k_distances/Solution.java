/**
 * Problem: Frog Jump with K Distances
 * Difficulty: MEDIUM | XP: 25
 *
 * Min cost for frog to reach stone n-1 with jumps of 1..k.
 * All 4 DP approaches + Monotonic Deque optimization.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(k^n) | Space: O(n) recursion stack
// ============================================================
class Recursive {
    public int minCost(int[] height, int k) {
        return solve(height, k, 0);
    }

    private int solve(int[] height, int k, int i) {
        if (i == height.length - 1) return 0;

        int minCost = Integer.MAX_VALUE;
        for (int j = 1; j <= k && i + j < height.length; j++) {
            int cost = solve(height, k, i + j) + Math.abs(height[i] - height[i + j]);
            minCost = Math.min(minCost, cost);
        }
        return minCost;
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n * k) | Space: O(n)
// ============================================================
class Memoization {
    public int minCost(int[] height, int k) {
        int n = height.length;
        int[] dp = new int[n];
        Arrays.fill(dp, -1);
        return solve(height, k, 0, dp);
    }

    private int solve(int[] height, int k, int i, int[] dp) {
        if (i == height.length - 1) return 0;
        if (dp[i] != -1) return dp[i];

        int minCost = Integer.MAX_VALUE;
        for (int j = 1; j <= k && i + j < height.length; j++) {
            int cost = solve(height, k, i + j, dp) + Math.abs(height[i] - height[i + j]);
            minCost = Math.min(minCost, cost);
        }
        dp[i] = minCost;
        return dp[i];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(n * k) | Space: O(n)
// ============================================================
class Tabulation {
    public int minCost(int[] height, int k) {
        int n = height.length;
        if (n == 1) return 0;

        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i < n; i++) {
            for (int j = 1; j <= k && i - j >= 0; j++) {
                dp[i] = Math.min(dp[i], dp[i - j] + Math.abs(height[i] - height[i - j]));
            }
        }
        return dp[n - 1];
    }
}

// ============================================================
// Approach 4: Space Optimized (Circular Buffer)
// Time: O(n * k) | Space: O(k)
// ============================================================
class SpaceOptimized {
    public int minCost(int[] height, int k) {
        int n = height.length;
        if (n == 1) return 0;

        // Circular buffer of size k+1
        int[] window = new int[k + 1];
        Arrays.fill(window, Integer.MAX_VALUE);
        window[0] = 0;

        for (int i = 1; i < n; i++) {
            int best = Integer.MAX_VALUE;
            for (int j = 1; j <= k && i - j >= 0; j++) {
                int prevIdx = (i - j) % (k + 1);
                best = Math.min(best, window[prevIdx] + Math.abs(height[i] - height[i - j]));
            }
            window[i % (k + 1)] = best;
        }
        return window[(n - 1) % (k + 1)];
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Frog Jump with K Distances ===\n");

        Recursive rec = new Recursive();
        Memoization memo = new Memoization();
        Tabulation tab = new Tabulation();
        SpaceOptimized space = new SpaceOptimized();

        int[][] heights = {
            {10, 30, 40, 20},
            {10, 20, 10},
            {30, 10, 60, 10, 60, 50},
            {10},
            {10, 10, 10, 10, 10}
        };
        int[] ks = {2, 1, 2, 3, 4};

        System.out.printf("%-30s %-4s %-10s %-10s %-10s %-10s%n",
                "Heights", "k", "Recurse", "Memo", "Tab", "SpaceOpt");
        System.out.println("-".repeat(75));

        for (int t = 0; t < heights.length; t++) {
            int r = rec.minCost(heights[t], ks[t]);
            int m = memo.minCost(heights[t], ks[t]);
            int tb = tab.minCost(heights[t], ks[t]);
            int s = space.minCost(heights[t], ks[t]);

            boolean pass = (r == m) && (m == tb) && (tb == s);
            System.out.printf("%-30s %-4d %-10d %-10d %-10d %-10d %s%n",
                    Arrays.toString(heights[t]), ks[t], r, m, tb, s, pass ? "PASS" : "FAIL");
        }

        // Edge cases
        System.out.println("\n--- Edge Cases ---");
        System.out.println("Single stone: " + tab.minCost(new int[]{5}, 3) + " (expected 0)");
        System.out.println("k=1 forced: " + tab.minCost(new int[]{1, 5, 1, 5, 1}, 1));
        System.out.println("All same: " + tab.minCost(new int[]{7, 7, 7, 7}, 2) + " (expected 0)");
    }
}
