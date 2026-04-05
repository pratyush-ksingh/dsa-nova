import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Try all 3^N color assignments
// Time: O(3^N)  |  Space: O(N)
// Recursively try every color for each house, skip if same as prev
// ============================================================
class BruteForce {
    static int[][] costs;
    static int N, minCost;

    public static int solve(int[][] costMatrix) {
        costs = costMatrix;
        N = costMatrix.length;
        minCost = Integer.MAX_VALUE;
        backtrack(0, -1, 0);
        return minCost;
    }

    private static void backtrack(int house, int prevColor, int total) {
        if (house == N) { minCost = Math.min(minCost, total); return; }
        for (int color = 0; color < 3; color++) {
            if (color != prevColor) backtrack(house + 1, color, total + costs[house][color]);
        }
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Bottom-up DP
// Time: O(N)  |  Space: O(N)
// dp[i][c] = min cost to paint houses 0..i where house i is color c
// dp[i][c] = costs[i][c] + min(dp[i-1][other colors])
// ============================================================
class Optimal {
    public static int solve(int[][] costs) {
        int n = costs.length;
        if (n == 0) return 0;
        int[][] dp = new int[n][3];
        dp[0][0] = costs[0][0];
        dp[0][1] = costs[0][1];
        dp[0][2] = costs[0][2];
        for (int i = 1; i < n; i++) {
            dp[i][0] = costs[i][0] + Math.min(dp[i-1][1], dp[i-1][2]);
            dp[i][1] = costs[i][1] + Math.min(dp[i-1][0], dp[i-1][2]);
            dp[i][2] = costs[i][2] + Math.min(dp[i-1][0], dp[i-1][1]);
        }
        return Math.min(dp[n-1][0], Math.min(dp[n-1][1], dp[n-1][2]));
    }
}

// ============================================================
// APPROACH 3: BEST - Space-optimized DP (O(1) space)
// Time: O(N)  |  Space: O(1)
// Only need previous row, use 3 variables
// ============================================================
class Best {
    public static int solve(int[][] costs) {
        if (costs.length == 0) return 0;
        int r = costs[0][0], g = costs[0][1], b = costs[0][2];
        for (int i = 1; i < costs.length; i++) {
            int nr = costs[i][0] + Math.min(g, b);
            int ng = costs[i][1] + Math.min(r, b);
            int nb = costs[i][2] + Math.min(r, g);
            r = nr; g = ng; b = nb;
        }
        return Math.min(r, Math.min(g, b));
    }
}

public class Solution {
    public static void main(String[] args) {
        // IB example: [[17,2,17],[16,16,5],[14,3,19]] -> 10 (2+5+3)
        int[][] costs1 = {{17,2,17},{16,16,5},{14,3,19}};
        System.out.println("Test 1 expected 10:");
        System.out.println("  Brute=" + BruteForce.solve(costs1));
        System.out.println("  Optimal=" + Optimal.solve(costs1));
        System.out.println("  Best=" + Best.solve(costs1));

        int[][] costs2 = {{7,6,2}};
        System.out.println("Test 2 (1 house) expected 2:");
        System.out.println("  Brute=" + BruteForce.solve(costs2));
        System.out.println("  Optimal=" + Optimal.solve(costs2));
        System.out.println("  Best=" + Best.solve(costs2));
    }
}
