/**
 * Problem: Implement Power Function (LeetCode #50 / InterviewBit)
 * Difficulty: EASY | XP: 10
 *
 * Implement pow(x, n) using binary exponentiation in O(log n).
 * Key insight: x^n = (x^(n/2))^2 when n is even, x * x^(n-1) when n is odd.
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Multiply n Times)
// Time: O(|n|) | Space: O(1)
// NOTE: TLEs for large n. Shown for completeness.
// ============================================================
class BruteForce {
    public static double myPow(double x, int n) {
        // Use long to avoid overflow when negating Integer.MIN_VALUE
        long N = n;
        if (N < 0) {
            x = 1.0 / x;
            N = -N;
        }
        double result = 1.0;
        // Cap at 10^6 iterations to avoid TLE in testing
        long limit = Math.min(N, 1000000L);
        for (long i = 0; i < limit; i++) {
            result *= x;
        }
        // If N was larger than limit, this result is wrong
        // but brute force cannot handle huge n anyway
        if (N > limit) return Double.NaN;
        return result;
    }
}

// ============================================================
// Approach 2: Optimal (Iterative Binary Exponentiation)
// Time: O(log n) | Space: O(1)
// ============================================================
class Optimal {
    public static double myPow(double x, int n) {
        // Use long to safely negate Integer.MIN_VALUE
        long N = n;
        if (N < 0) {
            x = 1.0 / x;
            N = -N;
        }

        double result = 1.0;
        while (N > 0) {
            if (N % 2 == 1) {
                result *= x;  // odd exponent: take one x out
            }
            x *= x;          // square the base
            N /= 2;          // halve the exponent
        }
        return result;
    }
}

// ============================================================
// Approach 3: Best (Recursive Binary Exponentiation)
// Time: O(log n) | Space: O(log n) recursion stack
// ============================================================
class Best {
    public static double myPow(double x, int n) {
        long N = n;
        if (N < 0) {
            x = 1.0 / x;
            N = -N;
        }
        return helper(x, N);
    }

    private static double helper(double x, long n) {
        if (n == 0) return 1.0;
        double half = helper(x, n / 2);
        if (n % 2 == 0) {
            return half * half;
        } else {
            return half * half * x;
        }
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Implement Power Function ===\n");

        double[] bases =    {2.0,   2.1,  2.0,  1.0,          0.0, -1.0, -1.0, 2.0};
        int[] exponents =   {10,    3,    -2,   2147483647,    1,   2,    3,    0};
        double[] expected = {1024.0, 9.261, 0.25, 1.0,         0.0, 1.0, -1.0, 1.0};

        for (int t = 0; t < bases.length; t++) {
            double x = bases[t];
            int n = exponents[t];
            double o = Optimal.myPow(x, n);
            double r = Best.myPow(x, n);

            // For brute force, skip huge exponents
            String bruteStr;
            if (Math.abs((long) n) <= 1000000L) {
                double b = BruteForce.myPow(x, n);
                bruteStr = String.format("%.3f", b);
            } else {
                bruteStr = "SKIPPED (n too large)";
            }

            boolean pass = Math.abs(o - expected[t]) < 0.01 && Math.abs(r - expected[t]) < 0.01;

            System.out.println("Input:    x=" + x + ", n=" + n);
            System.out.println("Brute:    " + bruteStr);
            System.out.printf("Optimal:  %.3f%n", o);
            System.out.printf("Best:     %.3f%n", r);
            System.out.printf("Expected: %.3f%n", expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
