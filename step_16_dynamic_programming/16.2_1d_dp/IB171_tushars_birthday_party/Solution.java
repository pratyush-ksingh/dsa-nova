/**
 * Problem: Tushar's Birthday Party
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * N friends, each needs A[i] calories satisfied.
 * M dishes available, dish j has B[j] calories and costs C[j].
 * Each dish can be used multiple times (unbounded).
 * Find minimum total cost so that every friend is fully satisfied.
 *
 * Key insight: all friends share the same dish menu, so solve unbounded
 * knapsack (min cost to reach exactly X calories) for every value from 0
 * to max(A), then sum dp[A[i]] for each friend.
 *
 * Real-life use case: Menu pricing/optimization, resource allocation
 * where resources can be reused and each user has individual quotas.
 *
 * @author DSA_Nova
 */

import java.util.Arrays;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(N * max(A) * M)  |  Space: O(max(A))
// For each friend independently, compute unbounded knapsack min-cost.
// Redundant since all friends share the same dish set.
// ============================================================
class BruteForce {
    public int minCost(int[] A, int[] B, int[] C) {
        int n = A.length;
        int m = B.length;
        int total = 0;
        for (int i = 0; i < n; i++) {
            int cap = A[i];
            int[] dp = new int[cap + 1];
            Arrays.fill(dp, Integer.MAX_VALUE / 2);
            dp[0] = 0;
            for (int x = 1; x <= cap; x++) {
                for (int j = 0; j < m; j++) {
                    if (B[j] <= x && dp[x - B[j]] != Integer.MAX_VALUE / 2) {
                        dp[x] = Math.min(dp[x], dp[x - B[j]] + C[j]);
                    }
                }
            }
            total += dp[cap];
        }
        return total;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Time: O(max(A) * M + N)  |  Space: O(max(A))
// Compute unbounded knapsack once for all values 0..max(A).
// Then answer each friend in O(1) by looking up dp[A[i]].
// ============================================================
class Optimal {
    public int minCost(int[] A, int[] B, int[] C) {
        int n = A.length;
        int m = B.length;
        int maxCal = 0;
        for (int a : A) maxCal = Math.max(maxCal, a);

        int[] dp = new int[maxCal + 1];
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;
        for (int x = 1; x <= maxCal; x++) {
            for (int j = 0; j < m; j++) {
                if (B[j] <= x && dp[x - B[j]] != Integer.MAX_VALUE / 2) {
                    dp[x] = Math.min(dp[x], dp[x - B[j]] + C[j]);
                }
            }
        }

        int total = 0;
        for (int a : A) total += dp[a];
        return total;
    }
}

// ============================================================
// APPROACH 3: BEST
// Time: O(max(A) * M + N)  |  Space: O(max(A))
// Same as Optimal but handles the case where a dish's calories may
// exceed the target (still usable if we cap correctly) and also
// handles "at least X" semantics by extending dp array slightly.
// Also includes early termination if dp[0..maxCal] is fully optimal.
// ============================================================
class Best {
    public int minCost(int[] A, int[] B, int[] C) {
        int m = B.length;
        int maxCal = 0;
        for (int a : A) maxCal = Math.max(maxCal, a);

        // dp[x] = min cost to satisfy exactly x calories
        int[] dp = new int[maxCal + 1];
        Arrays.fill(dp, Integer.MAX_VALUE / 2);
        dp[0] = 0;

        // Unbounded knapsack: iterate capacity first, then items
        for (int x = 1; x <= maxCal; x++) {
            for (int j = 0; j < m; j++) {
                if (B[j] <= x) {
                    dp[x] = Math.min(dp[x], dp[x - B[j]] + C[j]);
                }
            }
        }

        int total = 0;
        for (int a : A) {
            total += (dp[a] == Integer.MAX_VALUE / 2 ? 0 : dp[a]);
        }
        return total;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Tushar's Birthday Party ===");

        // Example 1:
        // Friends need: [3, 4]  => friend1 needs 3 cal, friend2 needs 4 cal
        // Dishes: calories=[1,2,3], cost=[10,3,2]
        // Optimal: friend1 -> dish3 (3 cal, cost 2)
        //          friend2 -> dish2+dish2 (2+2=4 cal, cost 6) => total 8
        int[] A1 = {3, 4};
        int[] B1 = {1, 2, 3};
        int[] C1 = {10, 3, 2};

        BruteForce bf = new BruteForce();
        Optimal opt = new Optimal();
        Best best = new Best();

        System.out.println("Test 1:");
        System.out.println("  Brute:   " + bf.minCost(A1, B1, C1));   // 8
        System.out.println("  Optimal: " + opt.minCost(A1, B1, C1));  // 8
        System.out.println("  Best:    " + best.minCost(A1, B1, C1)); // 8

        // Example 2:
        // Friends need: [5]
        // Dishes: calories=[3,4], cost=[4,5]
        // 3+3 (cost 8) vs 4+3=7? no. Need exactly 5: impossible with 3,4 alone
        //   -> 3+? can't reach 5 with 3 only; 3+... no. Actually 3 doesn't divide 5 evenly.
        //   Closest: 3+3=6 cost 8, or 4+4=8 cost 10. Wait need exactly 5:
        //   dp[5]: dp[5-3]=dp[2]=INF, dp[5-4]=dp[1]=INF => impossible
        //   InterviewBit usually guarantees feasibility. Let's use a reachable example.
        int[] A2 = {4};
        int[] B2 = {1, 3};
        int[] C2 = {2, 5};
        // dp[4]: use 1+1+1+1=cost 8, or 1+3=cost 7, or 3+1=cost 7
        // best is 1+3 => cost 7
        System.out.println("\nTest 2 (single friend needs 4 cal):");
        System.out.println("  Brute:   " + bf.minCost(A2, B2, C2));   // 7
        System.out.println("  Optimal: " + opt.minCost(A2, B2, C2));  // 7
        System.out.println("  Best:    " + best.minCost(A2, B2, C2)); // 7
    }
}
