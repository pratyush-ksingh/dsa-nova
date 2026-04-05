/**
 * Problem: Swap Two Numbers (Without Temp Variable)
 * Difficulty: EASY | XP: 10
 *
 * Swap two numbers without using a temporary variable.
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Using Temp Variable)
// Time: O(1) | Space: O(1)
// ============================================================
class BruteForce {
    public static int[] swap(int a, int b) {
        int temp = a;
        a = b;
        b = temp;
        return new int[]{a, b};
    }
}

// ============================================================
// Approach 2: Optimal (XOR Swap)
// Time: O(1) | Space: O(1) -- no extra variable
// ============================================================
class Optimal {
    public static int[] swap(int a, int b) {
        a = a ^ b;  // a now holds a XOR b
        b = a ^ b;  // b = (a XOR b) XOR b = original a
        a = a ^ b;  // a = (a XOR b) XOR a = original b
        return new int[]{a, b};
    }
}

// ============================================================
// Approach 3: Best (Arithmetic Swap -- No XOR)
// Time: O(1) | Space: O(1)
// NOTE: Works for ints; beware of overflow with very large values.
// ============================================================
class Best {
    public static int[] swap(int a, int b) {
        a = a + b;
        b = a - b;  // b = (a + b) - b = original a
        a = a - b;  // a = (a + b) - a = original b
        return new int[]{a, b};
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Swap Two Numbers ===\n");

        int[][] tests = {
            {5, 10}, {0, 7}, {-3, 4}, {100, 100}, {0, 0}, {-5, -8}
        };

        for (int[] t : tests) {
            int a = t[0], b = t[1];
            int[] bf = BruteForce.swap(a, b);
            int[] op = Optimal.swap(a, b);
            int[] bs = Best.swap(a, b);

            boolean pass = bf[0] == b && bf[1] == a
                        && op[0] == b && op[1] == a
                        && bs[0] == b && bs[1] == a;

            System.out.println("Input: a=" + a + ", b=" + b);
            System.out.println("  Brute:   [" + bf[0] + ", " + bf[1] + "]");
            System.out.println("  Optimal: [" + op[0] + ", " + op[1] + "]");
            System.out.println("  Best:    [" + bs[0] + ", " + bs[1] + "]");
            System.out.println("  " + (pass ? "PASS" : "FAIL"));
            System.out.println();
        }
    }
}
