import java.util.*;

/**
 * Problem: Merge Elements
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given an array, merge adjacent elements: cost = sum of two merged elements.
 * Find the minimum total cost to merge all elements into one.
 * This is the classic "Minimum Cost to Merge Stones" / Matrix Chain style interval DP.
 * Equivalent to Optimal Binary Search Tree / Burst Balloons variant.
 *
 * @author DSA_Nova
 */

// ============================================================
// APPROACH 1: BRUTE FORCE
// Recursive interval DP with memoization
// dp[i][j] = min cost to merge A[i..j] into one element
// Time: O(N^3)  |  Space: O(N^2)
// ============================================================
class BruteForce {
    static int[] A, prefix;
    static int[][] memo;

    static int dp(int i, int j) {
        if (i == j) return 0;
        if (memo[i][j] != -1) return memo[i][j];
        int cost = Integer.MAX_VALUE;
        int rangeSum = prefix[j + 1] - prefix[i];
        for (int k = i; k < j; k++) {
            cost = Math.min(cost, dp(i, k) + dp(k + 1, j) + rangeSum);
        }
        return memo[i][j] = cost;
    }

    static int minMergeCost(int[] arr) {
        A = arr;
        int n = arr.length;
        prefix = new int[n + 1];
        for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + arr[i];
        memo = new int[n][n];
        for (int[] row : memo) Arrays.fill(row, -1);
        return dp(0, n - 1);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Bottom-up interval DP
// Time: O(N^3)  |  Space: O(N^2)
// ============================================================
class Optimal {
    static int minMergeCost(int[] arr) {
        int n = arr.length;
        if (n <= 1) return 0;

        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + arr[i];

        int[][] dp = new int[n][n];
        // length 1: cost 0 (already done by default)

        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                int rangeSum = prefix[j + 1] - prefix[i];
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    dp[i][j] = Math.min(dp[i][j], dp[i][k] + dp[k + 1][j] + rangeSum);
                }
            }
        }
        return dp[0][n - 1];
    }
}

// ============================================================
// APPROACH 3: BEST
// Greedy: always merge the two smallest adjacent elements (like Huffman coding)
// Use a sorted structure / min-heap of adjacent pairs.
// NOTE: This greedy is NOT always optimal for arbitrary splits (O(N^3) DP is correct).
// Here we present it as an alternative O(N log N) heuristic for comparison.
// The TRULY optimal for all cases is the O(N^3) DP above.
// Time: O(N^2) naive simulation  |  Space: O(N)
// ============================================================
class Best {
    // Heap-based greedy simulation (optimal for some cases, heuristic for others)
    static int minMergeCostGreedy(int[] arr) {
        // Use a list; always merge smallest adjacent pair
        List<Integer> list = new ArrayList<>();
        for (int x : arr) list.add(x);
        int totalCost = 0;

        while (list.size() > 1) {
            // Find pair with minimum sum
            int minIdx = 0, minSum = list.get(0) + list.get(1);
            for (int i = 1; i < list.size() - 1; i++) {
                int s = list.get(i) + list.get(i + 1);
                if (s < minSum) { minSum = s; minIdx = i; }
            }
            totalCost += minSum;
            list.set(minIdx, minSum);
            list.remove(minIdx + 1);
        }
        return totalCost;
    }

    // Use correct DP as the "best" implementation
    static int minMergeCost(int[] arr) {
        return Optimal.minMergeCost(arr);
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Merge Elements ===");

        // [1,2,3] => merge(1,2)=3 cost 3, then merge(3,3)=6 cost 6 => total 9
        //          or merge(2,3)=5 cost 5, then merge(1,5)=6 cost 6 => total 11
        //          min = 9
        int[] A1 = {1, 2, 3};
        System.out.println("BruteForce [1,2,3]: " + BruteForce.minMergeCost(A1));  // 9
        System.out.println("Optimal    [1,2,3]: " + Optimal.minMergeCost(A1));     // 9
        System.out.println("Best       [1,2,3]: " + Best.minMergeCost(A1));        // 9

        // [4,3,2,6] => optimal DP
        int[] A2 = {4, 3, 2, 6};
        System.out.println("BruteForce [4,3,2,6]: " + BruteForce.minMergeCost(A2));  // 29
        System.out.println("Optimal    [4,3,2,6]: " + Optimal.minMergeCost(A2));     // 29

        int[] A3 = {10};
        System.out.println("Single: " + Optimal.minMergeCost(A3));  // 0
    }
}
