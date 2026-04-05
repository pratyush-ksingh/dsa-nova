/**
 * Problem: Count Digits
 * Difficulty: EASY | XP: 10
 *
 * Count the number of digits in a given integer N.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Iterative Division
    // Time: O(d) where d = number of digits  |  Space: O(1)
    // Repeatedly divide by 10 until 0, counting iterations.
    // ============================================================
    static class BruteForce {
        public static int countDigits(int n) {
            if (n == 0) return 1;

            // Handle negative: use long to avoid overflow on Integer.MIN_VALUE
            long num = Math.abs((long) n);
            int count = 0;
            while (num > 0) {
                count++;
                num /= 10;
            }
            return count;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Logarithm Formula
    // Time: O(1)  |  Space: O(1)
    // digits = floor(log10(|N|)) + 1
    // ============================================================
    static class Optimal {
        public static int countDigits(int n) {
            if (n == 0) return 1;

            long num = Math.abs((long) n);
            return (int) (Math.log10(num)) + 1;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- String Conversion
    // Time: O(d)  |  Space: O(d)
    // Convert to string, return length. Simple and readable.
    // ============================================================
    static class Best {
        public static int countDigits(int n) {
            // Math.abs with long to handle Integer.MIN_VALUE
            return String.valueOf(Math.abs((long) n)).length();
        }
    }

    public static void main(String[] args) {
        int[] testCases = {12345, 0, -987, 1000000, Integer.MAX_VALUE, Integer.MIN_VALUE};
        System.out.println("=== Count Digits ===");

        for (int n : testCases) {
            System.out.printf("N = %d%n", n);
            System.out.printf("  Brute Force: %d%n", BruteForce.countDigits(n));
            System.out.printf("  Optimal:     %d%n", Optimal.countDigits(n));
            System.out.printf("  Best:        %d%n", Best.countDigits(n));
        }
    }
}
