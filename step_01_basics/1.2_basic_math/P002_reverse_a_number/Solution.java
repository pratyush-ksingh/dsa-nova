/**
 * Problem: Reverse a Number (LeetCode #7)
 * Difficulty: EASY | XP: 10
 *
 * Reverse digits of a 32-bit signed integer. Return 0 on overflow.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- String Reversal
    // Time: O(d)  |  Space: O(d)
    // Convert to string, reverse, convert back. Check overflow.
    // ============================================================
    static class BruteForce {
        public static int reverse(int x) {
            if (x == 0) return 0;

            boolean negative = x < 0;
            // Use long to safely handle Integer.MIN_VALUE
            String str = String.valueOf(Math.abs((long) x));
            String reversed = new StringBuilder(str).reverse().toString();

            try {
                int result = Integer.parseInt(reversed);
                return negative ? -result : result;
            } catch (NumberFormatException e) {
                // Overflow
                return 0;
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Mathematical Digit-by-Digit
    // Time: O(d)  |  Space: O(1)
    // Pop last digit with %10, push onto rev with *10.
    // Check overflow BEFORE the multiply.
    // ============================================================
    static class Optimal {
        public static int reverse(int x) {
            int rev = 0;
            while (x != 0) {
                int digit = x % 10;
                x /= 10;

                // Overflow check before rev = rev * 10 + digit
                if (rev > Integer.MAX_VALUE / 10 || (rev == Integer.MAX_VALUE / 10 && digit > 7)) {
                    return 0;
                }
                if (rev < Integer.MIN_VALUE / 10 || (rev == Integer.MIN_VALUE / 10 && digit < -8)) {
                    return 0;
                }

                rev = rev * 10 + digit;
            }
            return rev;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Same as Optimal (mathematical)
    // Time: O(d)  |  Space: O(1)
    // This IS the best approach. Shown using long for simplicity.
    // ============================================================
    static class Best {
        public static int reverse(int x) {
            long rev = 0;
            while (x != 0) {
                rev = rev * 10 + x % 10;
                x /= 10;
            }
            // Check if result fits in 32-bit range
            if (rev > Integer.MAX_VALUE || rev < Integer.MIN_VALUE) {
                return 0;
            }
            return (int) rev;
        }
    }

    public static void main(String[] args) {
        int[] testCases = {123, -456, 120, 0, 1534236469, -2147483648};
        System.out.println("=== Reverse a Number ===");

        for (int x : testCases) {
            System.out.printf("x = %d%n", x);
            System.out.printf("  Brute Force: %d%n", BruteForce.reverse(x));
            System.out.printf("  Optimal:     %d%n", Optimal.reverse(x));
            System.out.printf("  Best:        %d%n", Best.reverse(x));
        }
    }
}
