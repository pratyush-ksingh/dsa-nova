/**
 * Problem: House Robber II (LeetCode #213)
 * Difficulty: MEDIUM | XP: 25
 *
 * Houses in a circle. Run House Robber twice: skip first OR skip last.
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
        int n = nums.length;
        if (n == 1) return nums[0];

        // Max of: skip last house, skip first house
        return Math.max(
            solveLinear(nums, 0, n - 2),
            solveLinear(nums, 1, n - 1)
        );
    }

    private int solveLinear(int[] nums, int start, int end) {
        return solve(nums, end, start);
    }

    private int solve(int[] nums, int i, int start) {
        if (i < start) return 0;
        if (i == start) return nums[start];

        return Math.max(
            solve(nums, i - 1, start),           // skip house i
            nums[i] + solve(nums, i - 2, start)  // rob house i
        );
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n) | Space: O(n)
// ============================================================
class MemoRobber {
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];

        return Math.max(
            solveLinear(nums, 0, n - 2),
            solveLinear(nums, 1, n - 1)
        );
    }

    private int solveLinear(int[] nums, int start, int end) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, -1);
        return solve(nums, end, start, dp);
    }

    private int solve(int[] nums, int i, int start, int[] dp) {
        if (i < start) return 0;
        if (i == start) return nums[start];
        if (dp[i] != -1) return dp[i];

        dp[i] = Math.max(
            solve(nums, i - 1, start, dp),
            nums[i] + solve(nums, i - 2, start, dp)
        );
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

        return Math.max(
            robLinear(nums, 0, n - 2),
            robLinear(nums, 1, n - 1)
        );
    }

    private int robLinear(int[] nums, int start, int end) {
        int len = end - start + 1;
        if (len == 1) return nums[start];

        int[] dp = new int[len];
        dp[0] = nums[start];
        dp[1] = Math.max(nums[start], nums[start + 1]);

        for (int i = 2; i < len; i++) {
            dp[i] = Math.max(dp[i - 1], nums[start + i] + dp[i - 2]);
        }

        return dp[len - 1];
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

        return Math.max(
            robLinear(nums, 0, n - 2),
            robLinear(nums, 1, n - 1)
        );
    }

    private int robLinear(int[] nums, int start, int end) {
        int prev2 = 0, prev1 = 0;

        for (int i = start; i <= end; i++) {
            int curr = Math.max(prev1, nums[i] + prev2);
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
        System.out.println("=== House Robber II ===\n");

        RecursiveRobber rec = new RecursiveRobber();
        MemoRobber memo = new MemoRobber();
        TabRobber tab = new TabRobber();
        SpaceRobber space = new SpaceRobber();

        int[][] houses = {
            {2, 3, 2},
            {1, 2, 3, 1},
            {1, 2, 3},
            {0},
            {5},
            {1, 2},
            {3, 3, 3, 3},
            {1, 100, 1, 100},
        };
        int[] expected = {3, 4, 3, 0, 5, 2, 6, 200};

        for (int t = 0; t < houses.length; t++) {
            int r = rec.rob(houses[t]);
            int m = memo.rob(houses[t]);
            int tb = tab.rob(houses[t]);
            int s = space.rob(houses[t]);

            boolean pass = r == expected[t] && m == expected[t]
                    && tb == expected[t] && s == expected[t];

            System.out.println("nums=" + Arrays.toString(houses[t]));
            System.out.println("  Rec=" + r + " | Memo=" + m + " | Tab=" + tb + " | Space=" + s);
            System.out.println("  Expected=" + expected[t] + " | Pass=" + pass + "\n");
        }
    }
}
