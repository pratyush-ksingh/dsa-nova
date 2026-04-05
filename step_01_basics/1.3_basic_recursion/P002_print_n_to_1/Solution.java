/**
 * Problem: Print N to 1 (Recursion)
 * Difficulty: EASY | XP: 10
 *
 * Print numbers from N to 1 using recursion (no loops).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: Count Down (print before recursion)
    // Time: O(n)  |  Space: O(n) call stack
    // Print N first, then recurse for N-1.
    // ============================================================
    public static void printDescending(int n) {
        if (n < 1) return;  // Base case: stop at 0

        System.out.println(n);       // Print BEFORE recursion = descending
        printDescending(n - 1);      // Recurse for smaller problem
    }

    // ============================================================
    // APPROACH 2: Count Up with Backtracking (print after recursion)
    // Time: O(n)  |  Space: O(n) call stack
    // Count up from 1 to N, but print on the way back (LIFO).
    // ============================================================
    public static void printViaBacktracking(int i, int n) {
        if (i > n) return;  // Base case

        printViaBacktracking(i + 1, n);  // Go deeper first
        System.out.println(i);           // Print AFTER return = descending
    }

    // ============================================================
    // APPROACH 3: Clean single-parameter version
    // Time: O(n)  |  Space: O(n) call stack
    // ============================================================
    public static void printNTo1(int n) {
        if (n == 0) return;
        System.out.println(n);  // Print current
        printNTo1(n - 1);       // Handle rest
    }

    public static void main(String[] args) {
        System.out.println("=== Print N to 1 (Recursion) ===\n");

        int n = 5;

        System.out.println("Approach 1: Count Down (n=" + n + ")");
        printDescending(n);

        System.out.println("\nApproach 2: Backtracking (i=1, n=" + n + ")");
        printViaBacktracking(1, n);

        System.out.println("\nApproach 3: Clean API (n=" + n + ")");
        printNTo1(n);

        // Edge cases
        System.out.println("\n--- Edge Case: N = 1 ---");
        printNTo1(1);

        System.out.println("\n--- Edge Case: N = 0 ---");
        printNTo1(0);
        System.out.println("(nothing printed)");
    }
}
