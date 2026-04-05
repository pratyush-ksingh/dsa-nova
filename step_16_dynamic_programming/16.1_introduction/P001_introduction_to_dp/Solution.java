/**
 * Problem: Introduction to DP -- Fibonacci Number
 * Difficulty: EASY | XP: 10
 *
 * Compute Nth Fibonacci using all 4 DP approaches:
 * Recursion -> Memoization -> Tabulation -> Space Optimized
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Plain Recursion
// Time: O(2^n) | Space: O(n) recursion stack
// ============================================================
class Recursive {
    public int fib(int n) {
        if (n <= 1) return n;
        return fib(n - 1) + fib(n - 2);
    }
}

// ============================================================
// Approach 2: Memoization (Top-Down DP)
// Time: O(n) | Space: O(n)
// ============================================================
class Memoization {
    public int fib(int n) {
        int[] dp = new int[n + 1];
        Arrays.fill(dp, -1);
        return solve(n, dp);
    }

    private int solve(int n, int[] dp) {
        if (n <= 1) return n;
        if (dp[n] != -1) return dp[n]; // already computed

        dp[n] = solve(n - 1, dp) + solve(n - 2, dp);
        return dp[n];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(n) | Space: O(n)
// ============================================================
class Tabulation {
    public int fib(int n) {
        if (n <= 1) return n;

        int[] dp = new int[n + 1];
        dp[0] = 0;
        dp[1] = 1;

        for (int i = 2; i <= n; i++) {
            dp[i] = dp[i - 1] + dp[i - 2];
        }
        return dp[n];
    }
}

// ============================================================
// Approach 4: Space Optimized
// Time: O(n) | Space: O(1)
// ============================================================
class SpaceOptimized {
    public int fib(int n) {
        if (n <= 1) return n;

        int prev2 = 0; // fib(0)
        int prev1 = 1; // fib(1)

        for (int i = 2; i <= n; i++) {
            int curr = prev1 + prev2;
            prev2 = prev1;
            prev1 = curr;
        }
        return prev1;
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Introduction to DP: Fibonacci ===\n");

        Recursive rec = new Recursive();
        Memoization memo = new Memoization();
        Tabulation tab = new Tabulation();
        SpaceOptimized space = new SpaceOptimized();

        int[] testCases = {0, 1, 2, 5, 10, 20, 30};
        int[] expected =  {0, 1, 1, 5, 55, 6765, 832040};

        System.out.printf("%-5s %-12s %-12s %-12s %-12s %-12s %-6s%n",
                "n", "Recursive", "Memo", "Tabulation", "SpaceOpt", "Expected", "Pass");
        System.out.println("-".repeat(70));

        for (int t = 0; t < testCases.length; t++) {
            int n = testCases[t];
            int r = (n <= 25) ? rec.fib(n) : -1; // skip recursion for large n
            int m = memo.fib(n);
            int tb = tab.fib(n);
            int s = space.fib(n);

            boolean pass = m == expected[t] && tb == expected[t] && s == expected[t];
            if (n <= 25) pass = pass && (r == expected[t]);

            System.out.printf("%-5d %-12s %-12d %-12d %-12d %-12d %-6s%n",
                    n, (n <= 25 ? String.valueOf(r) : "skip"), m, tb, s, expected[t], pass);
        }

        // Demonstrate the exponential blowup
        System.out.println("\n--- Timing Demo ---");
        long start = System.nanoTime();
        int result = tab.fib(45);
        long elapsed = System.nanoTime() - start;
        System.out.println("fib(45) = " + result + " [Tabulation: " + elapsed / 1000 + " us]");

        start = System.nanoTime();
        result = space.fib(45);
        elapsed = System.nanoTime() - start;
        System.out.println("fib(45) = " + result + " [Space Opt:  " + elapsed / 1000 + " us]");
    }
}
