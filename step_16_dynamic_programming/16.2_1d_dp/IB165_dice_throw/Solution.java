import java.util.*;

/**
 * Problem: Dice Throw
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given N dice each with M faces (1..M), count the number of ways to get sum S.
 * Classic bounded knapsack / DP problem.
 *
 * @author DSA_Nova
 */

// ============================================================
// APPROACH 1: BRUTE FORCE
// Recursive with memoization: dp[n][s] = ways to get sum s using n dice
// Time: O(N * S * M)  |  Space: O(N * S)
// ============================================================
class BruteForce {
    static int M_faces;
    static Map<String, Long> memo = new HashMap<>();

    static long dp(int diceLeft, int sumLeft) {
        if (diceLeft == 0) return sumLeft == 0 ? 1 : 0;
        if (sumLeft <= 0) return 0;
        String key = diceLeft + "," + sumLeft;
        if (memo.containsKey(key)) return memo.get(key);
        long ways = 0;
        for (int face = 1; face <= M_faces && face <= sumLeft; face++) {
            ways += dp(diceLeft - 1, sumLeft - face);
        }
        memo.put(key, ways);
        return ways;
    }

    static long countWays(int N, int M, int S) {
        M_faces = M;
        memo.clear();
        return dp(N, S);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Bottom-up DP: dp[i][j] = ways to get sum j using i dice
// dp[i][j] = sum(dp[i-1][j-face]) for face in 1..M
// Time: O(N * S * M)  |  Space: O(N * S)
// ============================================================
class Optimal {
    static long countWays(int N, int M, int S) {
        long[][] dp = new long[N + 1][S + 1];
        dp[0][0] = 1;

        for (int i = 1; i <= N; i++) {
            for (int j = i; j <= S; j++) {
                for (int face = 1; face <= M && face <= j; face++) {
                    dp[i][j] += dp[i - 1][j - face];
                }
            }
        }
        return dp[N][S];
    }
}

// ============================================================
// APPROACH 3: BEST
// Space-optimized: only keep previous row (1D DP with rolling array)
// Use prefix sums to avoid inner loop over faces
// Time: O(N * S)  |  Space: O(S)
// ============================================================
class Best {
    static long countWays(int N, int M, int S) {
        long[] prev = new long[S + 1];
        prev[0] = 1;

        for (int i = 0; i < N; i++) {
            long[] curr = new long[S + 1];
            // Build prefix sum of prev for O(1) range queries
            long[] prefix = new long[S + 2];
            for (int j = 0; j <= S; j++) prefix[j + 1] = prefix[j] + prev[j];

            for (int j = 1; j <= S; j++) {
                // sum of prev[j-M..j-1] (faces 1..M)
                int lo = Math.max(0, j - M);
                curr[j] = prefix[j] - prefix[lo];
            }
            prev = curr;
        }
        return prev[S];
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Dice Throw ===");

        // 2 dice, 6 faces, sum 7 => 6 ways
        System.out.println("BruteForce N=2 M=6 S=7: " + BruteForce.countWays(2, 6, 7));  // 6
        System.out.println("Optimal    N=2 M=6 S=7: " + Optimal.countWays(2, 6, 7));     // 6
        System.out.println("Best       N=2 M=6 S=7: " + Best.countWays(2, 6, 7));        // 6

        // 1 die, 6 faces, sum 3 => 1 way
        System.out.println("N=1 M=6 S=3: " + Optimal.countWays(1, 6, 3));  // 1

        // 3 dice, 6 faces, sum 8
        System.out.println("N=3 M=6 S=8: " + Optimal.countWays(3, 6, 8));  // 21

        // Large: 3 dice, 6 faces, sum 18 => 1 way (all 6s)
        System.out.println("N=3 M=6 S=18: " + Optimal.countWays(3, 6, 18)); // 1
    }
}
