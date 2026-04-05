/**
 * Problem: Factorial of N
 * Difficulty: EASY | XP: 10
 *
 * Compute N! = N * (N-1) * ... * 1, with 0! = 1.
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Iterative Loop)
// Time: O(n) | Space: O(1)
// ============================================================
class BruteForce {
    /**
     * Multiply 1 * 2 * ... * N in a loop.
     */
    public static long factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Factorial undefined for negative numbers");
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}

// ============================================================
// Approach 2: Optimal (Head Recursion)
// Time: O(n) | Space: O(n) recursion stack
// ============================================================
class Optimal {
    /**
     * fact(N) = N * fact(N-1), base case: fact(0) = 1.
     * Each call waits for the sub-call before multiplying.
     */
    public static long factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Factorial undefined for negative numbers");
        if (n == 0) return 1;
        return n * factorial(n - 1);
    }
}

// ============================================================
// Approach 3: Best (Tail Recursion with Accumulator)
// Time: O(n) | Space: O(1) with TCO, O(n) without
// ============================================================
class Best {
    /**
     * Tail-recursive: accumulator carries the product.
     * fact(N, acc) = fact(N-1, acc * N), base case: fact(0, acc) = acc.
     * JVM does not optimize tail calls, but this demonstrates the pattern.
     */
    public static long factorial(int n) {
        if (n < 0) throw new IllegalArgumentException("Factorial undefined for negative numbers");
        return helper(n, 1);
    }

    private static long helper(int n, long acc) {
        if (n == 0) return acc;
        return helper(n - 1, acc * n);
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Factorial of N ===\n");

        int[] inputs     = {0,  1,  2,  5,    10,       13,           20};
        long[] expected  = {1L, 1L, 2L, 120L, 3628800L, 6227020800L, 2432902008176640000L};

        for (int t = 0; t < inputs.length; t++) {
            int n = inputs[t];
            long b = BruteForce.factorial(n);
            long o = Optimal.factorial(n);
            long s = Best.factorial(n);
            boolean pass = (b == expected[t] && o == expected[t] && s == expected[t]);

            System.out.println("N = " + n);
            System.out.println("  Loop:      " + b);
            System.out.println("  Recursion: " + o);
            System.out.println("  Tail Rec:  " + s);
            System.out.println("  Expected:  " + expected[t]);
            System.out.println("  Pass:      " + pass);
            System.out.println();
        }

        // Demonstrate overflow: 21! overflows long
        System.out.println("--- Overflow Demo ---");
        System.out.println("20! = " + BruteForce.factorial(20) + " (fits in long)");
        System.out.println("21! = " + BruteForce.factorial(21) + " (OVERFLOWS long -- incorrect!)");
        System.out.println("For N > 20, use BigInteger in Java.\n");

        // Negative input test
        System.out.println("--- Negative Input ---");
        try {
            BruteForce.factorial(-1);
        } catch (IllegalArgumentException e) {
            System.out.println("factorial(-1) threw: " + e.getMessage() + " [PASS]");
        }
    }
}
