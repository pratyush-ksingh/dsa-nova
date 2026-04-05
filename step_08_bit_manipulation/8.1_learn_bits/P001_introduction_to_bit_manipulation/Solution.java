/**
 * Problem: Introduction to Bit Manipulation
 * Difficulty: EASY | XP: 10
 *
 * Implement basic bit operations: get, set, clear, toggle, power-of-2 check.
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (String Conversion)
// Time: O(1) -- 32 bits max | Space: O(1)
// ============================================================
class BruteForce {
    public static int getBit(int n, int i) {
        String binary = Integer.toBinaryString(n);
        // Pad to 32 chars
        while (binary.length() < 32) binary = "0" + binary;
        // Position i from right = index (31 - i) from left
        return binary.charAt(31 - i) - '0';
    }

    public static int setBit(int n, int i) {
        char[] bits = padBinary(n);
        bits[31 - i] = '1';
        return Integer.parseUnsignedInt(new String(bits), 2);
    }

    public static int clearBit(int n, int i) {
        char[] bits = padBinary(n);
        bits[31 - i] = '0';
        return Integer.parseUnsignedInt(new String(bits), 2);
    }

    public static int toggleBit(int n, int i) {
        char[] bits = padBinary(n);
        bits[31 - i] = (bits[31 - i] == '0') ? '1' : '0';
        return Integer.parseUnsignedInt(new String(bits), 2);
    }

    public static boolean isPowerOfTwo(int n) {
        if (n <= 0) return false;
        int count = 0;
        String binary = Integer.toBinaryString(n);
        for (char c : binary.toCharArray()) {
            if (c == '1') count++;
        }
        return count == 1;
    }

    private static char[] padBinary(int n) {
        String binary = Integer.toBinaryString(n);
        while (binary.length() < 32) binary = "0" + binary;
        return binary.toCharArray();
    }
}

// ============================================================
// Approach 2: Optimal (Bitwise Mask Operations)
// Time: O(1) | Space: O(1)
// ============================================================
class Optimal {
    public static int getBit(int n, int i) {
        return (n >> i) & 1;
    }

    public static int setBit(int n, int i) {
        return n | (1 << i);
    }

    public static int clearBit(int n, int i) {
        return n & ~(1 << i);
    }

    public static int toggleBit(int n, int i) {
        return n ^ (1 << i);
    }

    public static boolean isPowerOfTwo(int n) {
        return n > 0 && (n & (n - 1)) == 0;
    }
}

// ============================================================
// Approach 3: Best (Alternative Expressions -- same O(1))
// Time: O(1) | Space: O(1)
// ============================================================
class Best {
    // Alternative get: use AND with mask directly, return 0 or non-zero
    public static int getBit(int n, int i) {
        return (n & (1 << i)) != 0 ? 1 : 0;
    }

    public static int setBit(int n, int i) {
        return n | (1 << i);
    }

    public static int clearBit(int n, int i) {
        return n & ~(1 << i);
    }

    public static int toggleBit(int n, int i) {
        return n ^ (1 << i);
    }

    // Alternative power-of-2: use Integer.bitCount
    public static boolean isPowerOfTwo(int n) {
        return n > 0 && Integer.bitCount(n) == 1;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Introduction to Bit Manipulation ===\n");

        // --- Get Bit ---
        System.out.println("--- Get Bit ---");
        int[][] getBitTests = {{13, 2, 1}, {13, 1, 0}, {13, 0, 1}, {13, 3, 1}, {0, 0, 0}};
        for (int[] t : getBitTests) {
            int b = BruteForce.getBit(t[0], t[1]);
            int o = Optimal.getBit(t[0], t[1]);
            int be = Best.getBit(t[0], t[1]);
            boolean pass = b == t[2] && o == t[2] && be == t[2];
            System.out.printf("n=%d, i=%d -> Brute:%d Optimal:%d Best:%d Expected:%d [%s]%n",
                    t[0], t[1], b, o, be, t[2], pass ? "PASS" : "FAIL");
        }

        // --- Set Bit ---
        System.out.println("\n--- Set Bit ---");
        int[][] setBitTests = {{9, 2, 13}, {13, 2, 13}, {0, 0, 1}};
        for (int[] t : setBitTests) {
            int b = BruteForce.setBit(t[0], t[1]);
            int o = Optimal.setBit(t[0], t[1]);
            int be = Best.setBit(t[0], t[1]);
            boolean pass = b == t[2] && o == t[2] && be == t[2];
            System.out.printf("n=%d, i=%d -> Brute:%d Optimal:%d Best:%d Expected:%d [%s]%n",
                    t[0], t[1], b, o, be, t[2], pass ? "PASS" : "FAIL");
        }

        // --- Clear Bit ---
        System.out.println("\n--- Clear Bit ---");
        int[][] clearBitTests = {{13, 2, 9}, {9, 2, 9}, {1, 0, 0}};
        for (int[] t : clearBitTests) {
            int b = BruteForce.clearBit(t[0], t[1]);
            int o = Optimal.clearBit(t[0], t[1]);
            int be = Best.clearBit(t[0], t[1]);
            boolean pass = b == t[2] && o == t[2] && be == t[2];
            System.out.printf("n=%d, i=%d -> Brute:%d Optimal:%d Best:%d Expected:%d [%s]%n",
                    t[0], t[1], b, o, be, t[2], pass ? "PASS" : "FAIL");
        }

        // --- Toggle Bit ---
        System.out.println("\n--- Toggle Bit ---");
        int[][] toggleBitTests = {{13, 1, 15}, {15, 1, 13}, {0, 3, 8}};
        for (int[] t : toggleBitTests) {
            int b = BruteForce.toggleBit(t[0], t[1]);
            int o = Optimal.toggleBit(t[0], t[1]);
            int be = Best.toggleBit(t[0], t[1]);
            boolean pass = b == t[2] && o == t[2] && be == t[2];
            System.out.printf("n=%d, i=%d -> Brute:%d Optimal:%d Best:%d Expected:%d [%s]%n",
                    t[0], t[1], b, o, be, t[2], pass ? "PASS" : "FAIL");
        }

        // --- Power of Two ---
        System.out.println("\n--- Power of Two ---");
        int[][] powTests = {{16, 1}, {18, 0}, {1, 1}, {0, 0}, {1024, 1}};
        for (int[] t : powTests) {
            boolean b = BruteForce.isPowerOfTwo(t[0]);
            boolean o = Optimal.isPowerOfTwo(t[0]);
            boolean be = Best.isPowerOfTwo(t[0]);
            boolean expected = t[1] == 1;
            boolean pass = b == expected && o == expected && be == expected;
            System.out.printf("n=%d -> Brute:%b Optimal:%b Best:%b Expected:%b [%s]%n",
                    t[0], b, o, be, expected, pass ? "PASS" : "FAIL");
        }
    }
}
