/**
 * Problem: Intersecting Chords in a Circle
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given N pairs of points on a circle (2N points total), count the number
 * of ways to connect them with N non-intersecting chords modulo 10^9+7.
 * The answer is the Nth Catalan number.
 *
 * Catalan recurrence: C(0)=1, C(n) = sum_{i=0}^{n-1} C(i)*C(n-1-i)
 *
 * @author DSA_Nova
 */
public class Solution {

    static final long MOD = 1_000_000_007L;

    // ============================================================
    // APPROACH 1: BRUTE FORCE (Recursive Catalan, no memoization)
    // Time: O(4^n)  |  Space: O(n) recursion stack
    // ============================================================
    // At each step, fix the first point and choose which other point
    // it pairs with. The chord created splits remaining points into
    // two independent groups, giving the Catalan recurrence.
    // No caching -> exponential re-computation.
    static class BruteForce {
        public static long countChords(int n) {
            return catalan(n);
        }

        private static long catalan(int k) {
            if (k <= 1) return 1;
            long result = 0;
            for (int i = 0; i < k; i++) {
                result = (result + catalan(i) % MOD * catalan(k - 1 - i)) % MOD;
            }
            return result;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (Top-Down DP with memoization)
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    // Same recursion but store computed Catalan values in an array
    // to achieve polynomial time.
    static class Optimal {
        private static long[] memo;

        public static long countChords(int n) {
            memo = new long[n + 1];
            java.util.Arrays.fill(memo, -1);
            return catalan(n);
        }

        private static long catalan(int k) {
            if (k <= 1) return 1;
            if (memo[k] != -1) return memo[k];
            long result = 0;
            for (int i = 0; i < k; i++) {
                result = (result + catalan(i) * catalan(k - 1 - i)) % MOD;
            }
            memo[k] = result;
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST (Bottom-Up DP)
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    // Iteratively compute C(0), C(1), ..., C(n).
    // For each k, sum over all ways to split the 2k points into two
    // independent groups by fixing the chord from point 1 to point 2i+2.
    // Group on one side has 2i points -> C(i) ways.
    // Group on other side has 2(k-1-i) points -> C(k-1-i) ways.
    static class Best {
        public static long countChords(int n) {
            if (n == 0) return 1;
            long[] dp = new long[n + 1];
            dp[0] = 1;
            dp[1] = 1;

            for (int k = 2; k <= n; k++) {
                for (int i = 0; i < k; i++) {
                    dp[k] = (dp[k] + dp[i] * dp[k - 1 - i]) % MOD;
                }
            }
            return dp[n];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Intersecting Chords in a Circle ===");

        // Catalan: C(0)=1, C(1)=1, C(2)=2, C(3)=5, C(4)=14, C(5)=42, C(10)=16796
        int[]  tests    = {0, 1, 2, 3, 4, 5, 10};
        long[] expected = {1, 1, 2, 5, 14, 42, 16796};

        for (int t = 0; t < tests.length; t++) {
            int  n  = tests[t];
            long bf = BruteForce.countChords(n);
            long op = Optimal.countChords(n);
            long be = Best.countChords(n);
            String status = (bf == op && op == be && be == expected[t]) ? "PASS" : "FAIL";
            System.out.printf("[%s] N=%-3d | Brute: %-8d | Optimal: %-8d | Best: %-8d | Expected: %d%n",
                              status, n, bf, op, be, expected[t]);
        }
    }
}
