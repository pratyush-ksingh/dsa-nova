/**
 * Problem: N Digit Numbers with Digit Sum S
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Count N-digit positive integers whose digits sum to S.
 * The first digit is 1-9 (no leading zeros), subsequent digits are 0-9.
 * Return answer modulo 10^9+7.
 *
 * DP state: dp[pos][remaining_sum]
 *   pos = current digit position (1-indexed)
 *   remaining = remaining sum needed
 *   At pos=1: digits 1..9 allowed
 *   At pos>1: digits 0..9 allowed
 *
 * @author DSA_Nova
 */
public class Solution {

    static final int MOD = 1_000_000_007;

    // ============================================================
    // APPROACH 1: BRUTE FORCE (Recursion without memoization)
    // Time: O(10^N)  |  Space: O(N) recursion stack
    // ============================================================
    // At each position, try all valid digits and recurse.
    // No caching, so states are recomputed.
    static class BruteForce {
        public static int countNumbers(int N, int S) {
            if (S <= 0 || S > 9 * N) return 0;
            return solve(N, S, 1);
        }

        private static int solve(int N, int remaining, int pos) {
            if (pos == N + 1) return remaining == 0 ? 1 : 0;
            int start = (pos == 1) ? 1 : 0;  // first digit cannot be 0
            int count = 0;
            for (int d = start; d <= 9 && d <= remaining; d++) {
                count = (count + solve(N, remaining - d, pos + 1)) % MOD;
            }
            return count;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (Top-Down DP with memoization)
    // Time: O(N * S * 10)  |  Space: O(N * S)
    // ============================================================
    // Same recursion but memoize on (pos, remaining).
    static class Optimal {
        private static int[][] memo;

        public static int countNumbers(int N, int S) {
            if (S <= 0 || S > 9 * N) return 0;
            memo = new int[N + 2][S + 1];
            for (int[] row : memo) java.util.Arrays.fill(row, -1);
            return solve(N, S, 1);
        }

        private static int solve(int N, int remaining, int pos) {
            if (pos == N + 1) return remaining == 0 ? 1 : 0;
            if (remaining < 0) return 0;
            if (memo[pos][remaining] != -1) return memo[pos][remaining];

            int start = (pos == 1) ? 1 : 0;
            int count = 0;
            for (int d = start; d <= 9 && d <= remaining; d++) {
                count = (count + solve(N, remaining - d, pos + 1)) % MOD;
            }
            memo[pos][remaining] = count;
            return count;
        }
    }

    // ============================================================
    // APPROACH 3: BEST (Bottom-Up DP)
    // Time: O(N * S * 10)  |  Space: O(S) — rolling array
    // ============================================================
    // dp[s] = number of ways to form a number of `pos` digits summing to s.
    // Iterate position by position; at each position update dp via a new array.
    //
    // Transition:
    //   new_dp[s] = sum over d in valid_digits of dp[s - d]
    //
    // Use a 1D rolling array: process S down to 0 at each step.
    static class Best {
        public static int countNumbers(int N, int S) {
            if (N == 0 || S <= 0 || S > 9 * N) return 0;

            // dp[s] = ways to fill current position and have remaining sum = s
            long[] dp = new long[S + 1];

            // Position 1: digits 1..9
            for (int d = 1; d <= 9 && d <= S; d++) {
                dp[d]++;
            }

            // Positions 2..N: digits 0..9
            for (int pos = 2; pos <= N; pos++) {
                long[] newDp = new long[S + 1];
                for (int s = 0; s <= S; s++) {
                    if (dp[s] == 0) continue;
                    for (int d = 0; d <= 9 && s + d <= S; d++) {
                        newDp[s + d] = (newDp[s + d] + dp[s]) % MOD;
                    }
                }
                dp = newDp;
            }

            return (int) dp[S];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== N Digit Numbers with Digit Sum S ===");

        // N=1, S=5  -> only "5" -> 1
        // N=2, S=5  -> 14,23,32,41,50 -> 5
        // N=2, S=1  -> only "10" -> 1
        // N=3, S=2  -> 101,110,200 -> 3
        // N=1, S=9  -> only "9" -> 1
        int[][] tests    = {{1,5},{2,5},{2,1},{3,2},{1,9},{2,10}};
        int[]   expected = {1,    5,    1,    3,    1,    1   };
        // N=2,S=10: 19,28,37,46,55,64,73,82,91 -> 9? Let's verify:
        // Actually: 19,28,37,46,55,64,73,82,91 = 9
        expected[5] = 9;

        for (int t = 0; t < tests.length; t++) {
            int N = tests[t][0], S = tests[t][1];
            int bf = BruteForce.countNumbers(N, S);
            int op = Optimal.countNumbers(N, S);
            int be = Best.countNumbers(N, S);
            String status = (bf == op && op == be && be == expected[t]) ? "PASS" : "FAIL";
            System.out.printf("[%s] N=%d, S=%-3d | Brute: %d | Optimal: %d | Best: %d | Expected: %d%n",
                              status, N, S, bf, op, be, expected[t]);
        }
    }
}
