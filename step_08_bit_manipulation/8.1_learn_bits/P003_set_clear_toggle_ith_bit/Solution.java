/**
 * Problem: Set Clear Toggle ith Bit
 * Difficulty: EASY | XP: 10
 *
 * Given an integer n and bit position i, implement set, clear, and toggle.
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (String Conversion)
// Time: O(1) | Space: O(1) -- 32-bit numbers
// ============================================================
class BruteForce {
    public static int setBit(int n, int i) {
        StringBuilder sb = new StringBuilder(Integer.toBinaryString(n));
        while (sb.length() <= i) sb.insert(0, '0');
        sb.setCharAt(sb.length() - 1 - i, '1');
        return Integer.parseInt(sb.toString(), 2);
    }

    public static int clearBit(int n, int i) {
        StringBuilder sb = new StringBuilder(Integer.toBinaryString(n));
        while (sb.length() <= i) sb.insert(0, '0');
        sb.setCharAt(sb.length() - 1 - i, '0');
        return Integer.parseInt(sb.toString(), 2);
    }

    public static int toggleBit(int n, int i) {
        StringBuilder sb = new StringBuilder(Integer.toBinaryString(n));
        while (sb.length() <= i) sb.insert(0, '0');
        int idx = sb.length() - 1 - i;
        sb.setCharAt(idx, sb.charAt(idx) == '0' ? '1' : '0');
        return Integer.parseInt(sb.toString(), 2);
    }
}

// ============================================================
// Approach 2: Optimal (Bitwise Operations)
// Time: O(1) | Space: O(1)
// ============================================================
class Optimal {
    public static int setBit(int n, int i) {
        return n | (1 << i);
    }

    public static int clearBit(int n, int i) {
        return n & ~(1 << i);
    }

    public static int toggleBit(int n, int i) {
        return n ^ (1 << i);
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Set Clear Toggle ith Bit ===\n");

        // Test cases: {n, i, expectedSet, expectedClear, expectedToggle}
        int[][] tests = {
            {9,  2, 13, 9,  13},  // 1001 -> set bit 2 -> 1101=13; clear bit 2 of 1001 -> 1001=9; toggle -> 1101=13
            {13, 2, 13, 9,  9},   // 1101 -> set bit 2 -> 1101=13; clear -> 1001=9; toggle -> 1001=9
            {0,  0, 1,  0,  1},   // 0000 -> set bit 0 -> 0001=1
            {7,  0, 7,  6,  6},   // 0111 -> set bit 0 -> 0111=7; clear -> 0110=6; toggle -> 0110=6
            {0,  3, 8,  0,  8},   // 0000 -> set bit 3 -> 1000=8
        };

        boolean allPass = true;
        for (int[] t : tests) {
            int n = t[0], i = t[1];
            int expSet = t[2], expClear = t[3], expToggle = t[4];

            int bSet = BruteForce.setBit(n, i);
            int bClear = BruteForce.clearBit(n, i);
            int bToggle = BruteForce.toggleBit(n, i);

            int oSet = Optimal.setBit(n, i);
            int oClear = Optimal.clearBit(n, i);
            int oToggle = Optimal.toggleBit(n, i);

            boolean pass = bSet == expSet && bClear == expClear && bToggle == expToggle
                        && oSet == expSet && oClear == expClear && oToggle == expToggle;
            allPass &= pass;

            System.out.printf("n=%d, i=%d | Set: B=%d O=%d (exp %d) | Clear: B=%d O=%d (exp %d) | Toggle: B=%d O=%d (exp %d) [%s]%n",
                n, i, bSet, oSet, expSet, bClear, oClear, expClear, bToggle, oToggle, expToggle, pass ? "PASS" : "FAIL");
        }
        System.out.println("\nAll pass: " + allPass);
    }
}
