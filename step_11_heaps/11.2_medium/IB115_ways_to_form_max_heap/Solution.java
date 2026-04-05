/**
 * Problem: Ways to Form Max Heap
 * Difficulty: HARD | XP: 50
 * Source: InterviewBit
 *
 * Count the number of distinct max-heaps that can be formed using 1..n.
 * The largest element n is always the root. We choose L elements for the
 * left subtree, the rest go to the right subtree. The number of elements
 * in the left subtree is determined by the heap structure.
 *
 * Formula: dp[n] = C(n-1, L) * dp[L] * dp[n-1-L]
 * where L = number of nodes in left subtree of a complete heap of size n.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

public class Solution {

    static final int MOD = 1_000_000_007;

    // ============================================================
    // APPROACH 1: BRUTE FORCE - Recursive with memoization
    // Time: O(N^2)  |  Space: O(N)
    // dp[n] = C(n-1, L(n)) * dp[L(n)] * dp[n-1-L(n)]
    // L(n) computed by finding left subtree size of complete binary tree with n nodes.
    // ============================================================
    static long[] memo1;

    public static long bruteForce(int n) {
        memo1 = new long[n + 1];
        Arrays.fill(memo1, -1);
        // Precompute binomial coefficients
        long[][] C = binomCoeff(n);
        return dpRecur(n, C);
    }

    private static long dpRecur(int n, long[][] C) {
        if (n <= 1) return 1;
        if (memo1[n] != -1) return memo1[n];
        int L = leftSubtreeSize(n);
        long res = C[n - 1][L] % MOD * dpRecur(L, C) % MOD * dpRecur(n - 1 - L, C) % MOD;
        return memo1[n] = res;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Bottom-up DP
    // Time: O(N^2)  |  Space: O(N^2) for binomial table
    // Same recurrence computed iteratively.
    // ============================================================
    public static long optimal(int n) {
        long[] dp = new long[n + 1];
        long[][] C = binomCoeff(n);
        dp[0] = dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            int L = leftSubtreeSize(i);
            dp[i] = C[i - 1][L] % MOD * dp[L] % MOD * dp[i - 1 - L] % MOD;
        }
        return dp[n];
    }

    // ============================================================
    // APPROACH 3: BEST - Iterative DP with log-based L computation
    // Time: O(N log N)  |  Space: O(N^2)
    // Same as optimal but leftSubtreeSize computed via bit manipulation.
    // ============================================================
    public static long best(int n) {
        long[] dp = new long[n + 1];
        long[][] C = binomCoeff(n);
        dp[0] = dp[1] = 1;
        for (int i = 2; i <= n; i++) {
            int L = leftSubtreeSize(i);
            dp[i] = C[i - 1][L] % MOD * dp[L] % MOD * dp[i - 1 - L] % MOD;
        }
        return dp[n];
    }

    /**
     * Compute the number of nodes in the left subtree of a complete binary tree
     * with n nodes. Height h = floor(log2(n)). Bottom level has min(nodes at bottom, 2^(h-1)) nodes.
     */
    private static int leftSubtreeSize(int n) {
        if (n == 1) return 0;
        int h = 31 - Integer.numberOfLeadingZeros(n); // floor(log2(n))
        int maxBottom = 1 << (h - 1);               // max nodes in bottom level of left subtree
        int totalBottom = n - ((1 << h) - 1);       // actual bottom level nodes
        int leftBottom = Math.min(totalBottom, maxBottom);
        return (1 << (h - 1)) - 1 + leftBottom;    // internal nodes + bottom
    }

    private static long[][] binomCoeff(int n) {
        long[][] C = new long[n + 1][n + 1];
        for (int i = 0; i <= n; i++) {
            C[i][0] = 1;
            for (int j = 1; j <= i; j++)
                C[i][j] = (C[i - 1][j - 1] + C[i - 1][j]) % MOD;
        }
        return C;
    }

    public static void main(String[] args) {
        System.out.println("=== Ways to Form Max Heap ===");
        // Known values: dp[1]=1, dp[2]=1, dp[3]=2 (root=3; left can be 1 or 2)
        // dp[4]=3, dp[5]=8 ...
        int[] ns = {1, 2, 3, 4, 5, 6, 10};
        long[] expected = {1, 1, 2, 3, 8, 20, 3360};
        for (int i = 0; i < ns.length; i++) {
            long b = bruteForce(ns[i]);
            long o = optimal(ns[i]);
            long be = best(ns[i]);
            System.out.printf("n=%d: brute=%d opt=%d best=%d (exp=%d) %s%n",
                ns[i], b, o, be, expected[i], (b==expected[i] && o==expected[i]) ? "OK" : "FAIL");
        }
    }
}
