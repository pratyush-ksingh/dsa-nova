/**
 * Problem: Check if Number is Power of 2 (LeetCode #231)
 * Difficulty: EASY | XP: 10
 *
 * Return true if n is a power of two.
 * Key insight: A power of 2 has exactly one set bit, so n & (n-1) == 0.
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Repeated Division)
// Time: O(log n) | Space: O(1)
// ============================================================
class BruteForce {
    public static boolean isPowerOfTwo(int n) {
        if (n <= 0) return false;
        while (n % 2 == 0) {
            n /= 2;
        }
        return n == 1;
    }
}

// ============================================================
// Approach 2: Optimal (Bit Trick)
// Time: O(1) | Space: O(1)
// ============================================================
class Optimal {
    public static boolean isPowerOfTwo(int n) {
        // Power of 2 has exactly one set bit.
        // n & (n-1) clears the rightmost set bit.
        // If result is 0, there was only one set bit.
        return n > 0 && (n & (n - 1)) == 0;
    }
}

// ============================================================
// Approach 3: Best (bitCount -- clarity variant)
// Time: O(1) | Space: O(1)
// ============================================================
class Best {
    public static boolean isPowerOfTwo(int n) {
        // Exactly one set bit means power of 2
        return n > 0 && Integer.bitCount(n) == 1;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Check if Number is Power of 2 ===\n");

        int[] inputs =    {1, 2, 4, 16, 1024, 3, 0, -1, -4, 6, Integer.MIN_VALUE, 1073741824};
        boolean[] expected = {true, true, true, true, true, false, false, false, false, false, false, true};

        for (int t = 0; t < inputs.length; t++) {
            int n = inputs[t];
            boolean b = BruteForce.isPowerOfTwo(n);
            boolean o = Optimal.isPowerOfTwo(n);
            boolean r = Best.isPowerOfTwo(n);
            boolean pass = (b == expected[t] && o == expected[t] && r == expected[t]);

            System.out.println("Input:    " + n);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + r);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
