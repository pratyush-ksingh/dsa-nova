/**
 * Problem: Partition Equal Subset Sum (LeetCode #416)
 * Difficulty: MEDIUM | XP: 25
 *
 * Can array be partitioned into two subsets with equal sum?
 * Reduce to: Subset Sum = totalSum / 2
 * All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(2^n) | Space: O(n)
// ============================================================
class RecursivePartition {
    public boolean canPartition(int[] nums) {
        int totalSum = 0;
        for (int num : nums) totalSum += num;

        // Odd sum can never be split equally
        if (totalSum % 2 != 0) return false;

        int target = totalSum / 2;
        return solve(nums.length - 1, target, nums);
    }

    private boolean solve(int i, int target, int[] nums) {
        if (target == 0) return true;
        if (i == 0) return nums[0] == target;

        // Exclude nums[i]
        boolean notTake = solve(i - 1, target, nums);

        // Include nums[i] (only if it doesn't exceed target)
        boolean take = false;
        if (nums[i] <= target) {
            take = solve(i - 1, target - nums[i], nums);
        }

        return notTake || take;
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n * target) | Space: O(n * target)
// ============================================================
class MemoPartition {
    public boolean canPartition(int[] nums) {
        int totalSum = 0;
        for (int num : nums) totalSum += num;
        if (totalSum % 2 != 0) return false;

        int target = totalSum / 2;
        int n = nums.length;
        // 0 = unvisited, 1 = true, 2 = false
        int[][] dp = new int[n][target + 1];
        return solve(n - 1, target, nums, dp);
    }

    private boolean solve(int i, int target, int[] nums, int[][] dp) {
        if (target == 0) return true;
        if (i == 0) return nums[0] == target;
        if (dp[i][target] != 0) return dp[i][target] == 1;

        boolean notTake = solve(i - 1, target, nums, dp);
        boolean take = false;
        if (nums[i] <= target) {
            take = solve(i - 1, target - nums[i], nums, dp);
        }

        dp[i][target] = (notTake || take) ? 1 : 2;
        return notTake || take;
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(n * target) | Space: O(n * target)
// ============================================================
class TabPartition {
    public boolean canPartition(int[] nums) {
        int totalSum = 0;
        for (int num : nums) totalSum += num;
        if (totalSum % 2 != 0) return false;

        int target = totalSum / 2;
        int n = nums.length;
        boolean[][] dp = new boolean[n][target + 1];

        // Base case: sum = 0 always achievable
        for (int i = 0; i < n; i++) {
            dp[i][0] = true;
        }

        // Base case: first element
        if (nums[0] <= target) {
            dp[0][nums[0]] = true;
        }

        // Fill table
        for (int i = 1; i < n; i++) {
            for (int s = 1; s <= target; s++) {
                boolean notTake = dp[i - 1][s];
                boolean take = false;
                if (nums[i] <= s) {
                    take = dp[i - 1][s - nums[i]];
                }
                dp[i][s] = notTake || take;
            }
        }

        return dp[n - 1][target];
    }
}

// ============================================================
// Approach 4: Space Optimized
// Time: O(n * target) | Space: O(target)
// ============================================================
class SpacePartition {
    public boolean canPartition(int[] nums) {
        int totalSum = 0;
        for (int num : nums) totalSum += num;
        if (totalSum % 2 != 0) return false;

        int target = totalSum / 2;
        int n = nums.length;
        boolean[] prev = new boolean[target + 1];

        // Base case
        prev[0] = true;
        if (nums[0] <= target) {
            prev[nums[0]] = true;
        }

        for (int i = 1; i < n; i++) {
            boolean[] curr = new boolean[target + 1];
            curr[0] = true;
            for (int s = 1; s <= target; s++) {
                boolean notTake = prev[s];
                boolean take = false;
                if (nums[i] <= s) {
                    take = prev[s - nums[i]];
                }
                curr[s] = notTake || take;
            }
            prev = curr;
        }

        return prev[target];
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Partition Equal Subset Sum ===\n");

        RecursivePartition rec = new RecursivePartition();
        MemoPartition memo = new MemoPartition();
        TabPartition tab = new TabPartition();
        SpacePartition space = new SpacePartition();

        int[][] arrays = {
            {1, 5, 11, 5},
            {1, 2, 3, 5},
            {2, 2, 1, 1},
            {1, 2, 5},
            {3, 3},
            {1},
            {1, 1, 1, 1},
        };
        boolean[] expected = {true, false, true, false, true, false, true};

        for (int t = 0; t < arrays.length; t++) {
            boolean r = rec.canPartition(arrays[t]);
            boolean m = memo.canPartition(arrays[t]);
            boolean tb = tab.canPartition(arrays[t]);
            boolean s = space.canPartition(arrays[t]);

            boolean pass = r == expected[t] && m == expected[t]
                    && tb == expected[t] && s == expected[t];

            System.out.println("nums=" + Arrays.toString(arrays[t]));
            System.out.println("  Rec=" + r + " | Memo=" + m + " | Tab=" + tb + " | Space=" + s);
            System.out.println("  Expected=" + expected[t] + " | Pass=" + pass + "\n");
        }
    }
}
