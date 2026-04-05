import java.util.*;

/**
 * Jump Game II (LeetCode #45)
 *
 * Find minimum number of jumps to reach the last index.
 * nums[i] = maximum jump length from index i.
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Dynamic Programming
    // Time: O(N^2)  |  Space: O(N)
    // ============================================================
    public static int bruteForce(int[] nums) {
        int n = nums.length;
        if (n <= 1) return 0;

        int[] dp = new int[n];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (j + nums[j] >= i && dp[j] != Integer.MAX_VALUE) {
                    dp[i] = Math.min(dp[i], dp[j] + 1);
                }
            }
        }

        return dp[n - 1];
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- BFS level-by-level greedy
    // Time: O(N)  |  Space: O(1)
    // ============================================================
    public static int optimal(int[] nums) {
        int n = nums.length;
        if (n <= 1) return 0;

        int jumps = 0;
        int currentEnd = 0;   // end of current BFS level
        int farthest = 0;     // farthest reachable from current level

        for (int i = 0; i < n - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);

            if (i == currentEnd) {
                // Must take a new jump
                jumps++;
                currentEnd = farthest;

                if (currentEnd >= n - 1) break;
            }
        }

        return jumps;
    }

    // ============================================================
    // APPROACH 3: BEST -- Same greedy, cleanest form
    // Time: O(N)  |  Space: O(1)
    // ============================================================
    public static int best(int[] nums) {
        int n = nums.length;
        if (n <= 1) return 0;

        int jumps = 0, curEnd = 0, farthest = 0;

        for (int i = 0; i < n - 1; i++) {
            farthest = Math.max(farthest, i + nums[i]);
            if (i == curEnd) {
                jumps++;
                curEnd = farthest;
                // Early termination
                if (curEnd >= n - 1) return jumps;
            }
        }

        return jumps;
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        int[][] tests = {
            {2, 3, 1, 1, 4},   // 2
            {2, 3, 0, 1, 4},   // 2
            {1, 1, 1, 1},      // 3
            {0},                // 0
            {1, 2},             // 1
            {5, 4, 3, 2, 1},   // 1
        };
        int[] expected = {2, 2, 3, 0, 1, 1};

        System.out.println("=== Jump Game II ===");
        for (int t = 0; t < tests.length; t++) {
            int b = bruteForce(tests[t]);
            int o = optimal(tests[t]);
            int s = best(tests[t]);
            System.out.printf("%-20s -> Brute: %d | Optimal: %d | Best: %d (expected %d)%n",
                Arrays.toString(tests[t]), b, o, s, expected[t]);
        }
    }
}
