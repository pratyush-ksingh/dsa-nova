/**
 * Problem: Sum of Pairwise Hamming Distance
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given an integer array A of N elements, find the sum of bit differences
 * among all pairs (i, j) where i != j. Result modulo 10^9+7.
 *
 * Hamming distance = number of bit positions where two integers differ.
 *
 * @author DSA_Nova
 */
public class Solution {

    static final int MOD = 1_000_000_007;

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2 * 32)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * For every ordered pair (i, j) with i != j, XOR the two values,
         * count set bits (popcount), and accumulate.
         */
        public static long solve(int[] A) {
            int n = A.length;
            long total = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (i != j) {
                        total += Integer.bitCount(A[i] ^ A[j]);
                    }
                }
            }
            return total % MOD;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Bit-by-bit contribution
    // Time: O(32 * n)  |  Space: O(1)
    // ============================================================
    static class Optimal {
        /**
         * For each bit position b (0..31):
         *   - Count numbers with bit set: c
         *   - Numbers with bit clear: n - c
         *   - Ordered pairs that differ at bit b: 2 * c * (n - c)
         * Sum over all 32 bits.
         */
        public static long solve(int[] A) {
            int n = A.length;
            long total = 0;
            for (int b = 0; b < 32; b++) {
                long ones = 0;
                for (int x : A) {
                    if (((x >> b) & 1) == 1) ones++;
                }
                long zeros = n - ones;
                total = (total + 2 * ones * zeros) % MOD;
            }
            return total;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Same algorithm, slightly optimized loop
    // Time: O(32 * n)  |  Space: O(1)
    // ============================================================
    static class Best {
        /**
         * Identical to Optimal in complexity. The key insight: for bit b,
         * the contribution to the total hamming distance sum (over ordered
         * pairs) is 2 * c * (n - c) where c = count of 1s at bit b.
         * Accumulated modulo 10^9+7.
         */
        public static long solve(int[] A) {
            int n = A.length;
            long total = 0;
            for (int b = 0; b < 32; b++) {
                int ones = 0;
                for (int x : A) {
                    ones += (x >>> b) & 1;
                }
                total = (total + 2L * ones * (n - ones)) % MOD;
            }
            return total;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Sum of Pairwise Hamming Distance ===");
        int[][] tests = {{1, 3, 5}, {4, 14, 2}, {1}};
        for (int[] A : tests) {
            System.out.printf("Input: %s%n", java.util.Arrays.toString(A));
            System.out.printf("  Brute:   %d%n", BruteForce.solve(A));
            System.out.printf("  Optimal: %d%n", Optimal.solve(A));
            System.out.printf("  Best:    %d%n%n", Best.solve(A));
        }
    }
}
