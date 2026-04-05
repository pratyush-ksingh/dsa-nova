/**
 * Problem: Potions
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * N potions in a row. Mixing two adjacent potions produces a new potion
 * whose strength = sum of both. If the resulting strength is NEGATIVE,
 * you must drink it, adding |strength| to your penalty.
 * Find the minimum penalty to mix all potions into one.
 *
 * Interval DP:
 *   prefix[i] = sum of potions[0..i-1]
 *   sum(i,j)  = prefix[j+1] - prefix[i]   (sum of subarray i..j)
 *   dp[i][j]  = min penalty to fully merge potions i..j into one
 *
 * When we split [i..j] at k, the cost of merging the two halves is:
 *   dp[i][k] + dp[k+1][j] + penalty(sum(i,j))
 * where penalty(x) = max(0, -x)  (drink it only if negative)
 *
 * Real-life use case: Matrix chain multiplication variant, optimal
 * merge order in scientific simulations with negative-cost merge scenarios.
 *
 * @author DSA_Nova
 */

import java.util.Arrays;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^3)  |  Space: O(n^2)
// Top-down recursion (no memoization). Explores all split points.
// ============================================================
class BruteForce {
    private int[] potions;
    private int[] prefix;

    private int sum(int i, int j) {
        return prefix[j + 1] - prefix[i];
    }

    private int penalty(int val) {
        return val < 0 ? -val : 0;
    }

    private int solve(int i, int j) {
        if (i == j) return 0;
        int best = Integer.MAX_VALUE;
        for (int k = i; k < j; k++) {
            int cost = solve(i, k) + solve(k + 1, j) + penalty(sum(i, j));
            best = Math.min(best, cost);
        }
        return best;
    }

    public int minPenalty(int[] p) {
        int n = p.length;
        if (n <= 1) return 0;
        potions = p;
        prefix = new int[n + 1];
        for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + p[i];
        return solve(0, n - 1);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(n^3)  |  Space: O(n^2)
// Top-down with memoization. Each (i,j) subproblem computed once.
// Note: the penalty for merging i..j is always fixed as penalty(sum(i,j))
// regardless of split point k, so it can be factored out.
// ============================================================
class Optimal {
    private int[] prefix;
    private int[][] memo;

    private int sum(int i, int j) {
        return prefix[j + 1] - prefix[i];
    }

    private int penalty(int val) {
        return val < 0 ? -val : 0;
    }

    private int solve(int i, int j) {
        if (i == j) return 0;
        if (memo[i][j] != -1) return memo[i][j];
        int best = Integer.MAX_VALUE;
        int pen = penalty(sum(i, j)); // same for all k
        for (int k = i; k < j; k++) {
            int cost = solve(i, k) + solve(k + 1, j) + pen;
            best = Math.min(best, cost);
        }
        return memo[i][j] = best;
    }

    public int minPenalty(int[] p) {
        int n = p.length;
        if (n <= 1) return 0;
        prefix = new int[n + 1];
        for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + p[i];
        memo = new int[n][n];
        for (int[] row : memo) Arrays.fill(row, -1);
        return solve(0, n - 1);
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(n^3)  |  Space: O(n^2)
// Bottom-up tabulation. Fill dp by increasing interval length.
// Avoids recursion overhead; same asymptotic complexity but better
// constant factors (no call stack, cache-friendly access).
// ============================================================
class Best {
    public int minPenalty(int[] p) {
        int n = p.length;
        if (n <= 1) return 0;

        // Build prefix sums
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + p[i];

        int[][] dp = new int[n][n];
        // Base: single element -> no cost
        // Length 1 intervals already 0

        // Fill by increasing length
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                int rangeSum = prefix[j + 1] - prefix[i];
                int pen = rangeSum < 0 ? -rangeSum : 0;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + pen;
                    dp[i][j] = Math.min(dp[i][j], cost);
                }
            }
        }
        return dp[0][n - 1];
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Potions ===");

        BruteForce bf = new BruteForce();
        Optimal opt = new Optimal();
        Best best = new Best();

        // Test 1: [1, -1, -1]
        // All positive total = -1, mixing 0+1 costs 0 (sum=0), then 0+2 costs 1 (sum=-1)
        // or mix 1+2 cost 0 (sum=-2 => 2), then 0 cost 0 (sum=-1 =>1) => total 3
        // Or mix all: only one order matters
        // [1, -1]: sum=0, no penalty; result=0; then [0, -1]: sum=-1, penalty=1 => total=1
        // [-1, -1]: sum=-2, penalty=2; then [1, -2]: sum=-1, penalty=1 => total=3
        // Best is first: 1
        int[] t1 = {1, -1, -1};
        System.out.println("Test 1 [1,-1,-1]:");
        System.out.println("  Brute:   " + bf.minPenalty(t1));   // 1
        System.out.println("  Optimal: " + opt.minPenalty(t1));  // 1
        System.out.println("  Best:    " + best.minPenalty(t1)); // 1

        // Test 2: [1, 2, 3] all positive, never negative -> penalty = 0
        int[] t2 = {1, 2, 3};
        System.out.println("\nTest 2 [1,2,3] (all positive):");
        System.out.println("  Brute:   " + bf.minPenalty(t2));   // 0
        System.out.println("  Optimal: " + opt.minPenalty(t2));  // 0
        System.out.println("  Best:    " + best.minPenalty(t2)); // 0

        // Test 3: [-1, -2, -3]
        // Any merge of a subarray produces negative sum, forced to drink
        // dp[0][0]=0, dp[1][1]=0, dp[2][2]=0
        // dp[0][1]: sum=-3, pen=3, split at 0: dp[0][0]+dp[1][1]+3=3 => 3
        // dp[1][2]: sum=-5, pen=5, split at 1: 0+0+5=5 => 5
        // dp[0][2]: sum=-6, pen=6
        //   split at 0: dp[0][0]+dp[1][2]+6=0+5+6=11
        //   split at 1: dp[0][1]+dp[2][2]+6=3+0+6=9
        // => 9
        int[] t3 = {-1, -2, -3};
        System.out.println("\nTest 3 [-1,-2,-3]:");
        System.out.println("  Brute:   " + bf.minPenalty(t3));   // 9
        System.out.println("  Optimal: " + opt.minPenalty(t3));  // 9
        System.out.println("  Best:    " + best.minPenalty(t3)); // 9

        // Test 4: single element
        int[] t4 = {5};
        System.out.println("\nTest 4 single [5]: " + best.minPenalty(t4)); // 0
    }
}
