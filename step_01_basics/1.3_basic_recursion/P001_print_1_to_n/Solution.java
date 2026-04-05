/**
 * Problem: Print 1 to N (Recursion)
 * Difficulty: EASY | XP: 10
 *
 * Print numbers from 1 to N using recursion (no loops).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: Counting Up (pass current counter)
    // Time: O(n)  |  Space: O(n) call stack
    // Print current number, then recurse for next.
    // ============================================================
    public static void printAscending(int i, int n) {
        if (i > n) return;  // Base case: stop when past N

        System.out.println(i);
        printAscending(i + 1, n);  // Recurse for next number
    }

    // ============================================================
    // APPROACH 2: Backtracking (count down, print after recursion)
    // Time: O(n)  |  Space: O(n) call stack
    // Recurse first (going deeper), print on the way back up.
    // ============================================================
    public static void printViaBacktracking(int n) {
        if (n < 1) return;  // Base case: stop at 0

        printViaBacktracking(n - 1);  // Go deeper first
        System.out.println(n);        // Print AFTER returning = ascending order
    }

    // ============================================================
    // APPROACH 3: Single parameter (clean API)
    // Time: O(n)  |  Space: O(n) call stack
    // Same as Approach 2 but demonstrates the cleanest interface.
    // ============================================================
    public static void print1ToN(int n) {
        if (n == 0) return;
        print1ToN(n - 1);     // Handle 1..n-1 first
        System.out.println(n); // Then print n
    }

    public static void main(String[] args) {
        System.out.println("=== Print 1 to N (Recursion) ===\n");

        int n = 5;

        System.out.println("Approach 1: Counting Up (i=1 to n=" + n + ")");
        printAscending(1, n);

        System.out.println("\nApproach 2: Backtracking (n=" + n + ")");
        printViaBacktracking(n);

        System.out.println("\nApproach 3: Clean API (n=" + n + ")");
        print1ToN(n);

        // Edge cases
        System.out.println("\n--- Edge Case: N = 1 ---");
        print1ToN(1);

        System.out.println("\n--- Edge Case: N = 0 ---");
        print1ToN(0);  // prints nothing
        System.out.println("(nothing printed)");
    }
}
