/**
 * Problem: Subset Sum Equal to Target
 * Difficulty: MEDIUM | XP: 25
 *
 * Given array and target, check if any subset sums to target.
 * All 4 DP approaches: Recursion -> Memo -> Tab -> Space Optimized
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(2^n) | Space: O(n)
// ============================================================
class RecursiveSubsetSum {
    public boolean subsetSum(int[] arr, int target) {
        return solve(arr.length - 1, target, arr);
    }

    private boolean solve(int i, int target, int[] arr) {
        if (target == 0) return true;
        if (i == 0) return arr[0] == target;

        // Exclude arr[i]
        boolean notTake = solve(i - 1, target, arr);

        // Include arr[i] (only if it doesn't exceed target)
        boolean take = false;
        if (arr[i] <= target) {
            take = solve(i - 1, target - arr[i], arr);
        }

        return notTake || take;
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n * target) | Space: O(n * target)
// ============================================================
class MemoSubsetSum {
    public boolean subsetSum(int[] arr, int target) {
        int n = arr.length;
        // 0 = unvisited, 1 = true, 2 = false
        int[][] dp = new int[n][target + 1];
        return solve(n - 1, target, arr, dp);
    }

    private boolean solve(int i, int target, int[] arr, int[][] dp) {
        if (target == 0) return true;
        if (i == 0) return arr[0] == target;
        if (dp[i][target] != 0) return dp[i][target] == 1;

        boolean notTake = solve(i - 1, target, arr, dp);
        boolean take = false;
        if (arr[i] <= target) {
            take = solve(i - 1, target - arr[i], arr, dp);
        }

        dp[i][target] = (notTake || take) ? 1 : 2;
        return notTake || take;
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(n * target) | Space: O(n * target)
// ============================================================
class TabSubsetSum {
    public boolean subsetSum(int[] arr, int target) {
        int n = arr.length;
        boolean[][] dp = new boolean[n][target + 1];

        // Base case: sum = 0 is always achievable (empty subset)
        for (int i = 0; i < n; i++) {
            dp[i][0] = true;
        }

        // Base case: first element
        if (arr[0] <= target) {
            dp[0][arr[0]] = true;
        }

        // Fill table
        for (int i = 1; i < n; i++) {
            for (int s = 1; s <= target; s++) {
                boolean notTake = dp[i - 1][s];
                boolean take = false;
                if (arr[i] <= s) {
                    take = dp[i - 1][s - arr[i]];
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
class SpaceSubsetSum {
    public boolean subsetSum(int[] arr, int target) {
        int n = arr.length;
        boolean[] prev = new boolean[target + 1];

        // Base case
        prev[0] = true;
        if (arr[0] <= target) {
            prev[arr[0]] = true;
        }

        for (int i = 1; i < n; i++) {
            boolean[] curr = new boolean[target + 1];
            curr[0] = true;  // empty subset
            for (int s = 1; s <= target; s++) {
                boolean notTake = prev[s];
                boolean take = false;
                if (arr[i] <= s) {
                    take = prev[s - arr[i]];
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
        System.out.println("=== Subset Sum Equal to Target ===\n");

        RecursiveSubsetSum rec = new RecursiveSubsetSum();
        MemoSubsetSum memo = new MemoSubsetSum();
        TabSubsetSum tab = new TabSubsetSum();
        SpaceSubsetSum space = new SpaceSubsetSum();

        int[][] arrays = {
            {1, 2, 3, 4},
            {2, 3, 7, 8, 10},
            {1, 2, 3},
            {6, 1, 2, 1},
            {5},
            {5},
            {0, 1, 2},
        };
        int[] targets = {4, 11, 7, 4, 5, 3, 3};
        boolean[] expected = {true, true, false, true, true, false, true};

        for (int t = 0; t < arrays.length; t++) {
            boolean r = rec.subsetSum(arrays[t], targets[t]);
            boolean m = memo.subsetSum(arrays[t], targets[t]);
            boolean tb = tab.subsetSum(arrays[t], targets[t]);
            boolean s = space.subsetSum(arrays[t], targets[t]);

            boolean pass = r == expected[t] && m == expected[t]
                    && tb == expected[t] && s == expected[t];

            System.out.println("arr=" + Arrays.toString(arrays[t]) + ", target=" + targets[t]);
            System.out.println("  Rec=" + r + " | Memo=" + m + " | Tab=" + tb + " | Space=" + s);
            System.out.println("  Expected=" + expected[t] + " | Pass=" + pass + "\n");
        }
    }
}
