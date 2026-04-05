/**
 * Problem: Target Sum (LeetCode 494)
 * Difficulty: MEDIUM | XP: 25
 *
 * Assign '+' or '-' to each element of nums and count assignments that
 * produce exactly `target`.
 *
 * Reduction: count subsets with sum = (total + target) / 2.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — plain recursion
    // Time: O(2^n)  |  Space: O(n) recursion stack
    // ============================================================
    static class BruteForce {
        public int findTargetSumWays(int[] nums, int target) {
            return recurse(nums, 0, 0, target);
        }

        private int recurse(int[] nums, int index, int current, int target) {
            if (index == nums.length) {
                return current == target ? 1 : 0;
            }
            int add = recurse(nums, index + 1, current + nums[index], target);
            int sub = recurse(nums, index + 1, current - nums[index], target);
            return add + sub;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — 2D DP (count subsets)
    // Time: O(n * S)  |  Space: O(n * S)  where S=(total+target)/2
    // ============================================================
    static class Optimal {
        public int findTargetSumWays(int[] nums, int target) {
            int total = 0;
            for (int x : nums) total += x;
            if ((total + target) % 2 != 0) return 0;
            if (Math.abs(target) > total) return 0;

            int req = (total + target) / 2;
            int n = nums.length;

            // dp[i][j] = number of subsets from nums[0..i-1] summing to j
            int[][] dp = new int[n + 1][req + 1];
            dp[0][0] = 1;

            for (int i = 1; i <= n; i++) {
                for (int j = 0; j <= req; j++) {
                    dp[i][j] = dp[i - 1][j]; // skip
                    if (j >= nums[i - 1]) {
                        dp[i][j] += dp[i - 1][j - nums[i - 1]]; // take
                    }
                }
            }
            return dp[n][req];
        }
    }

    // ============================================================
    // APPROACH 3: BEST — 1D DP (space-optimised)
    // Time: O(n * S)  |  Space: O(S)
    // ============================================================
    static class Best {
        public int findTargetSumWays(int[] nums, int target) {
            int total = 0;
            for (int x : nums) total += x;
            if ((total + target) % 2 != 0) return 0;
            if (Math.abs(target) > total) return 0;

            int req = (total + target) / 2;
            int[] dp = new int[req + 1];
            dp[0] = 1;

            for (int x : nums) {
                // Traverse right-to-left: 0/1 knapsack, each element used once
                for (int j = req; j >= x; j--) {
                    dp[j] += dp[j - x];
                }
            }
            return dp[req];
        }
    }

    public static void main(String[] args) {
        int[][] inputs = {
            {1, 1, 1, 1, 1},
            {1},
            {1},
            {0, 0, 0, 0, 0, 0, 0, 0, 1},
            {1, 2, 3, 1}
        };
        int[] targets  = {3, 1, 2, 1, 3};
        int[] expected = {5, 1, 0, 256, 2};

        BruteForce bf = new BruteForce();
        Optimal op = new Optimal();
        Best bt = new Best();

        System.out.println("=== Target Sum ===");
        for (int t = 0; t < inputs.length; t++) {
            int b  = bf.findTargetSumWays(inputs[t], targets[t]);
            int o  = op.findTargetSumWays(inputs[t], targets[t]);
            int bs = bt.findTargetSumWays(inputs[t], targets[t]);
            String status = (b == expected[t] && o == expected[t] && bs == expected[t]) ? "PASS" : "FAIL";
            System.out.printf("[%s] target=%d | brute=%d optimal=%d best=%d | expected=%d%n",
                    status, targets[t], b, o, bs, expected[t]);
        }
    }
}
