/**
 * Problem: Print Name N Times (Recursion)
 * Difficulty: EASY | XP: 10
 *
 * Print your name N times using recursion (no loops).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: Count Down from N
    // Time: O(n)  |  Space: O(n) call stack
    // Print name, then recurse with N-1.
    // ============================================================
    public static void printNameCountDown(String name, int n) {
        if (n <= 0) return;  // Base case: no more prints needed

        System.out.println(name);
        printNameCountDown(name, n - 1);
    }

    // ============================================================
    // APPROACH 2: Count Up from 1 to N
    // Time: O(n)  |  Space: O(n) call stack
    // Use a counter that increments until it exceeds N.
    // ============================================================
    public static void printNameCountUp(String name, int i, int n) {
        if (i > n) return;  // Base case: counter exceeded N

        System.out.println(name);
        printNameCountUp(name, i + 1, n);
    }

    // ============================================================
    // APPROACH 3: With iteration counter in output
    // Time: O(n)  |  Space: O(n) call stack
    // Shows which iteration we are on -- helpful for debugging.
    // ============================================================
    public static void printNameWithCount(String name, int i, int n) {
        if (i > n) return;

        System.out.println(i + ". " + name);
        printNameWithCount(name, i + 1, n);
    }

    public static void main(String[] args) {
        System.out.println("=== Print Name N Times (Recursion) ===\n");

        String name = "Nova";
        int n = 5;

        System.out.println("Approach 1: Count Down (n=" + n + ")");
        printNameCountDown(name, n);

        System.out.println("\nApproach 2: Count Up (1 to n=" + n + ")");
        printNameCountUp(name, 1, n);

        System.out.println("\nApproach 3: With Iteration Number");
        printNameWithCount(name, 1, n);

        // Edge cases
        System.out.println("\n--- Edge Case: N = 1 ---");
        printNameCountDown(name, 1);

        System.out.println("\n--- Edge Case: N = 0 ---");
        printNameCountDown(name, 0);
        System.out.println("(nothing printed)");

        System.out.println("\n--- Custom Name ---");
        printNameWithCount("DSA_Nova", 1, 3);
    }
}
