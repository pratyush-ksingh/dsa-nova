/**
 * Problem: Lemonade Change (LeetCode #860)
 * Difficulty: EASY | XP: 10
 *
 * Each lemonade costs $5. Customers pay $5, $10, or $20.
 * Return true if you can give correct change to every customer.
 * Greedy: prefer $10+$5 over $5+$5+$5 for $20 change.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // GREEDY SIMULATION
    // Time: O(n) | Space: O(1)
    // ============================================================
    public static boolean lemonadeChange(int[] bills) {
        int fives = 0, tens = 0;

        for (int bill : bills) {
            if (bill == 5) {
                fives++;
            } else if (bill == 10) {
                if (fives == 0) return false;
                fives--;
                tens++;
            } else { // bill == 20
                // Prefer giving $10 + $5 (preserves $5 bills)
                if (tens > 0 && fives > 0) {
                    tens--;
                    fives--;
                } else if (fives >= 3) {
                    fives -= 3;
                } else {
                    return false;
                }
                // No need to track $20 bills -- never used as change
            }
        }

        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== Lemonade Change ===\n");

        int[][] testCases = {
            {5, 5, 5, 10, 20},
            {5, 5, 10, 10, 20},
            {5, 5, 10},
            {10},
            {5},
            {5, 5, 5, 5, 20, 20},
            {5, 5, 5, 10, 5, 5, 10, 20, 20, 20},
        };
        boolean[] expected = {true, false, true, false, true, true, false};

        for (int t = 0; t < testCases.length; t++) {
            boolean result = lemonadeChange(testCases[t]);
            boolean pass = result == expected[t];
            System.out.println("bills = " + Arrays.toString(testCases[t]));
            System.out.println("  Result: " + result + " | Expected: " + expected[t] + " | Pass: " + pass + "\n");
        }
    }
}
