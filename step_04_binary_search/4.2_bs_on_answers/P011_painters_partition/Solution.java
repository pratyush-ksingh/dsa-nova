/**
 * Problem: Painters Partition
 * Difficulty: HARD | XP: 50
 *
 * Given N boards and K painters, each painter paints contiguous boards.
 * Each unit of board takes 1 unit of time. Find the minimum time to paint all boards.
 * (Minimize the maximum work assigned to any single painter.)
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE  (DP)
    // Time: O(k * n^2)  |  Space: O(k * n)
    // ============================================================
    /**
     * DP: dp[i][j] = min possible max when partitioning first j boards among i painters.
     * dp[i][j] = min over all splits m of max(dp[i-1][m], sum(boards[m+1..j]))
     * Real-life: Task scheduling across limited workers to minimize bottleneck.
     */
    public static int bruteForce(int[] boards, int k) {
        int n = boards.length;
        // Prefix sums for range sums
        long[] prefix = new long[n + 1];
        for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + boards[i];

        // dp[i][j] = min-max when partitioning boards[0..j-1] among i painters
        long[][] dp = new long[k + 1][n + 1];
        for (long[] row : dp) Arrays.fill(row, Long.MAX_VALUE / 2);
        dp[0][0] = 0;

        // 1 painter: must take all boards up to j
        for (int j = 1; j <= n; j++) dp[1][j] = prefix[j];

        for (int i = 2; i <= k; i++) {
            for (int j = i; j <= n; j++) {
                for (int m = i - 1; m < j; m++) {
                    long cost = Math.max(dp[i - 1][m], prefix[j] - prefix[m]);
                    dp[i][j] = Math.min(dp[i][j], cost);
                }
            }
        }
        return (int) dp[k][n];
    }

    // ============================================================
    // APPROACH 2: OPTIMAL  (Binary Search on Answer)
    // Time: O(n log(sum))  |  Space: O(1)
    // ============================================================
    /**
     * Binary search on the answer (max time). For a given mid value,
     * greedily count how many painters are needed. If painters <= k, mid might be valid.
     * Real-life: Resource allocation in factory scheduling — minimizing the slowest line.
     */
    public static int optimal(int[] boards, int k) {
        long lo = Arrays.stream(boards).asLongStream().max().getAsLong();
        long hi = Arrays.stream(boards).asLongStream().sum();

        long result = hi;
        while (lo <= hi) {
            long mid = lo + (hi - lo) / 2;
            if (isPossible(boards, k, mid)) {
                result = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return (int) result;
    }

    private static boolean isPossible(int[] boards, int k, long maxTime) {
        int painters = 1;
        long current = 0;
        for (int board : boards) {
            if (current + board > maxTime) {
                painters++;
                current = board;
                if (painters > k) return false;
            } else {
                current += board;
            }
        }
        return true;
    }

    // ============================================================
    // APPROACH 3: BEST
    // Time: O(n log(sum))  |  Space: O(1)
    // ============================================================
    /**
     * Same binary search — already optimal. Slight refinement: handles edge case
     * where k >= n (each board gets its own painter, answer = max board).
     * Real-life: Distributed computing job scheduler — minimize longest-running node.
     */
    public static int best(int[] boards, int k) {
        int n = boards.length;
        if (k >= n) return Arrays.stream(boards).max().getAsInt();
        return optimal(boards, k);
    }

    public static void main(String[] args) {
        System.out.println("=== Painters Partition ===");

        int[][] testBoards = {
            {10, 20, 30, 40},
            {100, 200, 300, 400, 500},
            {5, 5, 5, 5},
        };
        int[] testK   = {2, 3, 2};
        int[] expected = {60, 500, 10};

        for (int t = 0; t < testBoards.length; t++) {
            System.out.println("\nBoards: " + Arrays.toString(testBoards[t]) + "  K=" + testK[t]);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Brute:   " + bruteForce(testBoards[t], testK[t]));
            System.out.println("Optimal: " + optimal(testBoards[t], testK[t]));
            System.out.println("Best:    " + best(testBoards[t], testK[t]));
        }
    }
}
