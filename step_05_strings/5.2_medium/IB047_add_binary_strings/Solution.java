/**
 * Problem: Add Binary Strings
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit | LeetCode #67
 *
 * Key Insight: Right-to-left addition with carry, just like paper math.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Integer Conversion
    // Time: O(n)  |  Space: O(n)
    //
    // Limitation: Overflows for very long binary strings.
    // Works for strings up to ~62 digits (long range).
    // ============================================================
    public static String bruteForce(String a, String b) {
        if (a.isEmpty()) return b.isEmpty() ? "0" : b;
        if (b.isEmpty()) return a;

        long numA = Long.parseLong(a, 2);
        long numB = Long.parseLong(b, 2);
        long sum = numA + numB;
        return Long.toBinaryString(sum);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Right-to-Left with Carry
    // Time: O(max(m, n))  |  Space: O(max(m, n))
    //
    // Process from right, add digits + carry, build result reversed.
    // ============================================================
    public static String optimal(String a, String b) {
        StringBuilder result = new StringBuilder();
        int i = a.length() - 1;
        int j = b.length() - 1;
        int carry = 0;

        while (i >= 0 || j >= 0 || carry > 0) {
            int digitA = (i >= 0) ? a.charAt(i) - '0' : 0;
            int digitB = (j >= 0) ? b.charAt(j) - '0' : 0;

            int total = digitA + digitB + carry;
            result.append(total % 2);
            carry = total / 2;

            i--;
            j--;
        }

        return result.reverse().toString();
    }

    // ============================================================
    // APPROACH 3: BEST -- Bitwise Carry Computation
    // Time: O(max(m, n))  |  Space: O(max(m, n))
    //
    // Uses XOR for sum bit and AND/OR for carry -- mirrors hardware.
    // ============================================================
    public static String best(String a, String b) {
        StringBuilder result = new StringBuilder();
        int i = a.length() - 1;
        int j = b.length() - 1;
        int carry = 0;

        while (i >= 0 || j >= 0 || carry > 0) {
            int x = (i >= 0) ? a.charAt(i) - '0' : 0;
            int y = (j >= 0) ? b.charAt(j) - '0' : 0;

            int sumBit = x ^ y ^ carry;
            carry = (x & y) | (x & carry) | (y & carry);
            result.append(sumBit);

            i--;
            j--;
        }

        return result.reverse().toString();
    }

    public static void main(String[] args) {
        System.out.println("=== Add Binary Strings ===");

        String[][] tests = {
            {"11", "1"},
            {"1010", "1011"},
            {"0", "0"},
            {"1", "1"}
        };

        for (String[] t : tests) {
            System.out.printf("a=\"%s\", b=\"%s\"%n", t[0], t[1]);
            System.out.printf("  Brute:   %s%n", bruteForce(t[0], t[1]));
            System.out.printf("  Optimal: %s%n", optimal(t[0], t[1]));
            System.out.printf("  Best:    %s%n", best(t[0], t[1]));
            System.out.println();
        }
    }
}
