/**
 * Problem: Print Longest Increasing Subsequence
 * Difficulty: MEDIUM | XP: 25
 *
 * Not just LENGTH but actually PRINT the LIS.
 * Backtrack using parent array from O(n^2) tabulation.
 * Approaches: Recursion -> Memo -> Tab + Parent -> Binary Search (length)
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Plain Recursion (Length only)
// Time: O(2^n) | Space: O(n)
// ============================================================
class RecursiveLIS {
    public int lisLength(int[] nums) {
        return solve(0, -1, nums);
    }

    private int solve(int i, int prevIdx, int[] nums) {
        if (i == nums.length) return 0;

        // Skip nums[i]
        int notTake = solve(i + 1, prevIdx, nums);

        // Take nums[i] if it continues the increasing sequence
        int take = 0;
        if (prevIdx == -1 || nums[i] > nums[prevIdx]) {
            take = 1 + solve(i + 1, i, nums);
        }

        return Math.max(take, notTake);
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n^2) | Space: O(n^2)
// ============================================================
class MemoLIS {
    public int lisLength(int[] nums) {
        int n = nums.length;
        // dp[i][prevIdx+1] to handle -1 index
        int[][] dp = new int[n][n + 1];
        for (int[] row : dp) Arrays.fill(row, -1);
        return solve(0, -1, nums, dp);
    }

    private int solve(int i, int prevIdx, int[] nums, int[][] dp) {
        if (i == nums.length) return 0;
        if (dp[i][prevIdx + 1] != -1) return dp[i][prevIdx + 1];

        int notTake = solve(i + 1, prevIdx, nums, dp);
        int take = 0;
        if (prevIdx == -1 || nums[i] > nums[prevIdx]) {
            take = 1 + solve(i + 1, i, nums, dp);
        }

        dp[i][prevIdx + 1] = Math.max(take, notTake);
        return dp[i][prevIdx + 1];
    }
}

// ============================================================
// Approach 3: Tabulation + Parent Tracking (PRINT the LIS)
// Time: O(n^2) | Space: O(n)
// ============================================================
class PrintLIS {
    public List<Integer> lis(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];       // dp[i] = LIS length ending at i
        int[] parent = new int[n];   // parent[i] = previous index in LIS

        // Initialize: each element is an LIS of length 1
        Arrays.fill(dp, 1);
        for (int i = 0; i < n; i++) parent[i] = i;

        int maxLen = 1, maxIdx = 0;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i] && dp[j] + 1 > dp[i]) {
                    dp[i] = dp[j] + 1;
                    parent[i] = j;
                }
            }
            if (dp[i] > maxLen) {
                maxLen = dp[i];
                maxIdx = i;
            }
        }

        // Backtrack from maxIdx to reconstruct LIS
        List<Integer> result = new ArrayList<>();
        int idx = maxIdx;
        while (parent[idx] != idx) {
            result.add(nums[idx]);
            idx = parent[idx];
        }
        result.add(nums[idx]); // Add the starting element

        Collections.reverse(result);
        return result;
    }
}

// ============================================================
// Approach 4: Binary Search (Length only) - O(n log n)
// Time: O(n log n) | Space: O(n)
// ============================================================
class BinarySearchLIS {
    public int lisLength(int[] nums) {
        // tails[i] = smallest tail element for IS of length i+1
        List<Integer> tails = new ArrayList<>();

        for (int num : nums) {
            int pos = Collections.binarySearch(tails, num);
            if (pos < 0) pos = -(pos + 1); // insertion point

            if (pos == tails.size()) {
                tails.add(num);  // Extend the longest IS
            } else {
                tails.set(pos, num); // Replace to keep smallest tail
            }
        }

        return tails.size();
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Print Longest Increasing Subsequence ===\n");

        RecursiveLIS rec = new RecursiveLIS();
        MemoLIS memo = new MemoLIS();
        PrintLIS printer = new PrintLIS();
        BinarySearchLIS bs = new BinarySearchLIS();

        int[][] arrays = {
            {10, 9, 2, 5, 3, 7, 101, 18},
            {0, 1, 0, 3, 2, 3},
            {7, 7, 7, 7},
            {5, 4, 3, 2, 1},
            {1, 2, 3, 4},
            {5},
            {-1, 3, -2, 5},
        };
        int[] expectedLen = {4, 4, 1, 1, 4, 1, 3};

        for (int t = 0; t < arrays.length; t++) {
            int r = rec.lisLength(arrays[t]);
            int m = memo.lisLength(arrays[t]);
            List<Integer> printed = printer.lis(arrays[t]);
            int b = bs.lisLength(arrays[t]);

            boolean pass = r == expectedLen[t] && m == expectedLen[t]
                    && printed.size() == expectedLen[t] && b == expectedLen[t];

            System.out.println("nums=" + Arrays.toString(arrays[t]));
            System.out.println("  Rec=" + r + " | Memo=" + m + " | BS=" + b
                    + " | Print=" + printed);
            System.out.println("  Expected len=" + expectedLen[t]
                    + " | Pass=" + pass + "\n");
        }
    }
}
