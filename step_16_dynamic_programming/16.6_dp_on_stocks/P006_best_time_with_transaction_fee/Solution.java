import java.util.*;

/**
 * Problem: Best Time to Buy and Sell Stock with Transaction Fee (LeetCode #714)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursion with State
    // Time: O(2^n)  |  Space: O(n) recursion stack
    // ============================================================
    public static int bruteForce(int[] prices, int fee) {
        return solveBrute(prices, fee, 0, true);
    }

    private static int solveBrute(int[] prices, int fee, int i, boolean canBuy) {
        if (i >= prices.length) return 0;
        if (canBuy) {
            int buy = -prices[i] + solveBrute(prices, fee, i + 1, false);
            int skip = solveBrute(prices, fee, i + 1, true);
            return Math.max(buy, skip);
        } else {
            int sell = prices[i] - fee + solveBrute(prices, fee, i + 1, true);
            int skip = solveBrute(prices, fee, i + 1, false);
            return Math.max(sell, skip);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- DP with Buy/Sell States
    // Time: O(n)  |  Space: O(n)
    // ============================================================
    public static int optimal(int[] prices, int fee) {
        int n = prices.length;
        if (n <= 1) return 0;

        int[] buy = new int[n];
        int[] sell = new int[n];

        buy[0] = -prices[0];
        sell[0] = 0;

        for (int i = 1; i < n; i++) {
            buy[i] = Math.max(buy[i - 1], sell[i - 1] - prices[i]);
            sell[i] = Math.max(sell[i - 1], buy[i - 1] + prices[i] - fee);
        }

        return sell[n - 1];
    }

    // ============================================================
    // APPROACH 3: BEST -- Space-Optimized Two Variables
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    public static int best(int[] prices, int fee) {
        int n = prices.length;
        if (n <= 1) return 0;

        int hold = -prices[0];  // max profit when holding stock
        int cash = 0;           // max profit when not holding stock

        for (int i = 1; i < n; i++) {
            int newHold = Math.max(hold, cash - prices[i]);
            int newCash = Math.max(cash, hold + prices[i] - fee);
            hold = newHold;
            cash = newCash;
        }

        return cash;
    }

    public static void main(String[] args) {
        System.out.println("=== Best Time with Transaction Fee ===\n");

        System.out.println("Brute:   " + bruteForce(new int[]{1,3,2,8,4,9}, 2));  // 8
        System.out.println("Optimal: " + optimal(new int[]{1,3,2,8,4,9}, 2));      // 8
        System.out.println("Best:    " + best(new int[]{1,3,2,8,4,9}, 2));         // 8

        System.out.println("\nBrute:   " + bruteForce(new int[]{1,3,7,5,10,3}, 3)); // 6
        System.out.println("Optimal: " + optimal(new int[]{1,3,7,5,10,3}, 3));      // 6
        System.out.println("Best:    " + best(new int[]{1,3,7,5,10,3}, 3));         // 6

        System.out.println("\nSingle:     " + best(new int[]{1}, 1));                // 0
        System.out.println("Decreasing: " + best(new int[]{5,4,3,2,1}, 1));         // 0
    }
}
