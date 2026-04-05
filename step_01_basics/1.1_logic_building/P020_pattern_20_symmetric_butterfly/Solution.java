/**
 * Problem: Pattern 20 - Symmetric Butterfly
 * Difficulty: EASY | XP: 10
 *
 * For n=4:
 *   *      *
 *   **    **
 *   ***  ***
 *   ********
 *   ********
 *   ***  ***
 *   **    **
 *   *      *
 *
 * Upper half: row i (1-indexed) -> i stars, 2*(n-i) spaces, i stars.
 * Lower half: mirror of upper half (row n down to 1).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Character-by-Character Printing
    // Time: O(n^2)  |  Space: O(1)
    // Print each star and space individually using inner loops.
    // ============================================================
    static class BruteForce {
        public static void printButterfly(int n) {
            // Upper half
            for (int i = 1; i <= n; i++) {
                for (int s = 0; s < i; s++)       System.out.print("*");
                for (int sp = 0; sp < 2 * (n - i); sp++) System.out.print(" ");
                for (int s = 0; s < i; s++)       System.out.print("*");
                System.out.println();
            }
            // Lower half
            for (int i = n; i >= 1; i--) {
                for (int s = 0; s < i; s++)       System.out.print("*");
                for (int sp = 0; sp < 2 * (n - i); sp++) System.out.print(" ");
                for (int s = 0; s < i; s++)       System.out.print("*");
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- StringBuilder per Row
    // Time: O(n^2)  |  Space: O(n)
    // Build each row as a StringBuilder then print it.
    // ============================================================
    static class Optimal {
        public static void printButterfly(int n) {
            // Upper half
            for (int i = 1; i <= n; i++) {
                StringBuilder row = new StringBuilder();
                row.append("*".repeat(i));
                row.append(" ".repeat(2 * (n - i)));
                row.append("*".repeat(i));
                System.out.println(row);
            }
            // Lower half
            for (int i = n; i >= 1; i--) {
                StringBuilder row = new StringBuilder();
                row.append("*".repeat(i));
                row.append(" ".repeat(2 * (n - i)));
                row.append("*".repeat(i));
                System.out.println(row);
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Full Grid in StringBuilder, Single Print
    // Time: O(n^2)  |  Space: O(n^2)
    // Assemble entire output in one StringBuilder, print once.
    // ============================================================
    static class Best {
        public static void printButterfly(int n) {
            StringBuilder grid = new StringBuilder();
            // Upper half
            for (int i = 1; i <= n; i++) {
                grid.append("*".repeat(i));
                grid.append(" ".repeat(2 * (n - i)));
                grid.append("*".repeat(i));
                grid.append("\n");
            }
            // Lower half
            for (int i = n; i >= 1; i--) {
                grid.append("*".repeat(i));
                grid.append(" ".repeat(2 * (n - i)));
                grid.append("*".repeat(i));
                if (i > 1) grid.append("\n");
            }
            System.out.println(grid);
        }
    }

    public static void main(String[] args) {
        int n = 4;
        System.out.println("=== Pattern 20 - Symmetric Butterfly ===");
        System.out.println("--- Brute Force ---");
        BruteForce.printButterfly(n);
        System.out.println("--- Optimal ---");
        Optimal.printButterfly(n);
        System.out.println("--- Best ---");
        Best.printButterfly(n);
    }
}
