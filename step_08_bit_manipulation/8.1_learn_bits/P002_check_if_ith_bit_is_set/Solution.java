/**
 * Problem: Check if ith Bit is Set
 * Difficulty: EASY | XP: 10
 *
 * Given N and position i, check if the ith bit is set (1) or not (0).
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Repeated Division)
// Time: O(i) | Space: O(1)
// ============================================================
class BruteForce {
    public static boolean isSet(int n, int i) {
        // Divide by 2, i times, to bring bit i to position 0
        for (int k = 0; k < i; k++) {
            n = n / 2;
        }
        return (n % 2) == 1;
    }
}

// ============================================================
// Approach 2: Optimal (Left-Shift Mask)
// Time: O(1) | Space: O(1)
// ============================================================
class Optimal {
    public static boolean isSet(int n, int i) {
        // Create mask with only bit i set, AND with n
        return (n & (1 << i)) != 0;
    }
}

// ============================================================
// Approach 3: Best (Right-Shift Number)
// Time: O(1) | Space: O(1)
// ============================================================
class Best {
    public static boolean isSet(int n, int i) {
        // Shift n right by i, check if LSB is 1
        return ((n >> i) & 1) == 1;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Check if ith Bit is Set ===\n");

        int[][] tests = {
            // {N, i, expected (1=true, 0=false)}
            {5, 0, 1},   // 101, bit 0 = 1
            {5, 1, 0},   // 101, bit 1 = 0
            {5, 2, 1},   // 101, bit 2 = 1
            {8, 3, 1},   // 1000, bit 3 = 1
            {8, 0, 0},   // 1000, bit 0 = 0
            {1, 0, 1},   // 1, bit 0 = 1
            {1, 1, 0},   // 1, bit 1 = 0
            {0, 0, 0},   // edge: n=0, no bits set (note: constraint says N>=1, but safe)
            {1023, 9, 1}, // 1111111111, bit 9 = 1
            {1024, 10, 1} // 10000000000, bit 10 = 1
        };

        for (int[] t : tests) {
            int n = t[0], i = t[1];
            boolean expected = t[2] == 1;
            boolean b = BruteForce.isSet(n, i);
            boolean o = Optimal.isSet(n, i);
            boolean be = Best.isSet(n, i);
            boolean pass = b == expected && o == expected && be == expected;

            System.out.printf("N=%d (%-12s), i=%d -> Brute:%-5b Optimal:%-5b Best:%-5b Expected:%-5b [%s]%n",
                    n, Integer.toBinaryString(n), i, b, o, be, expected, pass ? "PASS" : "FAIL");
        }
    }
}
