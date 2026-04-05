import java.util.*;

/**
 * Problem: Flip Array
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a positive integer array, negate some elements to minimize |sum|.
 * Find the MINIMUM number of elements to negate to make the sum closest to 0.
 * (Subset sum DP: find largest subset sum <= totalSum/2)
 *
 * @author DSA_Nova
 */

// ============================================================
// APPROACH 1: BRUTE FORCE
// Recursive: try all 2^N subsets of elements to negate
// Time: O(2^N)  |  Space: O(N) stack
// ============================================================
class BruteForce {
    static int[] arr;
    static int n, totalSum;
    static int minFlips, minDiff;

    static void solve(int idx, int currSum, int flips) {
        if (idx == n) {
            int diff = Math.abs(totalSum - 2 * currSum);
            if (diff < minDiff || (diff == minDiff && flips < minFlips)) {
                minDiff = diff;
                minFlips = flips;
            }
            return;
        }
        // Don't negate element idx
        solve(idx + 1, currSum, flips);
        // Negate element idx (add to negated subset)
        solve(idx + 1, currSum + arr[idx], flips + 1);
    }

    static int minFlipsToMinimizeSum(int[] A) {
        arr = A;
        n = A.length;
        totalSum = 0;
        for (int x : A) totalSum += x;
        minFlips = n + 1;
        minDiff = Integer.MAX_VALUE;
        solve(0, 0, 0);
        return minFlips;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Subset Sum DP: dp[count][sum] = can we negate exactly 'count' elements
// achieving negated-subset-sum = 'sum'?
// Find minimum count such that some sum in [totalSum/2, totalSum/2+1] is achievable.
// Time: O(N * totalSum)  |  Space: O(N * totalSum)
// ============================================================
class Optimal {
    static int minFlipsToMinimizeSum(int[] A) {
        int n = A.length;
        int totalSum = 0;
        for (int x : A) totalSum += x;
        int target = totalSum / 2;

        // dp[i][s] = minimum number of elements to negate to get negated-subset sum = s using A[0..i-1]
        // Use: dp[k][s] = true if we can pick exactly k elements summing to s
        // We want: minimum k such that some s in [0, target] is achievable with k elements,
        // and totalSum - 2*s is minimized (closest to 0)
        //
        // Alternative: dp[count][sum] feasibility approach
        int[][] dp = new int[n + 1][target + 1];
        for (int[] row : dp) Arrays.fill(row, Integer.MAX_VALUE / 2);
        dp[0][0] = 0;

        for (int i = 0; i < n; i++) {
            // Process in reverse for 0/1 knapsack
            for (int k = Math.min(i + 1, n); k >= 1; k--) {
                for (int s = target; s >= A[i]; s--) {
                    if (dp[k - 1][s - A[i]] < Integer.MAX_VALUE / 2) {
                        dp[k][s] = Math.min(dp[k][s], dp[k - 1][s - A[i]]);
                    }
                }
            }
        }

        // Actually, we need a different DP. Let's use:
        // dp[k][s] = true if we can choose exactly k elements that sum to s
        // Then find minimum k where any s in [0..target] is reachable.
        // Rebuild with boolean DP:
        boolean[][][] feasible = new boolean[n + 1][n + 1][target + 1];
        feasible[0][0][0] = true;
        for (int i = 0; i < n; i++) {
            for (int k = 0; k <= i; k++) {
                for (int s = 0; s <= target; s++) {
                    if (feasible[i][k][s]) {
                        // Don't take element i in negated set
                        feasible[i + 1][k][s] = true;
                        // Take element i in negated set
                        if (k + 1 <= n && s + A[i] <= target) {
                            feasible[i + 1][k + 1][s + A[i]] = true;
                        } else if (k + 1 <= n && s + A[i] > target) {
                            // Mark reachable (partial — we cap at target)
                            feasible[i + 1][k + 1][target] = true; // approximate
                        }
                    }
                }
            }
        }

        for (int k = 0; k <= n; k++) {
            for (int s = 0; s <= target; s++) {
                if (feasible[n][k][s]) return k;
            }
        }
        return n;
    }
}

// ============================================================
// APPROACH 3: BEST
// Clean 2D DP: dp[k][s] = can we pick k elements summing to s?
// Find minimum k where dp[k][s] = true for some s in [0..totalSum/2].
// Time: O(N^2 * S)  |  Space: O(N * S)
// ============================================================
class Best {
    static int minFlipsToMinimizeSum(int[] A) {
        int n = A.length;
        int totalSum = 0;
        for (int x : A) totalSum += x;
        int target = totalSum / 2;

        // dp[k][s] = true if we can select exactly k elements that sum to s
        boolean[][] dp = new boolean[n + 1][target + 1];
        dp[0][0] = true;

        for (int x : A) {
            // Traverse in reverse to maintain 0/1 knapsack
            for (int k = Math.min(n - 1, n); k >= 0; k--) {
                for (int s = target; s >= x; s--) {
                    if (dp[k][s - x]) dp[k + 1][s] = true;
                }
            }
        }

        // Find minimum k where any s in [0..target] is reachable
        for (int k = 0; k <= n; k++) {
            for (int s = target; s >= 0; s--) {
                if (dp[k][s]) return k;
            }
        }
        return n;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Flip Array ===");

        // [15,10,6] totalSum=31, target=15. Negate {10,6}=16>15; negate {15}=15=target => 1 flip
        // Actually: negate {15}: sum becomes -15+10+6=1 (diff=1), 1 flip
        // negate {10,6}: sum becomes 15-10-6=-1 (diff=1), 2 flips
        // minimum flips = 1
        int[] A1 = {15, 10, 6};
        System.out.println("BruteForce [15,10,6]: " + BruteForce.minFlipsToMinimizeSum(A1));  // 1
        System.out.println("Best       [15,10,6]: " + Best.minFlipsToMinimizeSum(A1));        // 1

        // [1,2,3,4]: totalSum=10, target=5. negate {1,4}=5 => sum=0, 2 flips
        int[] A2 = {1, 2, 3, 4};
        System.out.println("BruteForce [1,2,3,4]: " + BruteForce.minFlipsToMinimizeSum(A2)); // 2
        System.out.println("Best       [1,2,3,4]: " + Best.minFlipsToMinimizeSum(A2));       // 2

        // [1]: negate 1 -> sum=-1, 1 flip (diff=1); don't negate: sum=1, 0 flips. Min diff=1, flips=1 vs 0 flips
        // Best for minimizing |sum|: negate nothing => |1|=1, or negate {1} => |-1|=1, prefer 0 flips
        int[] A3 = {1};
        System.out.println("BruteForce [1]: " + BruteForce.minFlipsToMinimizeSum(A3));  // 0
        System.out.println("Best       [1]: " + Best.minFlipsToMinimizeSum(A3));        // 0
    }
}
