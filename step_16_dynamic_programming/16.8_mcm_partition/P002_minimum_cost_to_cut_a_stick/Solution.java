/**
 * Problem: Minimum Cost to Cut a Stick  (LeetCode 1547)
 * Difficulty: HARD | XP: 50
 *
 * You have a stick of length n. You are given an array cuts[] of positions
 * to cut. The cost of a cut is the current length of the stick being cut.
 * You can make the cuts in any order. Return the minimum total cost.
 *
 * Key insight: Sort cuts, add 0 and n as endpoints. Define dp[i][j] = min
 * cost to make all cuts strictly between newCuts[i] and newCuts[j].
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE (Try all orderings — simulation)
    // Time: O(c! * c)  |  Space: O(c)
    // ============================================================
    static class BruteForce {
        /**
         * Try every permutation of cuts; simulate cost for each.
         * Only feasible for tiny inputs (c <= 8).
         */
        static int minCost;

        static void permute(int[] cuts, int start, int n) {
            if (start == cuts.length) {
                minCost = Math.min(minCost, simulate(cuts, n));
                return;
            }
            for (int i = start; i < cuts.length; i++) {
                swap(cuts, start, i);
                permute(cuts, start + 1, n);
                swap(cuts, start, i);
            }
        }

        static int simulate(int[] order, int n) {
            // Use a sorted list of segment endpoints
            java.util.TreeSet<Integer> segs = new java.util.TreeSet<>();
            segs.add(0);
            segs.add(n);
            int cost = 0;
            for (int pos : order) {
                int lo = segs.lower(pos);   // segment left endpoint
                int hi = segs.higher(pos);  // segment right endpoint
                cost += hi - lo;
                segs.add(pos);
            }
            return cost;
        }

        static void swap(int[] a, int i, int j) {
            int t = a[i]; a[i] = a[j]; a[j] = t;
        }

        static int minCostCuts(int n, int[] cuts) {
            minCost = Integer.MAX_VALUE;
            permute(cuts.clone(), 0, n);
            return minCost;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (Top-Down Interval DP / Memoization)
    // Time: O(c^3)  |  Space: O(c^2) memo + O(c) stack
    // ============================================================
    static class Optimal {
        /**
         * Add 0 and n as virtual endpoints, sort all positions.
         * dp(i, j) = min cost to make all cuts between newCuts[i] and newCuts[j].
         * For the first cut at position k: cost = (newCuts[j] - newCuts[i]) + dp(i,k) + dp(k,j).
         * Base case: j <= i+1 (no cut positions between them) -> 0.
         */
        static int[] nc;      // newCuts with 0 and n added
        static int[][] memo;

        static int dp(int i, int j) {
            if (j - i <= 1) return 0;
            if (memo[i][j] != -1) return memo[i][j];
            int best = Integer.MAX_VALUE;
            for (int k = i + 1; k < j; k++) {
                int cost = (nc[j] - nc[i]) + dp(i, k) + dp(k, j);
                best = Math.min(best, cost);
            }
            memo[i][j] = best;
            return best;
        }

        static int minCostCuts(int n, int[] cuts) {
            int m = cuts.length;
            nc = new int[m + 2];
            nc[0] = 0;
            nc[m + 1] = n;
            for (int i = 0; i < m; i++) nc[i + 1] = cuts[i];
            Arrays.sort(nc);
            int sz = nc.length;
            memo = new int[sz][sz];
            for (int[] row : memo) Arrays.fill(row, -1);
            return dp(0, sz - 1);
        }
    }

    // ============================================================
    // APPROACH 3: BEST (Bottom-Up Interval DP)
    // Time: O(c^3)  |  Space: O(c^2) — no recursion overhead
    // ============================================================
    static class Best {
        /**
         * Same recurrence as Approach 2, built iteratively by gap size.
         * dp[i][j] = min cost to make all cuts strictly between newCuts[i] and newCuts[j].
         * Fill the DP table by increasing gap = j - i from 2 upward.
         */
        static int minCostCuts(int n, int[] cuts) {
            int m = cuts.length;
            int[] nc = new int[m + 2];
            nc[0] = 0;
            nc[m + 1] = n;
            for (int i = 0; i < m; i++) nc[i + 1] = cuts[i];
            Arrays.sort(nc);
            int sz = nc.length;
            int[][] dp = new int[sz][sz];

            for (int gap = 2; gap < sz; gap++) {
                for (int i = 0; i + gap < sz; i++) {
                    int j = i + gap;
                    dp[i][j] = Integer.MAX_VALUE;
                    for (int k = i + 1; k < j; k++) {
                        int cost = (nc[j] - nc[i]) + dp[i][k] + dp[k][j];
                        dp[i][j] = Math.min(dp[i][j], cost);
                    }
                }
            }
            return dp[0][sz - 1];
        }
    }

    // ============================================================
    // MAIN — run all approaches on test cases
    // ============================================================
    public static void main(String[] args) {
        int[]   ns       = {7,  9,  10, 5};
        int[][] cutsList = {{1,3,4,5}, {5,6,1,4,2}, {2,4,7}, {3}};
        int[]   expected = {16, 22, 20, 5};

        System.out.println("=== Minimum Cost to Cut a Stick ===\n");
        for (int t = 0; t < ns.length; t++) {
            int n = ns[t];
            int[] cuts = cutsList[t];
            int b  = BruteForce.minCostCuts(n, cuts.clone());
            int o  = Optimal.minCostCuts(n, cuts.clone());
            int be = Best.minCostCuts(n, cuts.clone());
            String status = (b == o && o == be && be == expected[t]) ? "PASS" : "FAIL";
            System.out.printf("n=%d cuts=...  Brute=%d  Optimal=%d  Best=%d  Expected=%d  [%s]%n",
                              n, b, o, be, expected[t], status);
        }
    }
}
