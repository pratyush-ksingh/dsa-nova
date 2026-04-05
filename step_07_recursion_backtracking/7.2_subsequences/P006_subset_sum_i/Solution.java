/**
 * Problem: Subset Sum I
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an array of non-negative integers and a target sum,
 * check whether any subset sums to the target.
 * Real-life use: Budget allocation, partition problems, knapsack decisions.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Pure recursion: at every index, include or exclude the element.
    // Time: O(2^N)  |  Space: O(N) recursion stack
    // ============================================================
    static class BruteForce {
        public static boolean hasSubsetSum(int[] arr, int target) {
            return solve(arr, 0, target);
        }

        private static boolean solve(int[] arr, int idx, int remaining) {
            if (remaining == 0) return true;
            if (idx == arr.length || remaining < 0) return false;
            return solve(arr, idx + 1, remaining - arr[idx])
                || solve(arr, idx + 1, remaining);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Top-down DP (memoization). State = (index, remaining sum).
    // Time: O(N * target)  |  Space: O(N * target)
    // ============================================================
    static class Optimal {
        public static boolean hasSubsetSum(int[] arr, int target) {
            // memo: 0=unset, 1=true, -1=false
            int[][] memo = new int[arr.length][target + 1];
            return solve(arr, 0, target, memo);
        }

        private static boolean solve(int[] arr, int idx, int remaining, int[][] memo) {
            if (remaining == 0) return true;
            if (idx == arr.length || remaining < 0) return false;
            if (memo[idx][remaining] != 0) return memo[idx][remaining] == 1;
            boolean result = solve(arr, idx + 1, remaining - arr[idx], memo)
                          || solve(arr, idx + 1, remaining, memo);
            memo[idx][remaining] = result ? 1 : -1;
            return result;
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Bottom-up DP (space-optimized 1D array).
    // dp[s] = true if any subset of elements seen so far sums to s.
    // Traverse sum right-to-left to prevent reuse of same element.
    // Time: O(N * target)  |  Space: O(target)
    // ============================================================
    static class Best {
        public static boolean hasSubsetSum(int[] arr, int target) {
            boolean[] dp = new boolean[target + 1];
            dp[0] = true;
            for (int num : arr) {
                for (int s = target; s >= num; s--) {
                    dp[s] = dp[s] || dp[s - num];
                }
            }
            return dp[target];
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Subset Sum I ===");

        int[][] arrs = {{3, 34, 4, 12, 5, 2}, {3, 34, 4, 12, 5, 2}, {1, 2, 3}};
        int[] targets = {9, 30, 7};

        for (int i = 0; i < arrs.length; i++) {
            int t = targets[i];
            System.out.printf("%narr=%s  target=%d%n", Arrays.toString(arrs[i]), t);
            System.out.printf("  Brute  : %b%n", BruteForce.hasSubsetSum(arrs[i], t));
            System.out.printf("  Optimal: %b%n", Optimal.hasSubsetSum(arrs[i], t));
            System.out.printf("  Best   : %b%n", Best.hasSubsetSum(arrs[i], t));
        }

        System.out.println("\nEdge: empty arr target=0 -> " + Best.hasSubsetSum(new int[]{}, 0));
        System.out.println("Edge: [5] target=5      -> " + Best.hasSubsetSum(new int[]{5}, 5));
        System.out.println("Edge: [5] target=3      -> " + Best.hasSubsetSum(new int[]{5}, 3));
    }
}
