/**
 * Problem: Best Time to Buy and Sell Stock III (LeetCode 123)
 * Difficulty: HARD | XP: 50
 *
 * Given prices[], complete at most 2 transactions to maximise profit.
 * Must sell before buying again. Return the maximum profit.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2)  |  Space: O(1)
    // Try every split point; sum best single-transaction from each half
    // ============================================================
    static class BruteForce {
        public int maxProfit(int[] prices) {
            int n = prices.length;
            if (n < 2) return 0;
            int best = 0;
            for (int k = 0; k < n; k++) {
                int left  = maxProfitRange(prices, 0, k);
                int right = maxProfitRange(prices, k, n - 1);
                best = Math.max(best, left + right);
            }
            return best;
        }

        private int maxProfitRange(int[] prices, int lo, int hi) {
            int minPrice = prices[lo], profit = 0;
            for (int i = lo; i <= hi; i++) {
                profit   = Math.max(profit, prices[i] - minPrice);
                minPrice = Math.min(minPrice, prices[i]);
            }
            return profit;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — DP with 4 state arrays
    // Time: O(n)  |  Space: O(n)
    // Explicit arrays buy1, sell1, buy2, sell2 for each day
    // ============================================================
    static class Optimal {
        public int maxProfit(int[] prices) {
            int n = prices.length;
            if (n < 2) return 0;

            int[] buy1  = new int[n];
            int[] sell1 = new int[n];
            int[] buy2  = new int[n];
            int[] sell2 = new int[n];

            buy1[0]  = -prices[0];
            sell1[0] = 0;
            buy2[0]  = -prices[0];
            sell2[0] = 0;

            for (int i = 1; i < n; i++) {
                buy1[i]  = Math.max(buy1[i - 1],  -prices[i]);
                sell1[i] = Math.max(sell1[i - 1], buy1[i - 1]  + prices[i]);
                buy2[i]  = Math.max(buy2[i - 1],  sell1[i - 1] - prices[i]);
                sell2[i] = Math.max(sell2[i - 1], buy2[i - 1]  + prices[i]);
            }
            return sell2[n - 1];
        }
    }

    // ============================================================
    // APPROACH 3: BEST — 4 scalar variables, O(1) space
    // Time: O(n)  |  Space: O(1)
    // Observe each state only depends on the previous day → compress
    // ============================================================
    static class Best {
        public int maxProfit(int[] prices) {
            int buy1  = Integer.MIN_VALUE; // best profit after first buy
            int sell1 = 0;                 // best profit after first sell
            int buy2  = Integer.MIN_VALUE; // best profit after second buy
            int sell2 = 0;                 // best profit after second sell

            for (int price : prices) {
                buy1  = Math.max(buy1,  -price);            // buy 1st time
                sell1 = Math.max(sell1, buy1  + price);     // sell 1st time
                buy2  = Math.max(buy2,  sell1 - price);     // buy 2nd time
                sell2 = Math.max(sell2, buy2  + price);     // sell 2nd time
            }
            return sell2;
        }
    }

    public static void main(String[] args) {
        int[][] inputs = {
            {3, 3, 5, 0, 0, 3, 1, 4},
            {1, 2, 3, 4, 5},
            {7, 6, 4, 3, 1},
            {1},
            {1, 2},
            {6, 1, 3, 2, 4, 7}
        };
        int[] expected = {6, 4, 0, 0, 1, 7};

        BruteForce bf = new BruteForce();
        Optimal op = new Optimal();
        Best bt = new Best();

        System.out.println("=== Best Time to Buy and Sell Stock III ===");
        for (int t = 0; t < inputs.length; t++) {
            int b  = bf.maxProfit(inputs[t]);
            int o  = op.maxProfit(inputs[t]);
            int bs = bt.maxProfit(inputs[t]);
            String status = (b == expected[t] && o == expected[t] && bs == expected[t]) ? "PASS" : "FAIL";
            System.out.printf("[%s] brute=%d optimal=%d best=%d expected=%d%n",
                    status, b, o, bs, expected[t]);
        }
    }
}
