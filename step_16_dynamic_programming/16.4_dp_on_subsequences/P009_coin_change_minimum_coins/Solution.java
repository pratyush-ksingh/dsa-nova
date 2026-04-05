import java.util.*;

/**
 * Problem: Coin Change - Minimum Coins (LeetCode 322)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    private static final int INF = Integer.MAX_VALUE / 2;

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursion
    // Time: O(amount^n)  |  Space: O(amount) recursion stack
    // ============================================================
    public static int bruteForce(int[] coins, int amount) {
        int ans = solveBrute(coins, coins.length - 1, amount);
        return ans >= INF ? -1 : ans;
    }

    private static int solveBrute(int[] coins, int idx, int remaining) {
        if (remaining == 0) return 0;
        if (idx == 0) {
            return remaining % coins[0] == 0 ? remaining / coins[0] : INF;
        }
        int notTake = solveBrute(coins, idx - 1, remaining);
        int take = INF;
        if (coins[idx] <= remaining) {
            int sub = solveBrute(coins, idx, remaining - coins[idx]);
            if (sub != INF) take = 1 + sub;
        }
        return Math.min(notTake, take);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- 2D DP Tabulation
    // Time: O(n * amount)  |  Space: O(n * amount)
    // dp[i][a] = min coins to make amount a using coins 0..i.
    // ============================================================
    public static int optimal(int[] coins, int amount) {
        int n = coins.length;
        int[][] dp = new int[n][amount + 1];

        for (int[] row : dp) Arrays.fill(row, INF);
        for (int i = 0; i < n; i++) dp[i][0] = 0;

        // Base: only coin[0]
        for (int a = 1; a <= amount; a++) {
            if (a % coins[0] == 0) dp[0][a] = a / coins[0];
        }

        for (int i = 1; i < n; i++) {
            for (int a = 0; a <= amount; a++) {
                int notTake = dp[i - 1][a];
                int take = INF;
                if (coins[i] <= a && dp[i][a - coins[i]] != INF) {
                    take = 1 + dp[i][a - coins[i]];
                }
                dp[i][a] = Math.min(notTake, take);
            }
        }

        return dp[n - 1][amount] >= INF ? -1 : dp[n - 1][amount];
    }

    // ============================================================
    // APPROACH 3: BEST -- 1D Space-Optimized DP
    // Time: O(n * amount)  |  Space: O(amount)
    // Left-to-right allows reuse of same coin (unbounded).
    // ============================================================
    public static int best(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int coin : coins) {
            for (int a = coin; a <= amount; a++) { // left to right
                if (dp[a - coin] != INF) {
                    dp[a] = Math.min(dp[a], 1 + dp[a - coin]);
                }
            }
        }

        return dp[amount] >= INF ? -1 : dp[amount];
    }

    public static void main(String[] args) {
        System.out.println("=== Coin Change - Minimum Coins ===\n");

        int[] coins1 = {1, 5, 6, 9};
        System.out.println("Brute:   " + bruteForce(coins1, 11));   // 2
        System.out.println("Optimal: " + optimal(coins1, 11));       // 2
        System.out.println("Best:    " + best(coins1, 11));          // 2

        int[] coins2 = {1, 2, 5};
        System.out.println("\nBrute:   " + bruteForce(coins2, 11));  // 3
        System.out.println("Optimal: " + optimal(coins2, 11));       // 3
        System.out.println("Best:    " + best(coins2, 11));          // 3

        System.out.println("\nImpossible: " + best(new int[]{2}, 3));       // -1
        System.out.println("Zero amount: " + best(new int[]{1, 2, 5}, 0)); // 0
    }
}
