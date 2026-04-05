/**
 * Problem: Check Palindrome (LeetCode #9)
 * Difficulty: EASY | XP: 10
 *
 * Given an integer x, return true if x is a palindrome.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- String Conversion
    // Time: O(D)  |  Space: O(D) where D = number of digits
    // Convert to string, compare with its reverse.
    // ============================================================
    static class BruteForce {
        public static boolean isPalindrome(int x) {
            if (x < 0) return false;
            String s = String.valueOf(x);
            String rev = new StringBuilder(s).reverse().toString();
            return s.equals(rev);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Full Number Reversal (Math)
    // Time: O(D)  |  Space: O(1)
    // Reverse entire number using modulo, compare with original.
    // ============================================================
    static class Optimal {
        public static boolean isPalindrome(int x) {
            if (x < 0) return false;

            int original = x;
            long reversed = 0; // long to avoid overflow

            while (x > 0) {
                reversed = reversed * 10 + x % 10;
                x /= 10;
            }

            return reversed == original;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Half Reversal (Math, Overflow-Safe)
    // Time: O(D/2) = O(D)  |  Space: O(1)
    // Reverse only the second half. Compare halves.
    // No overflow possible since reversed half <= original half.
    // ============================================================
    static class Best {
        public static boolean isPalindrome(int x) {
            // Negative numbers and numbers ending in 0 (except 0 itself)
            if (x < 0 || (x % 10 == 0 && x != 0)) return false;

            int reversedHalf = 0;
            while (x > reversedHalf) {
                reversedHalf = reversedHalf * 10 + x % 10;
                x /= 10;
            }

            // Even length: x == reversedHalf
            // Odd length: x == reversedHalf / 10 (discard middle digit)
            return x == reversedHalf || x == reversedHalf / 10;
        }
    }

    public static void main(String[] args) {
        int[] tests = {121, -121, 10, 0, 1234321, 1221, 12345, 1, 11};

        System.out.println("=== Check Palindrome ===");
        for (int x : tests) {
            System.out.println("\nx = " + x);
            System.out.println("  Brute Force: " + BruteForce.isPalindrome(x));
            System.out.println("  Optimal:     " + Optimal.isPalindrome(x));
            System.out.println("  Best:        " + Best.isPalindrome(x));
        }
    }
}
