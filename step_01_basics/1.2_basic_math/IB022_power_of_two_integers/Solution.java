/**
 * Problem: Power of Two Integers
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a positive integer A, return 1 if A can be expressed as B^P
 * where B >= 1 and P >= 2, otherwise return 0.
 *
 * Special case: A=1 => 1 = 1^P for any P >= 2, return 1.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(sqrt(A) * log(A))  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * Try every base b from 2 to sqrt(A).
         * For each base, repeatedly multiply (b^2, b^3, ...) checking if == A.
         */
        public static int solve(int A) {
            if (A == 1) return 1;
            int limit = (int) Math.sqrt(A);
            for (int b = 2; b <= limit; b++) {
                long power = (long) b * b;
                while (power <= A) {
                    if (power == A) return 1;
                    power *= b;
                }
            }
            return 0;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Enumerate exponents, float-based root
    // Time: O(32 * log(A))  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * For each exponent p from 2 to 32, compute candidate base using
         * Math.pow and check floor/ceil/round with exact long arithmetic
         * to avoid floating point imprecision.
         */
        public static int solve(int A) {
            if (A == 1) return 1;
            for (int p = 2; p <= 32; p++) {
                int b = (int) Math.round(Math.pow(A, 1.0 / p));
                for (int candidate = Math.max(2, b - 1); candidate <= b + 1; candidate++) {
                    if (exactPow(candidate, p) == A) return 1;
                }
            }
            return 0;
        }

        private static long exactPow(long base, int exp) {
            long result = 1;
            for (int i = 0; i < exp; i++) {
                result *= base;
                if (result > Integer.MAX_VALUE) return result;
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Enumerate exponents, binary search for base
    // Time: O(32 * log(A))  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * For each exponent p (2..32), binary search for a base b in [2, A]
         * such that b^p == A. Uses exact long arithmetic to avoid overflow.
         */
        public static int solve(int A) {
            if (A == 1) return 1;
            for (int p = 2; p <= 32; p++) {
                if (bsearchBase((long) A, p)) return 1;
            }
            return 0;
        }

        private static boolean bsearchBase(long A, int p) {
            long lo = 2, hi = (long) Math.pow(A, 1.0 / p) + 2;
            while (lo <= hi) {
                long mid = lo + (hi - lo) / 2;
                long val = safePow(mid, p, A);
                if (val == A) return true;
                else if (val < A) lo = mid + 1;
                else hi = mid - 1;
            }
            return false;
        }

        private static long safePow(long base, int exp, long limit) {
            long result = 1;
            for (int i = 0; i < exp; i++) {
                result *= base;
                if (result > limit) return result;
            }
            return result;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Power of Two Integers ===");
        int[] tests = {1, 4, 8, 9, 16, 27, 6, 7, 64, 1000000000};
        for (int A : tests) {
            System.out.printf("A=%d: Brute=%d, Optimal=%d, Best=%d%n",
                A, BruteForce.solve(A), Optimal.solve(A), Best.solve(A));
        }
    }
}
