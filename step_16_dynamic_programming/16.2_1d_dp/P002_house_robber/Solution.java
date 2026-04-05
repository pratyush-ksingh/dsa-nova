import java.util.*;

// ============================================================
// APPROACH 1: Recursion (Brute Force)
// Time: O(2^n) | Space: O(n)
// ============================================================
class HouseRobber_Recursion {
    public int rob(int[] nums) {
        return solve(nums, 0);
    }

    // solve(i) = max money from house i onward
    private int solve(int[] nums, int i) {
        if (i >= nums.length) return 0;

        // Choice 1: Skip house i
        int skip = solve(nums, i + 1);

        // Choice 2: Rob house i, then skip i+1
        int steal = nums[i] + solve(nums, i + 2);

        return Math.max(skip, steal);
    }
}

// ============================================================
// APPROACH 2: Memoization (Top-Down DP)
// Time: O(n) | Space: O(n)
// ============================================================
class HouseRobber_Memoization {
    public int rob(int[] nums) {
        int[] dp = new int[nums.length];
        Arrays.fill(dp, -1);
        return solve(nums, 0, dp);
    }

    private int solve(int[] nums, int i, int[] dp) {
        if (i >= nums.length) return 0;
        if (dp[i] != -1) return dp[i];

        int skip = solve(nums, i + 1, dp);
        int steal = nums[i] + solve(nums, i + 2, dp);

        dp[i] = Math.max(skip, steal);
        return dp[i];
    }
}

// ============================================================
// APPROACH 3: Tabulation (Bottom-Up DP)
// Time: O(n) | Space: O(n)
// dp[i] = max money considering houses 0..i
// ============================================================
class HouseRobber_Tabulation {
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];

        int[] dp = new int[n];
        dp[0] = nums[0];
        dp[1] = Math.max(nums[0], nums[1]);

        for (int i = 2; i < n; i++) {
            // Either skip house i (carry dp[i-1]) or rob it (dp[i-2] + nums[i])
            dp[i] = Math.max(dp[i - 1], dp[i - 2] + nums[i]);
        }

        return dp[n - 1];
    }
}

// ============================================================
// APPROACH 4: Space-Optimized (Two Variables)
// Time: O(n) | Space: O(1)
// dp[i] depends only on dp[i-1] and dp[i-2]
// ============================================================
class HouseRobber_SpaceOptimized {
    public int rob(int[] nums) {
        int n = nums.length;
        if (n == 1) return nums[0];

        int prev2 = nums[0];                      // dp[i-2]
        int prev1 = Math.max(nums[0], nums[1]);   // dp[i-1]

        for (int i = 2; i < n; i++) {
            int curr = Math.max(prev1, prev2 + nums[i]);
            prev2 = prev1;
            prev1 = curr;
        }

        return prev1;
    }
}

// ============================================================
// TEST HARNESS
// ============================================================
public class Solution {
    public static void main(String[] args) {
        int[][] testCases = {
            {1, 2, 3, 1},          // Expected: 4
            {2, 7, 9, 3, 1},       // Expected: 12
            {2, 1, 1, 2},          // Expected: 4
            {5},                    // Expected: 5
            {1, 2},                 // Expected: 2
            {0, 0, 0},             // Expected: 0
            {5, 5, 5, 5},          // Expected: 10
            {1, 2, 3, 4, 5},       // Expected: 9
            {100, 1, 1, 100},      // Expected: 200
            {1, 3, 1, 3, 100},     // Expected: 103
        };
        int[] expected = {4, 12, 4, 5, 2, 0, 10, 9, 200, 103};

        HouseRobber_Recursion sol1 = new HouseRobber_Recursion();
        HouseRobber_Memoization sol2 = new HouseRobber_Memoization();
        HouseRobber_Tabulation sol3 = new HouseRobber_Tabulation();
        HouseRobber_SpaceOptimized sol4 = new HouseRobber_SpaceOptimized();

        System.out.println("=== House Robber ===\n");

        for (int i = 0; i < testCases.length; i++) {
            int[] tc = testCases[i];
            int exp = expected[i];

            int r1 = sol1.rob(tc);
            int r2 = sol2.rob(tc);
            int r3 = sol3.rob(tc);
            int r4 = sol4.rob(tc);

            boolean pass = (r1 == exp && r2 == exp && r3 == exp && r4 == exp);
            String status = pass ? "PASS" : "FAIL";

            System.out.printf("Test %d: %s | Input: %s | Expected: %d | Got: [%d, %d, %d, %d]%n",
                i + 1, status, Arrays.toString(tc), exp, r1, r2, r3, r4);
        }

        System.out.println("\nAll tests completed.");
    }
}
