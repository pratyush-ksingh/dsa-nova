/**
 * Problem: Power Function using Recursion (LeetCode #50)
 * Difficulty: MEDIUM | XP: 25
 *
 * Implement pow(x, n) which calculates x raised to the power n.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Linear Recursion
    // Time: O(n)  |  Space: O(n)
    //
    // Simple recursive definition: x^n = x * x^(n-1).
    // Too slow for large n but illustrates the concept.
    // ============================================================
    public static double bruteForce(double x, int n) {
        if (n == 0) return 1.0;
        if (x == 0) return 0.0;
        if (x == 1) return 1.0;
        if (x == -1) return n % 2 == 0 ? 1.0 : -1.0;

        long N = n;
        if (N < 0) {
            x = 1.0 / x;
            N = -N;
        }
        return linearRecurse(x, N);
    }

    private static double linearRecurse(double x, long n) {
        if (n == 0) return 1.0;
        return x * linearRecurse(x, n - 1);
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Recursive Binary Exponentiation
    // Time: O(log n)  |  Space: O(log n)
    //
    // x^n = (x^(n/2))^2 if even, x * (x^(n/2))^2 if odd.
    // Halves n at each step for logarithmic depth.
    // ============================================================
    public static double optimal(double x, int n) {
        long N = n;
        if (N < 0) {
            x = 1.0 / x;
            N = -N;
        }
        return fastPowRecursive(x, N);
    }

    private static double fastPowRecursive(double x, long n) {
        if (n == 0) return 1.0;

        double half = fastPowRecursive(x, n / 2);

        if (n % 2 == 0) {
            return half * half;
        } else {
            return x * half * half;
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Iterative Binary Exponentiation
    // Time: O(log n)  |  Space: O(1)
    //
    // Uses binary representation of n. For each set bit,
    // multiply result by the corresponding power of x.
    // ============================================================
    public static double best(double x, int n) {
        long N = n;
        if (N < 0) {
            x = 1.0 / x;
            N = -N;
        }

        double result = 1.0;
        double currentProduct = x;

        while (N > 0) {
            if ((N & 1) == 1) {
                result *= currentProduct;
            }
            currentProduct *= currentProduct;
            N >>= 1;
        }

        return result;
    }

    // ============================================================
    // TESTING
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Power Function using Recursion ===\n");

        double[][] tests = {
            {2.0, 10, 1024.0},
            {2.1, 3, 9.261},
            {2.0, -2, 0.25},
            {1.0, 2147483647, 1.0},
            {2.0, 0, 1.0},
            {-1.0, 3, -1.0},
            {0.5, -2, 4.0},
        };

        for (double[] test : tests) {
            double x = test[0];
            int n = (int) test[1];
            double expected = test[2];

            // Skip brute force for huge n
            double b = Math.abs(n) <= 10000 ? bruteForce(x, n) : -999;
            double o = optimal(x, n);
            double be = best(x, n);

            System.out.printf("pow(%.1f, %d)  Expected: %.4f%n", x, n, expected);
            if (b != -999)
                System.out.printf("  Brute:   %.4f  %s%n", b, Math.abs(b - expected) < 0.001 ? "PASS" : "FAIL");
            else
                System.out.printf("  Brute:   SKIPPED (n too large)%n");
            System.out.printf("  Optimal: %.4f  %s%n", o, Math.abs(o - expected) < 0.001 ? "PASS" : "FAIL");
            System.out.printf("  Best:    %.4f  %s%n", be, Math.abs(be - expected) < 0.001 ? "PASS" : "FAIL");
            System.out.println();
        }
    }
}
