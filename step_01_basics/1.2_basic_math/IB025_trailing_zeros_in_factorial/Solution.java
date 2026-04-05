/**
 * Problem: Trailing Zeros in Factorial
 * Difficulty: EASY | XP: 10
 * Source: InterviewBit
 *
 * Given an integer n, return the number of trailing zeros in n!.
 * Trailing zeros = pairs of (2 * 5). Since 2s always exceed 5s, count factors of 5.
 *
 * @author DSA_Nova
 */
import java.math.BigInteger;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n log n)  |  Space: O(n digits) — BigInteger needed for large n
    // Compute n! directly, then count trailing zeros
    // ============================================================
    static class BruteForce {
        /**
         * Compute the full factorial using BigInteger (handles arbitrary precision),
         * then count trailing zeros by repeatedly dividing by 10.
         * Impractical for large n (e.g., n=1000 produces a ~2568-digit number).
         */
        public static int trailingZeroes(int n) {
            BigInteger factorial = BigInteger.ONE;
            for (int i = 2; i <= n; i++) {
                factorial = factorial.multiply(BigInteger.valueOf(i));
            }
            int count = 0;
            BigInteger ten = BigInteger.TEN;
            while (factorial.mod(ten).equals(BigInteger.ZERO)) {
                count++;
                factorial = factorial.divide(ten);
            }
            return count;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Time: O(log5 n)  |  Space: O(1)
    // Legendre's formula: count factors of 5 at each power of 5
    // ============================================================
    static class Optimal {
        /**
         * Trailing zeros = min(count of 2s, count of 5s) in the prime factorization of n!.
         * 2s are always more plentiful, so count only 5s.
         * n/5 numbers are divisible by 5, n/25 by 25 (contributing an extra 5), etc.
         */
        public static int trailingZeroes(int n) {
            int count = 0;
            long powerOf5 = 5;
            while (powerOf5 <= n) {
                count += n / powerOf5;
                powerOf5 *= 5;
            }
            return count;
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(log5 n)  |  Space: O(1)
    // Compact iterative: repeatedly divide n by 5
    // ============================================================
    static class Best {
        /**
         * Instead of tracking the growing power of 5, divide n by 5 repeatedly.
         * This is equivalent to the Optimal approach mathematically:
         *   floor(n/5) + floor(n/25) + ... == floor(n/5) + floor(n/5 / 5) + ...
         */
        public static int trailingZeroes(int n) {
            int count = 0;
            while (n >= 5) {
                n /= 5;
                count += n;
            }
            return count;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Trailing Zeros in Factorial ===");
        int[] inputs   = {5, 10, 25, 100, 0, 1, 30};
        int[] expected = {1,  2,  6,  24, 0, 0,  7};

        for (int i = 0; i < inputs.length; i++) {
            int n  = inputs[i];
            int b  = BruteForce.trailingZeroes(n);
            int o  = Optimal.trailingZeroes(n);
            int be = Best.trailingZeroes(n);
            String status = (b == o && o == be && be == expected[i]) ? "OK" : "FAIL";
            System.out.printf("n=%-4d | Brute: %-3d | Optimal: %-3d | Best: %-3d | Expected: %-3d | %s%n",
                    n, b, o, be, expected[i], status);
        }
    }
}
