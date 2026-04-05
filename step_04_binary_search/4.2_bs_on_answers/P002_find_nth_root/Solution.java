/**
 * Problem: Find Nth Root
 * Difficulty: MEDIUM | XP: 25
 *
 * Key Insight: Binary search on the answer space [1, m].
 * Check if mid^n == m using overflow-safe power computation.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Linear Scan
    // Time: O(m^(1/n) * n)  |  Space: O(1)
    //
    // Try each x from 1 upward, compute x^n.
    // ============================================================
    public static int bruteForce(int n, int m) {
        if (m == 0) return 0;
        for (long x = 1; x <= m; x++) {
            int cmp = safePow(x, n, m);
            if (cmp == 1) return (int) x;   // x^n == m
            if (cmp == 2) return -1;         // x^n > m, no point continuing
        }
        return -1;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Binary Search on Answer Space
    // Time: O(n * log(m))  |  Space: O(1)
    //
    // Binary search [1, m]. For each mid, check mid^n vs m.
    // ============================================================
    public static int optimal(int n, int m) {
        if (m == 0) return 0;
        if (n == 1) return m;

        long lo = 1, hi = m;
        while (lo <= hi) {
            long mid = lo + (hi - lo) / 2;
            int cmp = safePow(mid, n, m);
            if (cmp == 1) return (int) mid;  // mid^n == m
            if (cmp == 0) lo = mid + 1;      // mid^n < m
            else hi = mid - 1;               // mid^n > m
        }
        return -1;
    }

    /**
     * Compute base^exp vs target safely.
     * Returns: 0 if base^exp < target, 1 if ==, 2 if >.
     * Stops early on overflow / exceeding target.
     */
    private static int safePow(long base, int exp, int target) {
        long result = 1;
        for (int i = 0; i < exp; i++) {
            result *= base;
            if (result > target) return 2;  // exceeds target
        }
        if (result == target) return 1;
        return 0;
    }

    // ============================================================
    // APPROACH 3: BEST -- Newton's Method (Integer)
    // Time: O(n * log(log(m)))  |  Space: O(1)
    //
    // Quadratic convergence: x_new = ((n-1)*x + m/x^(n-1)) / n
    // ============================================================
    public static int best(int n, int m) {
        if (m == 0) return 0;
        if (m == 1 || n == 1) return (n == 1) ? m : 1;

        // Start from a reasonable initial guess
        long x = (long) Math.pow(m, 1.0 / n) + 1;

        // Newton's iteration
        for (int iter = 0; iter < 100; iter++) {
            // Compute x^(n-1) safely
            long xPowNm1 = 1;
            boolean overflow = false;
            for (int i = 0; i < n - 1; i++) {
                xPowNm1 *= x;
                if (xPowNm1 > m) {
                    overflow = true;
                    break;
                }
            }

            long xNew;
            if (overflow) {
                // x is too large, reduce
                xNew = x - 1;
            } else {
                xNew = ((n - 1) * x + m / xPowNm1) / n;
            }

            if (xNew >= x) break;  // converged
            x = xNew;
        }

        // Check x and x+1 (Newton's can undershoot by 1)
        if (safePow(x, n, m) == 1) return (int) x;
        if (safePow(x + 1, n, m) == 1) return (int) (x + 1);
        return -1;
    }

    public static void main(String[] args) {
        System.out.println("=== Find Nth Root ===");

        int[][] tests = {{3, 27}, {2, 16}, {3, 20}, {4, 81}, {2, 1}, {1, 100}};
        for (int[] t : tests) {
            int n = t[0], m = t[1];
            System.out.printf("n=%d, m=%d%n", n, m);
            System.out.printf("  Brute:   %d%n", bruteForce(n, m));
            System.out.printf("  Optimal: %d%n", optimal(n, m));
            System.out.printf("  Best:    %d%n", best(n, m));
            System.out.println();
        }
    }
}
