/**
 * Problem: Sum of First N Numbers
 * Difficulty: EASY | XP: 10
 *
 * Find 1 + 2 + 3 + ... + N using loop, recursion, and formula.
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Iterative Loop)
// Time: O(n) | Space: O(1)
// ============================================================
class BruteForce {
    /**
     * Simple accumulator loop from 1 to N.
     */
    public static long sum(int n) {
        long total = 0;
        for (int i = 1; i <= n; i++) {
            total += i;
        }
        return total;
    }
}

// ============================================================
// Approach 2: Optimal (Head Recursion)
// Time: O(n) | Space: O(n) recursion stack
// ============================================================
class Optimal {
    /**
     * sum(N) = N + sum(N-1), base case: sum(0) = 0.
     * Each call waits for the sub-call to return before adding N.
     */
    public static long sum(int n) {
        if (n == 0) return 0;
        return n + sum(n - 1);
    }
}

// ============================================================
// Approach 3: Best (Gauss's Formula)
// Time: O(1) | Space: O(1)
// ============================================================
class Best {
    /**
     * Closed-form: N * (N + 1) / 2.
     * Cast to long to avoid overflow for large N.
     */
    public static long sum(int n) {
        return (long) n * (n + 1) / 2;
    }
}

// Bonus: Tail Recursion (for languages that optimize tail calls)
class TailRecursive {
    /**
     * Tail-recursive version: accumulator passed as parameter.
     * sum(N, acc) = sum(N-1, acc + N), base case: sum(0, acc) = acc.
     * JVM does not optimize tail calls, but this demonstrates the concept.
     */
    public static long sum(int n) {
        return helper(n, 0);
    }

    private static long helper(int n, long acc) {
        if (n == 0) return acc;
        return helper(n - 1, acc + n);
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Sum of First N Numbers ===\n");

        int[] inputs   = {0, 1, 5, 10, 100, 1000};
        long[] expected = {0, 1, 15, 55, 5050, 500500};

        for (int t = 0; t < inputs.length; t++) {
            int n = inputs[t];
            long b = BruteForce.sum(n);
            long o = Optimal.sum(n);
            long s = Best.sum(n);
            long tr = TailRecursive.sum(n);
            boolean pass = (b == expected[t] && o == expected[t]
                         && s == expected[t] && tr == expected[t]);

            System.out.println("N = " + n);
            System.out.println("  Loop:       " + b);
            System.out.println("  Recursion:  " + o);
            System.out.println("  Formula:    " + s);
            System.out.println("  Tail Rec:   " + tr);
            System.out.println("  Expected:   " + expected[t]);
            System.out.println("  Pass:       " + pass);
            System.out.println();
        }

        // Large N test (only formula and loop -- recursion would stack overflow)
        int largeN = 1_000_000;
        long formulaResult = Best.sum(largeN);
        long loopResult = BruteForce.sum(largeN);
        System.out.println("Large N = " + largeN);
        System.out.println("  Formula: " + formulaResult);
        System.out.println("  Loop:    " + loopResult);
        System.out.println("  Match:   " + (formulaResult == loopResult));
    }
}
