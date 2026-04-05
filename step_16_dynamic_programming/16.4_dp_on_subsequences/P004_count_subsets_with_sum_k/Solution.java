/**
 * Problem: Count Subsets with Sum K
 * Difficulty: MEDIUM | XP: 25
 *
 * Count number of subsets that sum to K. Handle zeros carefully.
 * All 4 DP approaches.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(2^n) | Space: O(n) stack
// ============================================================
class Recursive {
    public int countSubsets(int[] arr, int k) {
        return solve(arr, 0, k);
    }

    private int solve(int[] arr, int idx, int target) {
        if (idx == arr.length) {
            return target == 0 ? 1 : 0;
        }

        // Not take
        int notTake = solve(arr, idx + 1, target);

        // Take (only if element <= remaining target)
        int take = 0;
        if (arr[idx] <= target) {
            take = solve(arr, idx + 1, target - arr[idx]);
        }

        return take + notTake;
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n * k) | Space: O(n * k)
// ============================================================
class Memoization {
    public int countSubsets(int[] arr, int k) {
        int n = arr.length;
        int[][] dp = new int[n][k + 1];
        for (int[] row : dp) Arrays.fill(row, -1);
        return solve(arr, 0, k, dp);
    }

    private int solve(int[] arr, int idx, int target, int[][] dp) {
        if (idx == arr.length) {
            return target == 0 ? 1 : 0;
        }
        if (dp[idx][target] != -1) return dp[idx][target];

        int notTake = solve(arr, idx + 1, target, dp);

        int take = 0;
        if (arr[idx] <= target) {
            take = solve(arr, idx + 1, target - arr[idx], dp);
        }

        dp[idx][target] = take + notTake;
        return dp[idx][target];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(n * k) | Space: O(n * k)
// ============================================================
class Tabulation {
    public int countSubsets(int[] arr, int k) {
        int n = arr.length;
        int[][] dp = new int[n + 1][k + 1];

        // Base case: empty subset sums to 0
        dp[0][0] = 1;

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= k; j++) {
                // Not take current element
                dp[i][j] = dp[i - 1][j];

                // Take current element (if it fits)
                if (arr[i - 1] <= j) {
                    dp[i][j] += dp[i - 1][j - arr[i - 1]];
                }
            }
        }

        return dp[n][k];
    }
}

// ============================================================
// Approach 4: Space Optimized (1D array, right-to-left)
// Time: O(n * k) | Space: O(k)
// ============================================================
class SpaceOptimized {
    public int countSubsets(int[] arr, int k) {
        int[] dp = new int[k + 1];
        dp[0] = 1; // empty subset sums to 0

        for (int num : arr) {
            if (num == 0) {
                // Zero: every existing count doubles (can include or exclude zero)
                // This is handled by: dp[j] += dp[j - 0] = dp[j] += dp[j] = dp[j] * 2
                // But we need to be careful with traversal direction.
                // Since j - 0 = j, going right-to-left would double correctly:
                for (int j = k; j >= 0; j--) {
                    dp[j] *= 2;
                }
            } else {
                // Standard 0/1 knapsack: right-to-left
                for (int j = k; j >= num; j--) {
                    dp[j] += dp[j - num];
                }
            }
        }

        return dp[k];
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Count Subsets with Sum K ===\n");

        Recursive rec = new Recursive();
        Memoization memo = new Memoization();
        Tabulation tab = new Tabulation();
        SpaceOptimized space = new SpaceOptimized();

        int[][] arrays = {
            {1, 2, 3},
            {1, 1, 1},
            {0, 1, 2, 3},
            {0, 0, 1},
            {5, 2, 3, 10, 6, 8},
            {0, 0, 0},
            {3},
        };
        int[] targets = {3, 2, 3, 1, 10, 0, 5};
        int[] expected = {2, 3, 4, 4, 3, 8, 0};

        System.out.printf("%-25s %-4s %-8s %-8s %-8s %-8s %-8s %-6s%n",
                "Array", "k", "Rec", "Memo", "Tab", "Space", "Expect", "Pass");
        System.out.println("-".repeat(85));

        for (int t = 0; t < arrays.length; t++) {
            int r = rec.countSubsets(arrays[t], targets[t]);
            int m = memo.countSubsets(arrays[t], targets[t]);
            int tb = tab.countSubsets(arrays[t], targets[t]);
            int s = space.countSubsets(arrays[t], targets[t]);

            boolean pass = (r == expected[t]) && (m == expected[t]) &&
                           (tb == expected[t]) && (s == expected[t]);

            System.out.printf("%-25s %-4d %-8d %-8d %-8d %-8d %-8d %-6s%n",
                    Arrays.toString(arrays[t]), targets[t], r, m, tb, s, expected[t],
                    pass ? "PASS" : "FAIL");
        }

        // Demonstrate zero handling
        System.out.println("\n--- Zero Handling Demo ---");
        System.out.println("arr = [0, 0, 1], k = 1");
        System.out.println("Subsets summing to 1:");
        System.out.println("  {1}         (no zeros)");
        System.out.println("  {0, 1}      (first zero)");
        System.out.println("  {0, 1}      (second zero)");
        System.out.println("  {0, 0, 1}   (both zeros)");
        System.out.println("Count = " + tab.countSubsets(new int[]{0, 0, 1}, 1) +
                " (2^2 = 4 multiplier from 2 zeros)");

        // DP table visualization
        System.out.println("\n--- DP Table: arr=[1,2,3], k=3 ---");
        int[] arr = {1, 2, 3};
        int k = 3;
        int[][] dp = new int[4][4];
        dp[0][0] = 1;
        for (int i = 1; i <= 3; i++) {
            for (int j = 0; j <= 3; j++) {
                dp[i][j] = dp[i - 1][j];
                if (arr[i - 1] <= j) dp[i][j] += dp[i - 1][j - arr[i - 1]];
            }
        }
        System.out.println("       j=0  j=1  j=2  j=3");
        String[] labels = {"i=0(-)", "i=1(1)", "i=2(2)", "i=3(3)"};
        for (int i = 0; i <= 3; i++) {
            System.out.printf("%-8s %2d   %2d   %2d   %2d%n",
                    labels[i], dp[i][0], dp[i][1], dp[i][2], dp[i][3]);
        }
    }
}
