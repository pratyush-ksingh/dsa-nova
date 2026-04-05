/**
 * Problem: Maximum Sum of Non Adjacent Elements / House Robber (LeetCode #198)
 * Difficulty: MEDIUM | XP: 25
 *
 * Find maximum sum of non-adjacent elements in array.
 * All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(2^n) | Space: O(n)
// ============================================================
class RecursiveRobber {
    public int rob(int[] nums) {
        return solve(nums.length - 1, nums);
    }

    private int solve(int i, int[] nums) {
        if (i < 0) return 0;
        if (i == 0) return nums[0];

        // Pick element i: add nums[i], skip to i-2
        int pick = nums[i] + solve(i - 2, nums);
        // Skip element i: take best from i-1
        int skip = solve(i - 1, nums);

        return Math.max(pick, skip);
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n) | Space: O(n)
// ============================================================
class MemoRobber {
    public int rob(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, -1);
        return solve(n - 1, nums, dp);
    }

    private int solve(int i, int[] nums, int[] dp) {
        if (i < 0) return 0;
        if (i == 0) return nums[0];
        if (dp[i] != -1) return dp[i];

        int pick = nums[i] + solve(i - 2, nums, dp);
        int skip = solve(i - 1, nums, dp);

        dp[i] = Math.max(pick, skip);
        return dp[i];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(n) | Space: O(n)
// ============================================================
class TabRobber {
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];

        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]); // best of first two

        for (int i = 2; i < n; i++) {
            dp[i] = Math.max(
                dp[i - 1],                  // skip i
                dp[i - 2] + nums[i]         // pick i
            );
        }

        return dp[n - 1];
    }
}

// ============================================================
// Approach 4: Space Optimized
// Time: O(n) | Space: O(1)
// ============================================================
class SpaceRobber {
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];

        int prev2 = nums[0];                           // dp[0]
        int prev1 = Math.max(nums[0], nums[1]);        // dp[1]

        for (int i = 2; i < n; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Maximum Sum of Non Adjacent Elements (House Robber) ===\n");

        RecursiveRobber rec = new RecursiveRobber();
        MemoRobber memo = new MemoRobber();
        TabRobber tab = new TabRobber();
        SpaceRobber space = new SpaceRobber();

        int[][] testCases = {
            {2, 7, 9, 3, 1},
            {1, 2, 3, 1},
            {2, 1, 4, 5, 3, 1, 1, 3},
            {5},
            {3, 7},
            {5, 5, 5, 5},
            {100, 1, 100, 1},
            {1, 100, 1},
        };
        int[] expected = {12, 4, 12, 5, 7, 10, 200, 100};

        for (int t = 0; t < testCases.length; t++) {
            int r = rec.rob(testCases[t]);
            int m = memo.rob(testCases[t]);
            int tb = tab.rob(testCases[t]);
            int s = space.rob(testCases[t]);

            boolean pass = r == expected[t] && m == expected[t]
                    && tb == expected[t] && s == expected[t];

            System.out.println("nums = " + Arrays.toString(testCases[t]));
            System.out.println("  Recursive: " + r + " | Memo: " + m
                    + " | Tab: " + tb + " | Space: " + s);
            System.out.println("  Expected: " + expected[t] + " | Pass: " + pass + "\n");
        }
    }
}
