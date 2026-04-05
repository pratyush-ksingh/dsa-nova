import java.util.*;

/**
 * Problem: Coin Change II - Count Ways (LeetCode 518)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursion
    // Time: O(2^(amount/min_coin))  |  Space: O(amount) stack
    // Count all combinations (order doesn't matter) to make amount.
    // ============================================================
    public static int bruteForce(int[] coins, int amount) {
        return solveBrute(coins, coins.length - 1, amount);
    }

    private static int solveBrute(int[] coins, int idx, int remaining) {
        if (remaining == 0) return 1;
        if (idx == 0) return remaining % coins[0] == 0 ? 1 : 0;
        int notTake = solveBrute(coins, idx - 1, remaining);
        int take = 0;
        if (coins[idx] <= remaining) {
            take = solveBrute(coins, idx, remaining - coins[idx]);
        }
        return notTake + take;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- 2D DP Tabulation
    // Time: O(n * amount)  |  Space: O(n * amount)
    // dp[i][a] = number of ways to make amount a using coins 0..i.
    // ============================================================
    public static int optimal(int[] coins, int amount) {
        int n = coins.length;
        int[][] dp = new int[n][amount + 1];

        // amount = 0 has 1 way (pick nothing)
        for (int i = 0; i < n; i++) dp[i][0] = 1;

        // Base: only coin[0]
        for (int a = 1; a <= amount; a++) {
            if (a % coins[0] == 0) dp[0][a] = 1;
        }

        for (int i = 1; i < n; i++) {
            for (int a = 0; a <= amount; a++) {
                int notTake = dp[i - 1][a];
                int take = 0;
                if (coins[i] <= a) {
                    take = dp[i][a - coins[i]]; // same row = reuse
                }
                dp[i][a] = notTake + take;
            }
        }

        return dp[n - 1][amount];
    }

    // ============================================================
    // APPROACH 3: BEST -- 1D Space-Optimized DP
    // Time: O(n * amount)  |  Space: O(amount)
    // Left-to-right ensures combinations (not permutations), reuse allowed.
    // ============================================================
    public static int best(int[] coins, int amount) {
        int[] dp = new int[amount + 1];
        dp[0] = 1; // one way to make 0

        for (int coin : coins) {
            for (int a = coin; a <= amount; a++) { // left to right
                dp[a] += dp[a - coin];
            }
        }

        return dp[amount];
    }

    public static void main(String[] args) {
        System.out.println("=== Coin Change II - Count Ways ===\n");

        int[] coins1 = {1, 2, 5};
        System.out.println("Brute:   " + bruteForce(coins1, 5));   // 4
        System.out.println("Optimal: " + optimal(coins1, 5));       // 4
        System.out.println("Best:    " + best(coins1, 5));          // 4

        int[] coins2 = {2};
        System.out.println("\nBrute:   " + bruteForce(coins2, 3));  // 0
        System.out.println("Optimal: " + optimal(coins2, 3));       // 0
        System.out.println("Best:    " + best(coins2, 3));          // 0

        System.out.println("\nSingle coin: " + best(new int[]{10}, 10));   // 1
        System.out.println("Zero amount: " + best(new int[]{1, 2, 5}, 0)); // 1
    }
}
