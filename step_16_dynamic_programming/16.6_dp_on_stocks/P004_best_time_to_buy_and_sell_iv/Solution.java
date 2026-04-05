/**
 * Problem: Best Time to Buy and Sell Stock IV
 * Difficulty: HARD | XP: 50
 * LeetCode: 188
 *
 * You are given an integer k and an array prices where prices[i] is the price
 * of a stock on day i. Find the maximum profit using at most k transactions.
 * You may not engage in multiple transactions simultaneously (must sell before buying).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Pure Recursion
    // Time: O(2^n)  |  Space: O(n) recursion stack
    // ============================================================
    static class BruteForce {
        private int[] prices;
        private int n;

        /**
         * At each day, branch on two choices:
         *   - If holding stock: sell today OR hold (skip).
         *   - If not holding: buy today OR skip.
         * A transaction is counted when we SELL.
         */
        public int maxProfit(int k, int[] prices) {
            this.prices = prices;
            this.n = prices.length;
            return recurse(0, k, false);
        }

        private int recurse(int day, int txnsLeft, boolean holding) {
            if (day == n || txnsLeft == 0) return 0;

            // Option 1: do nothing
            int result = recurse(day + 1, txnsLeft, holding);

            if (holding) {
                // Option 2: sell today (completes a transaction)
                result = Math.max(result, prices[day] + recurse(day + 1, txnsLeft - 1, false));
            } else {
                // Option 2: buy today
                result = Math.max(result, -prices[day] + recurse(day + 1, txnsLeft, true));
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Tabulation DP
    // Time: O(n * k)  |  Space: O(n * k)
    // ============================================================
    static class Optimal {
        /**
         * State: dp[t][hold]
         *   t    = transactions remaining (1..k)
         *   hold = 0 (not holding) | 1 (holding stock)
         *
         * Fill from day n-1 down to day 0 using rolling array (only need
         * "next day" values). A sell completes a transaction (decrements t).
         *
         * Special case: k >= n/2 means unlimited transactions -> greedy.
         */
        public int maxProfit(int k, int[] prices) {
            int n = prices.length;
            if (n == 0 || k == 0) return 0;

            // Unlimited transactions
            if (k >= n / 2) {
                return unlimitedGreedy(prices);
            }

            // dp[t][0/1]: profit with t transactions left, not holding / holding
            int[][] dp = new int[k + 1][2];

            for (int day = n - 1; day >= 0; day--) {
                int[][] newDp = new int[k + 1][2];
                for (int t = 1; t <= k; t++) {
                    // Not holding: skip or buy
                    newDp[t][0] = Math.max(dp[t][0], -prices[day] + dp[t][1]);
                    // Holding: skip or sell (uses up one transaction)
                    newDp[t][1] = Math.max(dp[t][1], prices[day] + dp[t - 1][0]);
                }
                dp = newDp;
            }
            return dp[k][0];
        }

        private int unlimitedGreedy(int[] prices) {
            int profit = 0;
            for (int i = 1; i < prices.length; i++) {
                if (prices[i] > prices[i - 1]) profit += prices[i] - prices[i - 1];
            }
            return profit;
        }
    }

    // ============================================================
    // APPROACH 3: BEST - Space-Optimized O(k) DP
    // Time: O(n * k)  |  Space: O(k)
    // ============================================================
    static class Best {
        /**
         * Track buy[t] and sell[t] arrays of size k+1.
         *   buy[t]  = best (most profitable) state after completing the t-th BUY
         *             (stored as net cash: sell_t-1_profit minus purchase price)
         *   sell[t] = best profit after completing the t-th SELL
         *
         * For each price p (left to right):
         *   buy[t]  = max(buy[t],  sell[t-1] - p)
         *   sell[t] = max(sell[t], buy[t] + p)
         *
         * Initialize buy[t] = MIN_VALUE (never bought yet), sell[t] = 0.
         * Answer is sell[k].
         *
         * Special case k >= n/2 -> O(n) greedy.
         */
        public int maxProfit(int k, int[] prices) {
            int n = prices.length;
            if (n == 0 || k == 0) return 0;

            if (k >= n / 2) {
                int profit = 0;
                for (int i = 1; i < n; i++) {
                    if (prices[i] > prices[i - 1]) profit += prices[i] - prices[i - 1];
                }
                return profit;
            }

            int[] buy  = new int[k + 1];
            int[] sell = new int[k + 1];

            for (int t = 0; t <= k; t++) {
                buy[t] = Integer.MIN_VALUE;
                sell[t] = 0;
            }

            for (int price : prices) {
                for (int t = 1; t <= k; t++) {
                    if (sell[t - 1] != Integer.MIN_VALUE) {
                        buy[t] = Math.max(buy[t], sell[t - 1] - price);
                    }
                    if (buy[t] != Integer.MIN_VALUE) {
                        sell[t] = Math.max(sell[t], buy[t] + price);
                    }
                }
            }

            return sell[k];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Best Time to Buy and Sell Stock IV ===\n");

        int[][] testPrices = {{2, 4, 1}, {3, 2, 6, 5, 0, 3}, {1, 2}};
        int[] testK        = {2,         2,                   1};
        int[] expected     = {2,         7,                   1};

        BruteForce bf = new BruteForce();
        Optimal    op = new Optimal();
        Best       be = new Best();

        for (int i = 0; i < testPrices.length; i++) {
            int k = testK[i];
            int[] p = testPrices[i];
            int bfRes = bf.maxProfit(k, p);
            int opRes = op.maxProfit(k, p);
            int beRes = be.maxProfit(k, p);
            String status = (bfRes == opRes && opRes == beRes && beRes == expected[i]) ? "OK" : "MISMATCH";
            System.out.printf("k=%d, prices=%s => Brute=%d, Optimal=%d, Best=%d | Expected=%d [%s]%n",
                k, java.util.Arrays.toString(p), bfRes, opRes, beRes, expected[i], status);
        }
    }
}
