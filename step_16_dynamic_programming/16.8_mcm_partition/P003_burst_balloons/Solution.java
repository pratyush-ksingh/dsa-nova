/**
 * Problem: Burst Balloons  (LeetCode 312)
 * Difficulty: HARD | XP: 50
 *
 * You are given n balloons indexed 0..n-1 with values nums[]. Bursting balloon i
 * yields nums[i-1] * nums[i] * nums[i+1] coins (out-of-range treated as 1).
 * Return the maximum coins collectible.
 *
 * Key insight: think about which balloon is burst LAST in each interval,
 * not which is burst first. Padding with virtual 1s at both ends simplifies
 * boundary handling.
 *
 * dp[i][j] = max coins bursting all balloons strictly between i and j.
 * dp[i][j] = max over k in (i+1..j-1) of:
 *              newNums[i]*newNums[k]*newNums[j] + dp[i][k] + dp[k][j]
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE (Try all burst orderings)
    // Time: O(n! * n)  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        /**
         * Generate all permutations of balloon indices.
         * For each permutation, simulate bursting in that order using a
         * shrinking list to track current neighbors.
         */
        static int maxCoins;

        static void permute(int[] arr, int start, int[] nums) {
            if (start == arr.length) {
                maxCoins = Math.max(maxCoins, simulate(arr, nums));
                return;
            }
            for (int i = start; i < arr.length; i++) {
                swap(arr, start, i);
                permute(arr, start + 1, nums);
                swap(arr, start, i);
            }
        }

        static int simulate(int[] order, int[] nums) {
            java.util.ArrayList<Integer> balls = new java.util.ArrayList<>();
            for (int v : nums) balls.add(v);
            int total = 0;
            for (int target : order) {
                // Find target's current index in the active list
                int pos = balls.indexOf(target);   // works if values unique; for general case track indices
                int left  = (pos > 0) ? balls.get(pos - 1) : 1;
                int right = (pos < balls.size() - 1) ? balls.get(pos + 1) : 1;
                total += left * balls.get(pos) * right;
                balls.remove(pos);
            }
            return total;
        }

        static void swap(int[] a, int i, int j) {
            int t = a[i]; a[i] = a[j]; a[j] = t;
        }

        static int maxCoins(int[] nums) {
            maxCoins = 0;
            int n = nums.length;
            int[] indices = new int[n];
            for (int i = 0; i < n; i++) indices[i] = i;
            // Note: this uses index-based permutation; simulate uses indices, not values
            permute(indices, 0, nums);
            return maxCoins;
        }

        // Re-implement simulate to use indices properly
        static int simulate(int[] order, int[] originalNums) {
            java.util.ArrayList<Integer> balls = new java.util.ArrayList<>();
            for (int v : originalNums) balls.add(v);
            int total = 0;
            for (int targetIdx : order) {
                // Find the value at targetIdx currently in balls
                // Since we permute indices and pop elements, we need to track
                // which position in balls corresponds to targetIdx
                // Simple approach: re-add mapping each time
                int pos = -1;
                // balls was built as a copy of originalNums in order, then shrunk.
                // We need to find where originalNums[targetIdx] currently is.
                // Use a separate position list for correctness.
                return -1;  // placeholder — see note below
            }
            return total;
        }
    }

    // ============================================================
    // APPROACH 1 (corrected): BRUTE FORCE using position tracking
    // ============================================================
    static class BruteForceCorrect {
        static int best;

        static void solve(java.util.ArrayList<Integer> balls, int coins) {
            if (balls.isEmpty()) {
                best = Math.max(best, coins);
                return;
            }
            for (int i = 0; i < balls.size(); i++) {
                int left  = (i > 0) ? balls.get(i - 1) : 1;
                int mid   = balls.get(i);
                int right = (i < balls.size() - 1) ? balls.get(i + 1) : 1;
                int gain  = left * mid * right;
                balls.remove(i);
                solve(balls, coins + gain);
                balls.add(i, mid);
            }
        }

        static int maxCoins(int[] nums) {
            best = 0;
            java.util.ArrayList<Integer> balls = new java.util.ArrayList<>();
            for (int v : nums) balls.add(v);
            solve(balls, 0);
            return best;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (Top-Down Interval DP / Memoization)
    // Time: O(n^3)  |  Space: O(n^2) memo + O(n) stack
    // ============================================================
    static class Optimal {
        /**
         * Pad with virtual 1s: newNums = [1, ...nums, 1].
         * dp(i, j) = max coins bursting all balloons strictly between i and j.
         * k is the LAST balloon burst in (i, j); its neighbors at burst time are i and j.
         */
        static int[] nn;
        static int[][] memo;

        static int dp(int i, int j) {
            if (j - i <= 1) return 0;
            if (memo[i][j] != -1) return memo[i][j];
            int best = 0;
            for (int k = i + 1; k < j; k++) {
                int coins = nn[i] * nn[k] * nn[j] + dp(i, k) + dp(k, j);
                best = Math.max(best, coins);
            }
            memo[i][j] = best;
            return best;
        }

        static int maxCoins(int[] nums) {
            int n = nums.length;
            nn = new int[n + 2];
            nn[0] = 1; nn[n + 1] = 1;
            for (int i = 0; i < n; i++) nn[i + 1] = nums[i];
            int m = n + 2;
            memo = new int[m][m];
            for (int[] row : memo) java.util.Arrays.fill(row, -1);
            return dp(0, m - 1);
        }
    }

    // ============================================================
    // APPROACH 3: BEST (Bottom-Up Interval DP)
    // Time: O(n^3)  |  Space: O(n^2) — no recursion overhead
    // ============================================================
    static class Best {
        /**
         * Same recurrence as Approach 2, filled iteratively by gap = j - i.
         * When we fill dp[i][j] for gap g, all dp values for smaller gaps are ready.
         */
        static int maxCoins(int[] nums) {
            int n = nums.length;
            int[] nn = new int[n + 2];
            nn[0] = 1; nn[n + 1] = 1;
            for (int i = 0; i < n; i++) nn[i + 1] = nums[i];
            int m = n + 2;
            int[][] dp = new int[m][m];

            for (int gap = 2; gap < m; gap++) {
                for (int i = 0; i + gap < m; i++) {
                    int j = i + gap;
                    for (int k = i + 1; k < j; k++) {
                        int coins = nn[i] * nn[k] * nn[j] + dp[i][k] + dp[k][j];
                        dp[i][j] = Math.max(dp[i][j], coins);
                    }
                }
            }
            return dp[0][m - 1];
        }
    }

    // ============================================================
    // MAIN — run all approaches on test cases
    // ============================================================
    public static void main(String[] args) {
        int[][] tests    = {{3,1,5,8}, {1,5}, {1}, {7,9,8,0,7,1,3,5,5,2,3}};
        int[]   expected = {167,        10,    1,   1654};

        System.out.println("=== Burst Balloons ===\n");
        for (int t = 0; t < tests.length; t++) {
            int[] nums = tests[t];
            int b  = BruteForceCorrect.maxCoins(nums.clone());
            int o  = Optimal.maxCoins(nums.clone());
            int be = Best.maxCoins(nums.clone());
            String status = (b == o && o == be && be == expected[t]) ? "PASS" : "FAIL";
            System.out.printf("nums=...  Brute=%d  Optimal=%d  Best=%d  Expected=%d  [%s]%n",
                              b, o, be, expected[t], status);
        }
    }
}
