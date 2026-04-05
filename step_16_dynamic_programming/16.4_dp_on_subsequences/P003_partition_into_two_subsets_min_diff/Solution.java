/**
 * Problem: Partition into Two Subsets Min Diff
 * Difficulty: HARD | XP: 50
 *
 * Given an array of integers, partition it into two subsets such that
 * the absolute difference between their sums is minimized.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(2^n)  |  Space: O(n) recursion stack
    // Try every possible assignment of each element to S1 or S2
    // ============================================================
    static class BruteForce {
        private int minDiff;
        private int total;

        public int minPartitionDiff(int[] nums) {
            total = 0;
            for (int x : nums) total += x;
            minDiff = Integer.MAX_VALUE;
            recurse(nums, 0, 0);
            return minDiff;
        }

        private void recurse(int[] nums, int index, int s1) {
            if (index == nums.length) {
                int s2 = total - s1;
                minDiff = Math.min(minDiff, Math.abs(s1 - s2));
                return;
            }
            recurse(nums, index + 1, s1 + nums[index]); // put in S1
            recurse(nums, index + 1, s1);               // put in S2
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — 2D boolean DP
    // Time: O(n * totalSum)  |  Space: O(n * totalSum)
    // dp[i][j] = can we form sum j from the first i elements?
    // ============================================================
    static class Optimal {
        public int minPartitionDiff(int[] nums) {
            int n = nums.length;
            int total = 0;
            for (int x : nums) total += x;

            boolean[][] dp = new boolean[n + 1][total + 1];
            for (int i = 0; i <= n; i++) dp[i][0] = true;

            for (int i = 1; i <= n; i++) {
                for (int j = 0; j <= total; j++) {
                    dp[i][j] = dp[i - 1][j];
                    if (j >= nums[i - 1]) {
                        dp[i][j] = dp[i][j] || dp[i - 1][j - nums[i - 1]];
                    }
                }
            }

            int minDiff = Integer.MAX_VALUE;
            for (int s1 = 0; s1 <= total / 2; s1++) {
                if (dp[n][s1]) {
                    minDiff = Math.min(minDiff, total - 2 * s1);
                }
            }
            return minDiff;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — 1D rolling DP
    // Time: O(n * totalSum)  |  Space: O(totalSum)
    // Compress 2D DP to 1D by iterating j in reverse order
    // ============================================================
    static class Best {
        public int minPartitionDiff(int[] nums) {
            int total = 0;
            for (int x : nums) total += x;

            boolean[] dp = new boolean[total + 1];
            dp[0] = true;

            for (int x : nums) {
                // Traverse right-to-left to avoid reusing x in same row
                for (int j = total; j >= x; j--) {
                    dp[j] = dp[j] || dp[j - x];
                }
            }

            int minDiff = Integer.MAX_VALUE;
            for (int s1 = 0; s1 <= total / 2; s1++) {
                if (dp[s1]) {
                    minDiff = Math.min(minDiff, total - 2 * s1);
                }
            }
            return minDiff;
        }
    }

    public static void main(String[] args) {
        int[][] testInputs = {
            {1, 6, 11, 5},
            {3, 9, 7, 3},
            {1, 2, 3, 4},
            {10},
            {1, 1}
        };
        int[] expected = {1, 2, 0, 10, 0};

        BruteForce bf = new BruteForce();
        Optimal op = new Optimal();
        Best bt = new Best();

        System.out.println("=== Partition into Two Subsets Min Diff ===");
        for (int t = 0; t < testInputs.length; t++) {
            int b = bf.minPartitionDiff(testInputs[t]);
            int o = op.minPartitionDiff(testInputs[t]);
            int bs = bt.minPartitionDiff(testInputs[t]);
            String status = (b == expected[t] && o == expected[t] && bs == expected[t]) ? "PASS" : "FAIL";
            System.out.printf("[%s] brute=%d optimal=%d best=%d expected=%d%n",
                    status, b, o, bs, expected[t]);
        }
    }
}
