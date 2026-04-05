/**
 * Problem: Longest Increasing Subsequence -- Binary Search
 * Difficulty: MEDIUM | XP: 25
 *
 * O(n log n) LIS using patience sorting / tails array.
 * Shows all 4 approaches: Recursion, Memo, Tabulation O(n^2), Tails O(n log n).
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Plain Recursion (Take / Not Take)
// Time: O(2^n) | Space: O(n) stack
// ============================================================
class Recursive {
    public int lis(int[] nums) {
        return solve(nums, 0, -1);
    }

    private int solve(int[] nums, int idx, int prevIdx) {
        if (idx == nums.length) return 0;

        // Option 1: don't take nums[idx]
        int notTake = solve(nums, idx + 1, prevIdx);

        // Option 2: take nums[idx] if it's greater than previous
        int take = 0;
        if (prevIdx == -1 || nums[idx] > nums[prevIdx]) {
            take = 1 + solve(nums, idx + 1, idx);
        }

        return Math.max(take, notTake);
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n^2) | Space: O(n^2)
// ============================================================
class Memoization {
    public int lis(int[] nums) {
        int n = nums.length;
        int[][] dp = new int[n][n + 1];
        for (int[] row : dp) Arrays.fill(row, -1);
        return solve(nums, 0, -1, dp);
    }

    private int solve(int[] nums, int idx, int prevIdx, int[][] dp) {
        if (idx == nums.length) return 0;
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
// Approach 3: Tabulation -- Classic O(n^2) DP
// Time: O(n^2) | Space: O(n)
// ============================================================
class Tabulation {
    public int lis(int[] nums) {
        int n = nums.length;
        int[] dp = new int[n];
        Arrays.fill(dp, 1); // each element is a subsequence of length 1

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
// Approach 4: Tails Array + Binary Search (Patience Sorting)
// Time: O(n log n) | Space: O(n)
// ============================================================
class BinarySearchLIS {
    public int lis(int[] nums) {
        // tails[i] = smallest tail element for increasing subsequence of length i+1
        List<Integer> tails = new ArrayList<>();

        for (int num : nums) {
            int pos = lowerBound(tails, num);
            if (pos == tails.size()) {
                tails.add(num); // extends LIS
            } else {
                tails.set(pos, num); // replace with smaller tail
            }
        }
        return tails.size();
    }

    /**
     * Find leftmost position where tails[pos] >= target.
     * This is the standard lower_bound / bisect_left operation.
     */
    private int lowerBound(List<Integer> tails, int target) {
        int lo = 0, hi = tails.size() - 1;
        int result = tails.size(); // default: append position

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (tails.get(mid) >= target) {
                result = mid;
                hi = mid - 1;
            } else {
                lo = mid + 1;
            }
        }
        return result;
    }

    /**
     * Variant: also reconstruct the actual LIS.
     * Uses an auxiliary array to track which element replaced which position.
     */
    public List<Integer> lisWithReconstruction(int[] nums) {
        int n = nums.length;
        List<Integer> tails = new ArrayList<>();
        int[] tailIndices = new int[n]; // tailIndices[i] = index in tails where nums[i] was placed
        int[] parent = new int[n];
        Arrays.fill(parent, -1);

        // predecessorAtPos[pos] = index of element before this position's element
        int[] posHolder = new int[n]; // which nums index is at each tails position

        for (int i = 0; i < n; i++) {
            int pos = lowerBound(tails, nums[i]);
            if (pos == tails.size()) {
                tails.add(nums[i]);
            } else {
                tails.set(pos, nums[i]);
            }
            posHolder[pos] = i;
            tailIndices[i] = pos;
            if (pos > 0) {
                parent[i] = posHolder[pos - 1];
            }
        }

        // Backtrack from the last element in the LIS
        List<Integer> result = new ArrayList<>();
        int idx = posHolder[tails.size() - 1];
        while (idx != -1) {
            result.add(nums[idx]);
            idx = parent[idx];
        }
        Collections.reverse(result);
        return result;
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Longest Increasing Subsequence (Binary Search) ===\n");

        Recursive rec = new Recursive();
        Memoization memo = new Memoization();
        Tabulation tab = new Tabulation();
        BinarySearchLIS bs = new BinarySearchLIS();

        int[][] tests = {
            {10, 9, 2, 5, 3, 7, 101, 18},
            {0, 1, 0, 3, 2, 3},
            {7, 7, 7, 7, 7},
            {1, 2, 3, 4, 5},
            {5, 4, 3, 2, 1},
            {3, 1, 4, 1, 5, 9, 2, 6},
            {1},
        };
        int[] expected = {4, 4, 1, 5, 1, 5, 1};

        System.out.printf("%-35s %-6s %-6s %-6s %-6s %-6s %-6s%n",
                "nums", "Rec", "Memo", "Tab", "BS", "Exp", "Pass");
        System.out.println("-".repeat(80));

        for (int t = 0; t < tests.length; t++) {
            int r = rec.lis(tests[t]);
            int m = memo.lis(tests[t]);
            int tb = tab.lis(tests[t]);
            int b = bs.lis(tests[t]);

            boolean pass = (r == expected[t]) && (m == expected[t]) &&
                           (tb == expected[t]) && (b == expected[t]);

            System.out.printf("%-35s %-6d %-6d %-6d %-6d %-6d %-6s%n",
                    Arrays.toString(tests[t]), r, m, tb, b, expected[t],
                    pass ? "PASS" : "FAIL");
        }

        // Show tails array evolution
        System.out.println("\n--- Tails Array Evolution ---");
        int[] demo = {10, 9, 2, 5, 3, 7, 101, 18};
        List<Integer> tails = new ArrayList<>();
        for (int num : demo) {
            int pos = Collections.binarySearch(tails, num);
            if (pos < 0) pos = -(pos + 1);
            if (pos == tails.size()) tails.add(num);
            else tails.set(pos, num);
            System.out.printf("  Process %3d -> tails = %s%n", num, tails);
        }
        System.out.println("LIS length = " + tails.size());

        // Reconstruct actual LIS
        System.out.println("\n--- LIS Reconstruction ---");
        List<Integer> actualLIS = bs.lisWithReconstruction(demo);
        System.out.println("One valid LIS: " + actualLIS);
    }
}
