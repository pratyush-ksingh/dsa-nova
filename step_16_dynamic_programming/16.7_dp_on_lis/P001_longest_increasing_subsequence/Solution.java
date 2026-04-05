import java.util.*;

// ============================================================
// APPROACH 1: Recursion (Brute Force) -- Try all subsequences
// Time: O(2^n) | Space: O(n)
// ============================================================
class LIS_Recursion {
    public int lengthOfLIS(int[] nums) {
        return solve(nums, 0, -1);
    }

    // solve(idx, prevIdx) = LIS length from idx onwards, with prevIdx as last picked
    private int solve(int[] nums, int idx, int prevIdx) {
        // Base case: no more elements
        if (idx == nums.length) return 0;

        // Choice 1: Skip current element
        int notTake = solve(nums, idx + 1, prevIdx);

        // Choice 2: Take current element (only if it's greater than previous)
        int take = 0;
        if (prevIdx == -1 || nums[idx] > nums[prevIdx]) {
            take = 1 + solve(nums, idx + 1, idx);
        }

        return Math.max(take, notTake);
    }
}

// ============================================================
// APPROACH 2: Memoization (Top-Down DP)
// Time: O(n^2) | Space: O(n^2)
// ============================================================
class LIS_Memoization {
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        // dp[idx][prevIdx + 1] -- shift prevIdx by 1 since it can be -1
        int[][] dp = new int[n][n + 1];
        for (int[] row : dp) Arrays.fill(row, -1);
        return solve(nums, 0, -1, dp);
    }

    private int solve(int[] nums, int idx, int prevIdx, int[][] dp) {
        if (idx == nums.length) return 0;

        // Check cache (prevIdx shifted by +1)
        if (dp[idx][prevIdx + 1] != -1) return dp[idx][prevIdx + 1];

        int notTake = solve(nums, idx + 1, prevIdx, dp);

        int take = 0;
        if (prevIdx == -1 || nums[idx] > nums[prevIdx]) {
            take = 1 + solve(nums, idx + 1, idx, dp);
        }

        dp[idx][prevIdx + 1] = Math.max(take, notTake);
        return dp[idx][prevIdx + 1];
    }
}

// ============================================================
// APPROACH 3: Tabulation (Bottom-Up DP)
// Time: O(n^2) | Space: O(n)
// dp[i] = length of LIS ending at index i
// ============================================================
class LIS_Tabulation {
    public int lengthOfLIS(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // Every element alone is LIS of length 1

        int maxLen = 1;

        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = Math.max(dp[i], dp[j] + 1);
                }
            }
            maxLen = Math.max(maxLen, dp[i]);
        }

        return maxLen;
    }
}

// ============================================================
// APPROACH 4: Space-Optimized -- Binary Search (Patience Sorting)
// Time: O(n log n) | Space: O(n)
// Maintain tails[] where tails[i] = smallest tail of IS of length i+1
// ============================================================
class LIS_BinarySearch {
    public int lengthOfLIS(int[] nums) {
        // tails[i] = smallest ending element of all increasing subsequences of length i+1
        List<Integer> tails = new ArrayList<>();

        for (int num : nums) {
            // Binary search for leftmost position in tails >= num
            int lo = 0, hi = tails.size();
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (tails.get(mid) < num) {
                    lo = mid + 1;
                } else {
                    hi = mid;
                }
            }

            // If lo == tails.size(), num extends the longest subsequence
            if (lo == tails.size()) {
                tails.add(num);
            } else {
                // Replace to maintain smallest possible tail
                tails.set(lo, num);
            }
        }

        return tails.size();
    }
}

// ============================================================
// TEST HARNESS
// ============================================================
public class Solution {
    public static void main(String[] args) {
        int[][] testCases = {
            {10, 9, 2, 5, 3, 7, 101, 18},  // Expected: 4
            {0, 1, 0, 3, 2, 3},             // Expected: 4
            {7, 7, 7, 7, 7},                // Expected: 1
            {5},                             // Expected: 1
            {1, 2, 3, 4, 5},                // Expected: 5
            {5, 4, 3, 2, 1},                // Expected: 1
            {-2, -1, 0},                    // Expected: 3
            {3, 1, 4, 1, 5, 9, 2, 6},      // Expected: 5
        };
        int[] expected = {4, 4, 1, 1, 5, 1, 3, 5};

        LIS_Recursion sol1 = new LIS_Recursion();
        LIS_Memoization sol2 = new LIS_Memoization();
        LIS_Tabulation sol3 = new LIS_Tabulation();
        LIS_BinarySearch sol4 = new LIS_BinarySearch();

        System.out.println("=== Longest Increasing Subsequence ===\n");

        for (int i = 0; i < testCases.length; i++) {
            int[] tc = testCases[i];
            int exp = expected[i];

            int r1 = sol1.lengthOfLIS(tc);
            int r2 = sol2.lengthOfLIS(tc);
            int r3 = sol3.lengthOfLIS(tc);
            int r4 = sol4.lengthOfLIS(tc);

            boolean pass = (r1 == exp && r2 == exp && r3 == exp && r4 == exp);
            String status = pass ? "PASS" : "FAIL";

            System.out.printf("Test %d: %s | Input: %s | Expected: %d | Got: [%d, %d, %d, %d]%n",
                i + 1, status, Arrays.toString(tc), exp, r1, r2, r3, r4);

            if (!pass) {
                System.out.println("  ** MISMATCH DETECTED **");
            }
        }

        System.out.println("\nAll tests completed.");
    }
}
