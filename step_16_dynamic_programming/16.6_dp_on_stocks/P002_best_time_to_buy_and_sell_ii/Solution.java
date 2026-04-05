/**
 * Problem: Best Time to Buy and Sell Stock II (LeetCode #122)
 * Difficulty: MEDIUM | XP: 25
 *
 * Unlimited transactions. Maximize profit.
 * All 4 DP approaches + Greedy: Recursion -> Memo -> Tab -> Space -> Greedy
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(2^n) | Space: O(n)
// ============================================================
class RecursiveStock {
    public int maxProfit(int[] prices) {
        return solve(0, 1, prices);
    }

    private int solve(int i, int canBuy, int[] prices) {
        if (i == prices.length) return 0;

        if (canBuy == 1) {
            // Buy today or skip
            int buy = -prices[i] + solve(i + 1, 0, prices);
            int skip = solve(i + 1, 1, prices);
            return Math.max(buy, skip);
        } else {
            // Sell today or skip
            int sell = prices[i] + solve(i + 1, 1, prices);
            int skip = solve(i + 1, 0, prices);
            return Math.max(sell, skip);
        }
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n) | Space: O(n)
// ============================================================
class MemoStock {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        int[][] dp = new int[n][2];
        for (int[] row : dp) Arrays.fill(row, -1);
        return solve(0, 1, prices, dp);
    }

    private int solve(int i, int canBuy, int[] prices, int[][] dp) {
        if (i == prices.length) return 0;
        if (dp[i][canBuy] != -1) return dp[i][canBuy];

        if (canBuy == 1) {
            int buy = -prices[i] + solve(i + 1, 0, prices, dp);
            int skip = solve(i + 1, 1, prices, dp);
            dp[i][canBuy] = Math.max(buy, skip);
        } else {
            int sell = prices[i] + solve(i + 1, 1, prices, dp);
            int skip = solve(i + 1, 0, prices, dp);
            dp[i][canBuy] = Math.max(sell, skip);
        }
        return dp[i][canBuy];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(n) | Space: O(n)
// ============================================================
class TabStock {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        // dp[i][0] = max profit from day i when holding stock
        // dp[i][1] = max profit from day i when can buy
        int[][] dp = new int[n + 1][2];

        // Base case: day n -> 0 profit
        // dp[n][0] = 0; dp[n][1] = 0; (already 0)

        for (int i = n - 1; i >= 0; i--) {
            // Can buy (canBuy = 1)
            dp[i][1] = Math.max(-prices[i] + dp[i + 1][0], dp[i + 1][1]);
            // Holding (canBuy = 0)
            dp[i][0] = Math.max(prices[i] + dp[i + 1][1], dp[i + 1][0]);
        }

        return dp[0][1]; // Start at day 0, can buy
    }
}

// ============================================================
// Approach 4a: Space Optimized DP
// Time: O(n) | Space: O(1)
// ============================================================
class SpaceStock {
    public int maxProfit(int[] prices) {
        int n = prices.length;
        int aheadBuy = 0, aheadSell = 0; // dp[i+1][1], dp[i+1][0]

        for (int i = n - 1; i >= 0; i--) {
            int currBuy = Math.max(-prices[i] + aheadSell, aheadBuy);
            int currSell = Math.max(prices[i] + aheadBuy, aheadSell);
            aheadBuy = currBuy;
            aheadSell = currSell;
        }

        return aheadBuy; // dp[0][1]
    }
}

// ============================================================
// Approach 4b: Greedy (Collect all upswings)
// Time: O(n) | Space: O(1)
// ============================================================
class GreedyStock {
    public int maxProfit(int[] prices) {
        int profit = 0;
        for (int i = 1; i < prices.length; i++) {
            // Collect every positive difference
            if (prices[i] > prices[i - 1]) {
                profit += prices[i] - prices[i - 1];
            }
        }
        return profit;
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Best Time to Buy and Sell Stock II ===\n");

        RecursiveStock rec = new RecursiveStock();
        MemoStock memo = new MemoStock();
        TabStock tab = new TabStock();
        SpaceStock space = new SpaceStock();
        GreedyStock greedy = new GreedyStock();

        int[][] prices = {
            {7, 1, 5, 3, 6, 4},
            {1, 2, 3, 4, 5},
            {7, 6, 4, 3, 1},
            {5},
            {1, 5},
            {3, 3, 3, 3},
            {1, 5, 1, 5},
        };
        int[] expected = {7, 4, 0, 0, 4, 0, 8};

        for (int t = 0; t < prices.length; t++) {
            int r = rec.maxProfit(prices[t]);
            int m = memo.maxProfit(prices[t]);
            int tb = tab.maxProfit(prices[t]);
            int s = space.maxProfit(prices[t]);
            int g = greedy.maxProfit(prices[t]);

            boolean pass = r == expected[t] && m == expected[t]
                    && tb == expected[t] && s == expected[t] && g == expected[t];

            System.out.println("prices=" + Arrays.toString(prices[t]));
            System.out.println("  Rec=" + r + " | Memo=" + m + " | Tab=" + tb
                    + " | Space=" + s + " | Greedy=" + g);
            System.out.println("  Expected=" + expected[t] + " | Pass=" + pass + "\n");
        }
    }
}
