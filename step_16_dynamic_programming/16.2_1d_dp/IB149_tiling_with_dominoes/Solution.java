import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Recursive with memoization
// Time: O(N)  |  Space: O(N)
// dp[n] = number of ways to tile 3xN board with 2x1 dominoes.
// Recurrence: dp[n] = 4*dp[n-2] - dp[n-4]  (for n >= 4, n even)
// Only even N can be tiled (3*N must be even), odd N -> 0 ways
// ============================================================
class BruteForce {
    static final int MOD = 1_000_000_007;
    static Map<Integer, Long> memo = new HashMap<>();

    public static long solve(int n) {
        if (n % 2 != 0) return 0;  // 3*n must be divisible by 2*1=2 -> n must be even
        if (n == 0) return 1;
        if (n == 2) return 3;
        if (memo.containsKey(n)) return memo.get(n);
        long result = (4 * solve(n - 2) % MOD - solve(n - 4) % MOD + MOD) % MOD;
        memo.put(n, result);
        return result;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Bottom-up DP
// Time: O(N)  |  Space: O(N)
// dp[i] = ways to tile 3xi board
// dp[0]=1, dp[2]=3, dp[n]=4*dp[n-2]-dp[n-4] for even n
// ============================================================
class Optimal {
    static final int MOD = 1_000_000_007;

    public static long solve(int n) {
        if (n % 2 != 0) return 0;
        if (n == 0) return 1;
        long[] dp = new long[n + 1];
        dp[0] = 1;
        dp[2] = 3;
        for (int i = 4; i <= n; i += 2) {
            dp[i] = (4 * dp[i - 2] % MOD - dp[i - 4] + MOD) % MOD;
        }
        return dp[n];
    }
}

// ============================================================
// APPROACH 3: BEST - O(1) space using only last two values
// Time: O(N)  |  Space: O(1)
// Only dp[i-2] and dp[i-4] needed, use two variables
// ============================================================
class Best {
    static final int MOD = 1_000_000_007;

    public static long solve(int n) {
        if (n % 2 != 0) return 0;
        if (n == 0) return 1;
        if (n == 2) return 3;
        long prev2 = 1; // dp[i-4]
        long prev1 = 3; // dp[i-2]
        long curr = 0;
        for (int i = 4; i <= n; i += 2) {
            curr = (4 * prev1 % MOD - prev2 + MOD) % MOD;
            prev2 = prev1;
            prev1 = curr;
        }
        return curr;
    }
}

public class Solution {
    public static void main(String[] args) {
        // n=2: 3 ways, n=4: 11 ways, n=6: 41 ways
        int[] tests = {0, 1, 2, 4, 6, 8};
        long[] expected = {1, 0, 3, 11, 41, 153};
        for (int i = 0; i < tests.length; i++) {
            long b = BruteForce.solve(tests[i]);
            long o = Optimal.solve(tests[i]);
            long best = Best.solve(tests[i]);
            System.out.printf("n=%d: Brute=%d, Optimal=%d, Best=%d (expected=%d) %s%n",
                tests[i], b, o, best, expected[i],
                (b == o && o == best && best == expected[i]) ? "OK" : "FAIL");
        }
    }
}
