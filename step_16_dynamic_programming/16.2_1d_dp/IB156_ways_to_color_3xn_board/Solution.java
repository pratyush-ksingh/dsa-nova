import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Enumerate all valid column patterns
// Time: O(n * P^2) where P = valid column patterns (~12)
// Space: O(P)
// ============================================================
// A 3xN board with 4 colors. No two adjacent cells (horizontal
// or vertical) can share a color. Process column by column.
// A column is a triple (c0,c1,c2) where c0!=c1, c1!=c2.
// Two adjacent columns are compatible if no row has same color.
// ============================================================

class BruteForce {
    static final int MOD = 1_000_000_007;

    public int solve(int n) {
        // Generate all valid single-column patterns: (c0,c1,c2) with c0!=c1, c1!=c2
        List<int[]> patterns = new ArrayList<>();
        for (int a = 0; a < 4; a++)
            for (int b = 0; b < 4; b++)
                for (int c = 0; c < 4; c++)
                    if (a != b && b != c) patterns.add(new int[]{a, b, c});

        // Build compatibility: pattern i is compatible with pattern j if no row matches
        int P = patterns.size(); // should be 36
        boolean[][] compat = new boolean[P][P];
        for (int i = 0; i < P; i++)
            for (int j = 0; j < P; j++) {
                int[] p = patterns.get(i), q = patterns.get(j);
                if (p[0] != q[0] && p[1] != q[1] && p[2] != q[2])
                    compat[i][j] = true;
            }

        // dp[i] = ways to fill up to current column ending with pattern i
        long[] dp = new long[P];
        Arrays.fill(dp, 1);

        for (int col = 1; col < n; col++) {
            long[] ndp = new long[P];
            for (int j = 0; j < P; j++) {
                for (int i = 0; i < P; i++) {
                    if (compat[i][j]) ndp[j] = (ndp[j] + dp[i]) % MOD;
                }
            }
            dp = ndp;
        }

        long ans = 0;
        for (long x : dp) ans = (ans + x) % MOD;
        return (int) ans;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - DP with precomputed transitions
// Time: O(n * P^2)  |  Space: O(P)
// ============================================================
// Same as brute force but cleaner: precompute next-state
// sums for each pattern. O(P^2) per column, O(n) columns.
// P=36 so P^2=1296 transitions per step. Very fast.
// ============================================================

class Optimal {
    static final int MOD = 1_000_000_007;

    public int solve(int n) {
        List<int[]> patterns = new ArrayList<>();
        for (int a = 0; a < 4; a++)
            for (int b = 0; b < 4; b++)
                for (int c = 0; c < 4; c++)
                    if (a != b && b != c) patterns.add(new int[]{a, b, c});

        int P = patterns.size();
        // Precompute adjacency sum: for each j, sum of dp[i] where compat[i][j]
        // Precompute transition lists: trans[j] = list of i compatible with j
        List<List<Integer>> trans = new ArrayList<>();
        for (int j = 0; j < P; j++) {
            trans.add(new ArrayList<>());
            int[] q = patterns.get(j);
            for (int i = 0; i < P; i++) {
                int[] p = patterns.get(i);
                if (p[0] != q[0] && p[1] != q[1] && p[2] != q[2])
                    trans.get(j).add(i);
            }
        }

        long[] dp = new long[P];
        Arrays.fill(dp, 1);

        for (int col = 1; col < n; col++) {
            long[] ndp = new long[P];
            for (int j = 0; j < P; j++)
                for (int i : trans.get(j))
                    ndp[j] = (ndp[j] + dp[i]) % MOD;
            dp = ndp;
        }

        long ans = 0;
        for (long x : dp) ans = (ans + x) % MOD;
        return (int) ans;
    }
}

// ============================================================
// APPROACH 3: BEST - Matrix Exponentiation for large N
// Time: O(P^3 * log n)  |  Space: O(P^2)
// ============================================================
// The DP transition is a linear recurrence: dp' = T * dp where
// T[j][i] = 1 if i and j are compatible. Use matrix
// exponentiation to compute T^(n-1) in O(P^3 log n) time.
// For small n, the iterative DP is faster; for huge n (up to
// 10^18), this is the only feasible approach.
// Real-life use: computing graph walk counts after many steps,
// Fibonacci-like sequences mod prime over large inputs.
// ============================================================

class Best {
    static final int MOD = 1_000_000_007;

    long[][] matMul(long[][] A, long[][] B, int sz) {
        long[][] C = new long[sz][sz];
        for (int i = 0; i < sz; i++)
            for (int k = 0; k < sz; k++) {
                if (A[i][k] == 0) continue;
                for (int j = 0; j < sz; j++)
                    C[i][j] = (C[i][j] + A[i][k] * B[k][j]) % MOD;
            }
        return C;
    }

    long[][] matPow(long[][] M, int p, int sz) {
        long[][] result = new long[sz][sz];
        for (int i = 0; i < sz; i++) result[i][i] = 1; // identity
        while (p > 0) {
            if ((p & 1) == 1) result = matMul(result, M, sz);
            M = matMul(M, M, sz);
            p >>= 1;
        }
        return result;
    }

    public int solve(int n) {
        List<int[]> patterns = new ArrayList<>();
        for (int a = 0; a < 4; a++)
            for (int b = 0; b < 4; b++)
                for (int c = 0; c < 4; c++)
                    if (a != b && b != c) patterns.add(new int[]{a, b, c});

        int P = patterns.size(); // 36
        long[][] T = new long[P][P];
        for (int i = 0; i < P; i++) {
            int[] p = patterns.get(i);
            for (int j = 0; j < P; j++) {
                int[] q = patterns.get(j);
                if (p[0] != q[0] && p[1] != q[1] && p[2] != q[2])
                    T[j][i] = 1;
            }
        }

        if (n == 1) {
            return P; // all 36 patterns are valid for a single column
        }

        long[][] Tn = matPow(T, n - 1, P);

        // Initial state: all patterns valid (each count = 1)
        long ans = 0;
        for (int j = 0; j < P; j++) {
            long sum = 0;
            for (int i = 0; i < P; i++) sum = (sum + Tn[j][i]) % MOD;
            ans = (ans + sum) % MOD;
        }
        return (int) ans;
    }
}

// ============================================================
// TEST HARNESS
// ============================================================

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Ways to Color 3xN Board ===");

        // n=1: 4*3*3=36 patterns (c0 has 4 choices, c1 has 3, c2 has 3)
        System.out.println("n=1 Brute   (expect 36): " + new BruteForce().solve(1));
        System.out.println("n=1 Optimal (expect 36): " + new Optimal().solve(1));
        System.out.println("n=1 Best    (expect 36): " + new Best().solve(1));

        // n=2: precomputed = 588
        System.out.println("n=2 Brute   (expect 588): " + new BruteForce().solve(2));
        System.out.println("n=2 Optimal (expect 588): " + new Optimal().solve(2));
        System.out.println("n=2 Best    (expect 588): " + new Best().solve(2));

        // n=3
        System.out.println("n=3 Brute:   " + new BruteForce().solve(3));
        System.out.println("n=3 Optimal: " + new Optimal().solve(3));
        System.out.println("n=3 Best:    " + new Best().solve(3));

        // n=5 cross-check
        int b5 = new BruteForce().solve(5);
        int o5 = new Optimal().solve(5);
        int best5 = new Best().solve(5);
        System.out.println("n=5 all match? " + (b5 == o5 && o5 == best5) + " val=" + b5);
    }
}
