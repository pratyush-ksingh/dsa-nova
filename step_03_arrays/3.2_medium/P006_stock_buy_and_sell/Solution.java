/**
 * Problem: Stock Buy and Sell (Multiple Transactions Allowed)
 * Difficulty: EASY | XP: 10
 *
 * Find the maximum profit from unlimited buy/sell transactions.
 * Must sell before buying again.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Recursion -- try all states)
// Time: O(2^n) | Space: O(n) recursion stack
// ============================================================
class BruteForce {
    public static int maxProfit(int[] prices) {
        return solve(prices, 0, false);
    }

    private static int solve(int[] prices, int day, boolean holding) {
        if (day >= prices.length) return 0;

        // Option 1: skip this day
        int skip = solve(prices, day + 1, holding);

        if (holding) {
            // Option 2: sell today
            int sell = prices[day] + solve(prices, day + 1, false);
            return Math.max(skip, sell);
        } else {
            // Option 2: buy today
            int buy = -prices[day] + solve(prices, day + 1, true);
            return Math.max(skip, buy);
        }
    }
}

// ============================================================
// Approach 2: Optimal (Greedy -- sum positive differences)
// Time: O(n) | Space: O(1)
// ============================================================
class Optimal {
    public static int maxProfit(int[] prices) {
        int profit = 0;
        for (int i = 1; i < prices.length; i++) {
            if (prices[i] > prices[i - 1]) {
                profit += prices[i] - prices[i - 1];
            }
        }
        return profit;
    }
}

// ============================================================
// Approach 3: Best (Valley-Peak -- explicit trade pairs)
// Time: O(n) | Space: O(1)
// ============================================================
class Best {
    public static int maxProfit(int[] prices) {
        int n = prices.length;
        int i = 0;
        int profit = 0;

        while (i < n - 1) {
            // Find valley (local minimum)
            while (i < n - 1 && prices[i] >= prices[i + 1]) {
                i++;
            }
            int valley = prices[i];

            // Find peak (local maximum)
            while (i < n - 1 && prices[i] <= prices[i + 1]) {
                i++;
            }
            int peak = prices[i];

            profit += peak - valley;
        }
        return profit;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Stock Buy and Sell ===\n");

        int[][] testCases = {
            {7, 1, 5, 3, 6, 4},
            {1, 2, 3, 4, 5},
            {7, 6, 4, 3, 1},
            {5},
            {1, 5},
            {3, 3, 3},
            {1, 5, 1, 5}
        };
        int[] expected = {7, 4, 0, 0, 4, 0, 8};

        for (int t = 0; t < testCases.length; t++) {
            int bruteResult = BruteForce.maxProfit(testCases[t]);
            int optimalResult = Optimal.maxProfit(testCases[t]);
            int bestResult = Best.maxProfit(testCases[t]);

            boolean pass = bruteResult == expected[t]
                        && optimalResult == expected[t]
                        && bestResult == expected[t];

            System.out.println("Input:    " + Arrays.toString(testCases[t]));
            System.out.println("Brute:    " + bruteResult);
            System.out.println("Optimal:  " + optimalResult);
            System.out.println("Best:     " + bestResult);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
