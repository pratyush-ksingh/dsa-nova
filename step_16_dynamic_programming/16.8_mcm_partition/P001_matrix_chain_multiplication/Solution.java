/**
 * Problem: Matrix Chain Multiplication
 * Difficulty: HARD | XP: 50
 *
 * Given a chain of n matrices with dimensions represented by array arr[] where
 * matrix i has dimensions arr[i-1] x arr[i], find the minimum number of scalar
 * multiplications needed to compute the product of the entire chain.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE (Pure Recursion)
    // Time: O(2^n)  |  Space: O(n) recursion stack
    // ============================================================
    static class BruteForce {
        /**
         * Try every possible parenthesization recursively.
         * For n matrices there are Catalan(n-1) distinct parenthesizations.
         * At each split k, cost = left sub-chain + right sub-chain + outer multiply.
         */
        static int solve(int[] arr, int i, int j) {
            if (i == j) return 0;          // single matrix: no cost
            int minCost = Integer.MAX_VALUE;
            for (int k = i; k < j; k++) {
                int cost = solve(arr, i, k)
                         + solve(arr, k + 1, j)
                         + arr[i - 1] * arr[k] * arr[j];
                minCost = Math.min(minCost, cost);
            }
            return minCost;
        }

        static int matrixChain(int[] arr) {
            int n = arr.length - 1;        // number of matrices
            return solve(arr, 1, n);
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (Top-Down DP / Memoization)
    // Time: O(n^3)  |  Space: O(n^2) dp table + O(n) stack
    // ============================================================
    static class Optimal {
        /**
         * Cache results in a 2-D memo table to avoid recomputation.
         * dp[i][j] = min multiplications to compute matrices i..j.
         */
        static int[][] memo;

        static int solve(int[] arr, int i, int j) {
            if (i == j) return 0;
            if (memo[i][j] != -1) return memo[i][j];
            int best = Integer.MAX_VALUE;
            for (int k = i; k < j; k++) {
                int cost = solve(arr, i, k)
                         + solve(arr, k + 1, j)
                         + arr[i - 1] * arr[k] * arr[j];
                best = Math.min(best, cost);
            }
            memo[i][j] = best;
            return best;
        }

        static int matrixChain(int[] arr) {
            int n = arr.length - 1;
            memo = new int[n + 1][n + 1];
            for (int[] row : memo)
                java.util.Arrays.fill(row, -1);
            return solve(arr, 1, n);
        }
    }

    // ============================================================
    // APPROACH 3: BEST (Bottom-Up DP on chain length)
    // Time: O(n^3)  |  Space: O(n^2)  — no recursion overhead
    // ============================================================
    static class Best {
        /**
         * Iterative DP: fill the table by increasing sub-chain length.
         * dp[i][j] = minimum multiplications for matrices i..j (1-indexed).
         * Fill diagonals from length 2 up to n so all sub-problems are solved
         * before they are needed.
         */
        static int matrixChain(int[] arr) {
            int n = arr.length - 1;
            int[][] dp = new int[n + 1][n + 1];
            // dp[i][i] = 0 by default (single matrix)

            for (int len = 2; len <= n; len++) {            // chain length
                for (int i = 1; i <= n - len + 1; i++) {
                    int j = i + len - 1;
                    dp[i][j] = Integer.MAX_VALUE;
                    for (int k = i; k < j; k++) {
                        int cost = dp[i][k] + dp[k + 1][j]
                                 + arr[i - 1] * arr[k] * arr[j];
                        dp[i][j] = Math.min(dp[i][j], cost);
                    }
                }
            }
            return dp[1][n];
        }
    }

    // ============================================================
    // MAIN — run all approaches on test cases
    // ============================================================
    public static void main(String[] args) {
        int[][] tests   = {{10, 30, 5, 60}, {40, 20, 30, 10, 30}, {10, 20, 30}, {1, 2, 3, 4}};
        int[]   expected = {4500,              26000,                 6000,         18};

        System.out.println("=== Matrix Chain Multiplication ===\n");
        for (int t = 0; t < tests.length; t++) {
            int[] arr = tests[t];
            int b  = BruteForce.matrixChain(arr);
            int o  = Optimal.matrixChain(arr);
            int be = Best.matrixChain(arr);
            String status = (b == o && o == be && be == expected[t]) ? "PASS" : "FAIL";
            System.out.printf("arr=..., Brute=%d  Optimal=%d  Best=%d  Expected=%d  [%s]%n",
                              b, o, be, expected[t], status);
        }
    }
}
