import java.util.*;

/**
 * Problem: Best Time to Buy and Sell Stock with Cooldown (LeetCode #309)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursion with State
    // Time: O(2^n)  |  Space: O(n) recursion stack
    // ============================================================
    public static int bruteForce(int[] prices) {
        return solveBrute(prices, 0, true);
    }

    private static int solveBrute(int[] prices, int i, boolean canBuy) {
        if (i >= prices.length) return 0;
        if (canBuy) {
            int buy = -prices[i] + solveBrute(prices, i + 1, false);
            int skip = solveBrute(prices, i + 1, true);
            return Math.max(buy, skip);
        } else {
            int sell = prices[i] + solveBrute(prices, i + 2, true); // i+2 cooldown
            int skip = solveBrute(prices, i + 1, false);
            return Math.max(sell, skip);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- DP with Three States
    // Time: O(n)  |  Space: O(n)
    // buy[i], sell[i], cool[i] arrays.
    // ============================================================
    public static int optimal(int[] prices) {
        int n = prices.length;
        if (n <= 1) return 0;

        int[] buy = new int[n];
        int[] sell = new int[n];
        int[] cool = new int[n];

        buy[0] = -prices[0];
        sell[0] = 0;
        cool[0] = 0;

        for (int i = 1; i < n; i++) {
            buy[i] = Math.max(buy[i - 1], cool[i - 1] - prices[i]);
            sell[i] = buy[i - 1] + prices[i];
            cool[i] = Math.max(cool[i - 1], sell[i - 1]);
        }

        return Math.max(sell[n - 1], cool[n - 1]);
    }

    // ============================================================
    // APPROACH 3: BEST -- Space-Optimized 3 Variables
    // Time: O(n)  |  Space: O(1)
    // ============================================================
    public static int best(int[] prices) {
        int n = prices.length;
        if (n <= 1) return 0;

        int prevBuy = -prices[0];
        int prevSell = 0;
        int prevCool = 0;

        for (int i = 1; i < n; i++) {
            int currBuy = Math.max(prevBuy, prevCool - prices[i]);
            int currSell = prevBuy + prices[i];
            int currCool = Math.max(prevCool, prevSell);

            prevBuy = currBuy;
            prevSell = currSell;
            prevCool = currCool;
        }

        return Math.max(prevSell, prevCool);
    }

    public static void main(String[] args) {
        System.out.println("=== Best Time with Cooldown ===\n");

        System.out.println("Brute:   " + bruteForce(new int[]{1,2,3,0,2}));  // 3
        System.out.println("Optimal: " + optimal(new int[]{1,2,3,0,2}));      // 3
        System.out.println("Best:    " + best(new int[]{1,2,3,0,2}));         // 3

        System.out.println("\nSingle:     " + best(new int[]{1}));             // 0
        System.out.println("Decreasing: " + best(new int[]{5,4,3,2,1}));      // 0
        System.out.println("Two days:   " + best(new int[]{1,4}));            // 3
    }
}
