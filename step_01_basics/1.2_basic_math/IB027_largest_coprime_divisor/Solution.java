/**
 * Problem: Largest Coprime Divisor
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given two positive integers A and B, find the largest divisor of A
 * that is coprime to B (i.e., gcd(divisor, B) == 1).
 *
 * Key insight: repeatedly divide A by gcd(A, B) until gcd becomes 1.
 * The resulting A is the answer.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(A)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * Enumerate all divisors of A from largest to smallest.
         * Return the first d with gcd(d, B) == 1.
         */
        public static int solve(int A, int B) {
            for (int d = A; d >= 1; d--) {
                if (A % d == 0 && gcd(d, B) == 1) return d;
            }
            return 1;
        }

        private static int gcd(int a, int b) {
            return b == 0 ? a : gcd(b, a % b);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Repeated GCD removal
    // Time: O(log^2(A))  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * While gcd(A, B) > 1, divide A by gcd(A, B).
         * This removes all prime factors shared between A and B.
         * When gcd(A, B) == 1, A is the largest such divisor.
         *
         * Correctness: each step reduces A by removing common primes,
         * so A remains a divisor of the original A and becomes coprime to B.
         */
        public static int solve(int A, int B) {
            int g = gcd(A, B);
            while (g != 1) {
                A /= g;
                g = gcd(A, B);
            }
            return A;
        }

        static int gcd(int a, int b) {
            return b == 0 ? a : gcd(b, a % b);
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Same algorithm, explicit factor removal
    // Time: O(log^2(A))  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Same repeated-GCD-removal. Expressed with an inner while loop
         * to fully remove each shared factor g before recomputing gcd.
         * Functionally equivalent; both are O(log^2 A).
         */
        public static int solve(int A, int B) {
            while (true) {
                int g = Optimal.gcd(A, B);
                if (g == 1) break;
                while (A % g == 0) A /= g;
            }
            return A;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Largest Coprime Divisor ===");
        int[][] tests = {{30, 12}, {15, 3}, {7, 2}, {100, 10}, {48, 36}};
        for (int[] t : tests) {
            System.out.printf("A=%d, B=%d: Brute=%d, Optimal=%d, Best=%d%n",
                t[0], t[1],
                BruteForce.solve(t[0], t[1]),
                Optimal.solve(t[0], t[1]),
                Best.solve(t[0], t[1]));
        }
    }
}
