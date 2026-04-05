/**
 * Problem: Count Set Bits (Hamming Weight)
 * Difficulty: EASY | XP: 10
 * LeetCode #191
 *
 * Key Insight: n & (n-1) clears the rightmost set bit (Brian Kernighan's trick).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Check Each Bit
    // Time: O(32) = O(1)  |  Space: O(1)
    //
    // Extract last bit with n&1, shift right, repeat.
    // ============================================================
    public static int bruteForce(int n) {
        int count = 0;
        while (n != 0) {
            count += (n & 1);
            n >>>= 1;  // unsigned right shift (important for negative numbers)
        }
        return count;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Brian Kernighan's Algorithm
    // Time: O(k) where k = set bits  |  Space: O(1)
    //
    // n & (n-1) clears rightmost set bit. Loop k times.
    // ============================================================
    public static int optimal(int n) {
        int count = 0;
        while (n != 0) {
            n = n & (n - 1);  // clear rightmost set bit
            count++;
        }
        return count;
    }

    // ============================================================
    // APPROACH 3: BEST -- Lookup Table (byte-level)
    // Time: O(1) -- 4 lookups  |  Space: O(256) = O(1)
    //
    // Precompute popcount for all byte values, split int into 4 bytes.
    // ============================================================
    private static final int[] BYTE_TABLE = new int[256];
    static {
        for (int i = 1; i < 256; i++) {
            BYTE_TABLE[i] = BYTE_TABLE[i >> 1] + (i & 1);
        }
    }

    public static int best(int n) {
        return BYTE_TABLE[n & 0xFF]
             + BYTE_TABLE[(n >>> 8) & 0xFF]
             + BYTE_TABLE[(n >>> 16) & 0xFF]
             + BYTE_TABLE[(n >>> 24) & 0xFF];
    }

    public static void main(String[] args) {
        System.out.println("=== Count Set Bits ===");

        int[] tests = {11, 128, 255, 0, 1, Integer.MAX_VALUE};
        for (int n : tests) {
            System.out.printf("n=%d (binary: %s)%n", n, Integer.toBinaryString(n));
            System.out.printf("  Brute:   %d%n", bruteForce(n));
            System.out.printf("  Optimal: %d%n", optimal(n));
            System.out.printf("  Best:    %d%n", best(n));
            System.out.println();
        }
    }
}
