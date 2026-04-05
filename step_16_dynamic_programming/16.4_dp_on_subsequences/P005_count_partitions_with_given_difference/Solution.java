import java.util.*;

/**
 * Problem: Count Partitions with Given Difference
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Recursion
    // Time: O(2^n)  |  Space: O(n) recursion stack
    // ============================================================
    public static int bruteForce(int[] arr, int d) {
        int total = 0;
        for (int x : arr) total += x;
        if ((total + d) % 2 != 0 || total + d < 0) return 0;
        int target = (total + d) / 2;
        return solveBrute(arr, arr.length - 1, target);
    }

    private static int solveBrute(int[] arr, int idx, int rem) {
        if (idx == 0) {
            if (rem == 0 && arr[0] == 0) return 2;
            if (rem == 0 || rem == arr[0]) return 1;
            return 0;
        }
        int ways = solveBrute(arr, idx - 1, rem);
        if (arr[idx] <= rem) {
            ways += solveBrute(arr, idx - 1, rem - arr[idx]);
        }
        return ways;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- DP Tabulation
    // Time: O(n * target)  |  Space: O(n * target)
    // ============================================================
    public static int optimal(int[] arr, int d) {
        int n = arr.length;
        int total = 0;
        for (int x : arr) total += x;
        if ((total + d) % 2 != 0 || total + d < 0) return 0;
        int target = (total + d) / 2;

        int[][] dp = new int[n][target + 1];

        if (arr[0] == 0) {
            dp[0][0] = 2;
        } else {
            dp[0][0] = 1;
            if (arr[0] <= target) dp[0][arr[0]] = 1;
        }

        for (int i = 1; i < n; i++) {
            for (int s = 0; s <= target; s++) {
                int notPick = dp[i - 1][s];
                int pick = (arr[i] <= s) ? dp[i - 1][s - arr[i]] : 0;
                dp[i][s] = notPick + pick;
            }
        }

        return dp[n - 1][target];
    }

    // ============================================================
    // APPROACH 3: BEST -- Space-Optimized 1D DP
    // Time: O(n * target)  |  Space: O(target)
    // ============================================================
    public static int best(int[] arr, int d) {
        int n = arr.length;
        int total = 0;
        for (int x : arr) total += x;
        if ((total + d) % 2 != 0 || total + d < 0) return 0;
        int target = (total + d) / 2;

        int[] prev = new int[target + 1];

        if (arr[0] == 0) {
            prev[0] = 2;
        } else {
            prev[0] = 1;
            if (arr[0] <= target) prev[arr[0]] = 1;
        }

        for (int i = 1; i < n; i++) {
            int[] curr = new int[target + 1];
            for (int s = 0; s <= target; s++) {
                int notPick = prev[s];
                int pick = (arr[i] <= s) ? prev[s - arr[i]] : 0;
                curr[s] = notPick + pick;
            }
            prev = curr;
        }

        return prev[target];
    }

    public static void main(String[] args) {
        System.out.println("=== Count Partitions with Given Difference ===\n");

        System.out.println("Brute:   " + bruteForce(new int[]{5, 2, 6, 4}, 3));  // 1
        System.out.println("Optimal: " + optimal(new int[]{5, 2, 6, 4}, 3));      // 1
        System.out.println("Best:    " + best(new int[]{5, 2, 6, 4}, 3));         // 1

        System.out.println("\nBrute:   " + bruteForce(new int[]{1, 1, 1, 1}, 0)); // 6
        System.out.println("Optimal: " + optimal(new int[]{1, 1, 1, 1}, 0));      // 6
        System.out.println("Best:    " + best(new int[]{1, 1, 1, 1}, 0));         // 6
    }
}
