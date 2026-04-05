/**
 * Problem: Unique BST II
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Count the number of structurally unique BSTs that store values 1..n.
 * Answer is the Nth Catalan number.
 *
 * Catalan recurrence:
 *   C(0) = 1  (empty tree)
 *   C(n) = sum_{i=0}^{n-1} C(i) * C(n-1-i)   for n >= 1
 *
 * Explanation: for each root value r (1..n), left subtree has r-1 nodes
 * and right subtree has n-r nodes. Number of combinations = C(r-1)*C(n-r).
 * Summing over all roots gives C(n).
 *
 * Real-life use case: Counting parse tree structures, expression tree
 * combinatorics, analyzing balanced vs skewed BST distributions.
 *
 * @author DSA_Nova
 */

import java.util.HashMap;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n * Catalan(n)) ~ O(4^n / n^1.5)  |  Space: O(n) recursion depth
// Pure recursion without memoization.
// Useful to understand the recurrence but exponential in practice.
// ============================================================
class BruteForce {
    private int countTrees(int n) {
        if (n <= 1) return 1;
        int total = 0;
        for (int root = 1; root <= n; root++) {
            total += countTrees(root - 1) * countTrees(n - root);
        }
        return total;
    }

    public int numTrees(int n) {
        return countTrees(n);
    }
}

// ============================================================
// APPROACH 2: OPTIMAL (Top-down memoization)
// Time: O(n^2)  |  Space: O(n)
// Cache overlapping subproblems with a HashMap.
// countTrees(k) is called multiple times for same k -> memo avoids recomputation.
// ============================================================
class Optimal {
    private HashMap<Integer, Integer> memo = new HashMap<>();

    private int countTrees(int n) {
        if (n <= 1) return 1;
        if (memo.containsKey(n)) return memo.get(n);
        int total = 0;
        for (int root = 1; root <= n; root++) {
            total += countTrees(root - 1) * countTrees(n - root);
        }
        memo.put(n, total);
        return total;
    }

    public int numTrees(int n) {
        return countTrees(n);
    }
}

// ============================================================
// APPROACH 3: BEST (Bottom-up DP)
// Time: O(n^2)  |  Space: O(n)
// Build Catalan numbers iteratively.
// dp[i] = C(i) = sum_{j=0}^{i-1} dp[j] * dp[i-1-j]
// No recursion, simpler and fastest in practice.
// ============================================================
class Best {
    public int numTrees(int n) {
        long[] dp = new long[n + 1];
        dp[0] = 1;  // empty tree
        dp[1] = 1;  // single node
        for (int i = 2; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                dp[i] += dp[j] * dp[i - 1 - j];
            }
        }
        return (int) dp[n];
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Unique BST II (Count Unique BSTs) ===");

        BruteForce bf = new BruteForce();
        Optimal opt = new Optimal();
        Best best = new Best();

        // Known Catalan numbers: C(0)=1, C(1)=1, C(2)=2, C(3)=5, C(4)=14, C(5)=42
        int[] expected = {1, 1, 2, 5, 14, 42, 132, 429};

        System.out.printf("%-5s %-10s %-10s %-10s %-10s%n", "n", "Expected", "Brute", "Optimal", "Best");
        for (int n = 0; n <= 7; n++) {
            System.out.printf("%-5d %-10d %-10d %-10d %-10d%n",
                n, expected[n],
                bf.numTrees(n),
                opt.numTrees(n),
                best.numTrees(n));
        }
    }
}
