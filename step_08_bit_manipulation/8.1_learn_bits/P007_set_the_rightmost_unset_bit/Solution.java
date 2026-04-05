/**
 * Problem: Set the Rightmost Unset Bit
 * Difficulty: EASY | XP: 10
 *
 * Key Insight: n | (n+1) sets the rightmost 0-bit to 1.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Bit-by-Bit Scan
    // Time: O(32) = O(1)  |  Space: O(1)
    //
    // Scan from LSB, find first 0-bit, set it.
    // ============================================================
    public static int bruteForce(int n) {
        for (int pos = 0; pos < 32; pos++) {
            if (((n >> pos) & 1) == 0) {
                return n | (1 << pos);
            }
        }
        return n;  // all bits are 1
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- n | (n + 1)
    // Time: O(1)  |  Space: O(1)
    //
    // n+1 flips rightmost 0 (and trailing 1s).
    // OR with n restores trailing 1s and keeps the new bit.
    // ============================================================
    public static int optimal(int n) {
        // If all bits are set, return n unchanged
        // (n & (n+1)) == 0 means n is of the form 2^k - 1
        if (n > 0 && (n & (n + 1)) == 0) {
            return n;
        }
        return n | (n + 1);
    }

    // ============================================================
    // APPROACH 3: BEST -- Isolate with ~n & (n + 1)
    // Time: O(1)  |  Space: O(1)
    //
    // ~n & (n+1) isolates the rightmost 0-bit as a mask.
    // OR the mask into n.
    // ============================================================
    public static int best(int n) {
        // If all bits set, return unchanged
        if (n > 0 && (n & (n + 1)) == 0) {
            return n;
        }
        int mask = ~n & (n + 1);  // isolate rightmost 0
        return n | mask;
    }

    public static void main(String[] args) {
        System.out.println("=== Set the Rightmost Unset Bit ===");

        int[] tests = {6, 9, 15, 0, 1, 10};
        for (int n : tests) {
            System.out.printf("n=%d (binary: %s)%n", n, Integer.toBinaryString(n));
            System.out.printf("  Brute:   %d (%s)%n", bruteForce(n), Integer.toBinaryString(bruteForce(n)));
            System.out.printf("  Optimal: %d (%s)%n", optimal(n), Integer.toBinaryString(optimal(n)));
            System.out.printf("  Best:    %d (%s)%n", best(n), Integer.toBinaryString(best(n)));
            System.out.println();
        }
    }
}
