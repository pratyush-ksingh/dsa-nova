/**
 * Problem: Best Time to Buy and Sell Stock I (LeetCode #121)
 * Difficulty: EASY | XP: 10
 *
 * Given array prices, find max profit from one buy and one sell.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (All Pairs)
// Time: O(n^2) | Space: O(1)
// ============================================================
class BruteStock {
    public int maxProfit(int[] prices) {
        int maxProfit = 0;
        int n = prices.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                int profit = prices[j] - prices[i];
                maxProfit = Math.max(maxProfit, profit);
            }
        }

        return maxProfit;
    }
}

// ============================================================
// Approach 2: Optimal (Single Pass with Running Minimum)
// Time: O(n) | Space: O(1)
// ============================================================
class OptimalStock {
    public int maxProfit(int[] prices) {
        int minPrice = prices[0];
        int maxProfit = 0;

        for (int i = 1; i < prices.length; i++) {
            // Could we profit by selling today?
            int profitToday = prices[i] - minPrice;
            maxProfit = Math.max(maxProfit, profitToday);

            // Update minimum price seen so far
            minPrice = Math.min(minPrice, prices[i]);
        }

        return maxProfit;
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Best Time to Buy and Sell Stock I ===\n");

        BruteStock brute = new BruteStock();
        OptimalStock optimal = new OptimalStock();

        int[][] testCases = {
            {7, 1, 5, 3, 6, 4},
            {7, 6, 4, 3, 1},
            {2, 4, 1},
            {1, 2},
            {5},
            {3, 3, 3, 3},
            {1, 2, 3, 4, 5},
        };
        int[] expected = {5, 0, 2, 1, 0, 0, 4};

        for (int t = 0; t < testCases.length; t++) {
            int b = brute.maxProfit(testCases[t]);
            int o = optimal.maxProfit(testCases[t]);

            boolean pass = b == expected[t] && o == expected[t];
            System.out.println("prices = " + Arrays.toString(testCases[t]));
            System.out.println("  Brute: " + b + " | Optimal: " + o
                    + " | Expected: " + expected[t] + " | Pass: " + pass);
            System.out.println();
        }
    }
}
