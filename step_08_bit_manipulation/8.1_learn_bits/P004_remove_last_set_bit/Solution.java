/**
 * Problem: Remove Last Set Bit
 * Difficulty: EASY | XP: 10
 *
 * Turn off the rightmost set bit of a non-negative integer n.
 * Key insight: n & (n-1) clears the lowest set bit (Brian Kernighan's trick).
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Find Position Then Clear)
// Time: O(log n) | Space: O(1)
// ============================================================
class BruteForce {
    public static int removeLastSetBit(int n) {
        if (n == 0) return 0;
        // Find position of rightmost set bit by checking each bit
        for (int pos = 0; pos < 32; pos++) {
            if ((n & (1 << pos)) != 0) {
                // Clear this bit using XOR
                return n ^ (1 << pos);
            }
        }
        return 0;
    }
}

// ============================================================
// Approach 2: Optimal (Brian Kernighan's Trick)
// Time: O(1) | Space: O(1)
// ============================================================
class Optimal {
    public static int removeLastSetBit(int n) {
        // n-1 flips the rightmost 1 and all trailing 0s
        // AND-ing with n clears exactly the rightmost set bit
        return n & (n - 1);
    }
}

// ============================================================
// Approach 3: Best (Isolate and Subtract)
// Time: O(1) | Space: O(1)
// ============================================================
class Best {
    public static int removeLastSetBit(int n) {
        // n & -n isolates the rightmost set bit
        // Subtracting it clears that bit
        return n - (n & -n);
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Remove Last Set Bit ===\n");

        int[] inputs =   {12, 7, 16, 0, 1, 6, Integer.MAX_VALUE};
        int[] expected = { 8, 6,  0, 0, 0, 4, Integer.MAX_VALUE - 1};

        for (int t = 0; t < inputs.length; t++) {
            int n = inputs[t];
            int b = BruteForce.removeLastSetBit(n);
            int o = Optimal.removeLastSetBit(n);
            int r = Best.removeLastSetBit(n);
            boolean pass = (b == expected[t] && o == expected[t] && r == expected[t]);

            System.out.println("Input:    " + n + " (binary: " + Integer.toBinaryString(n) + ")");
            System.out.println("Brute:    " + b + " (binary: " + Integer.toBinaryString(b) + ")");
            System.out.println("Optimal:  " + o + " (binary: " + Integer.toBinaryString(o) + ")");
            System.out.println("Best:     " + r + " (binary: " + Integer.toBinaryString(r) + ")");
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
