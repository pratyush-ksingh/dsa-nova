/**
 * Problem: Pattern 2 - Right-Angled Triangle
 * Difficulty: EASY | XP: 10
 *
 * Print a right-angled triangle of stars with N rows.
 * Row i (1-indexed) has i stars.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
    // Time: O(N^2)  |  Space: O(1)
    // Outer loop for rows, inner loop prints i stars for row i.
    // ============================================================
    static class BruteForce {
        public static void printTriangle(int n) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= i; j++) {
                    if (j > 1) System.out.print(" ");
                    System.out.print("*");
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- String Repetition per Row
    // Time: O(N^2)  |  Space: O(N)
    // Build each row using StringBuilder, print it.
    // ============================================================
    static class Optimal {
        public static void printTriangle(int n) {
            for (int i = 1; i <= n; i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 1; j <= i; j++) {
                    if (j > 1) row.append(" ");
                    row.append("*");
                }
                System.out.println(row.toString());
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Full Grid String, Single Print
    // Time: O(N^2)  |  Space: O(N^2)
    // Build entire triangle as one string, single I/O call.
    // ============================================================
    static class Best {
        public static void printTriangle(int n) {
            StringBuilder grid = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= i; j++) {
                    if (j > 1) grid.append(" ");
                    grid.append("*");
                }
                if (i < n) grid.append("\n");
            }
            System.out.println(grid.toString());
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("=== Pattern 2 - Right-Angled Triangle ===");
        System.out.println("--- Brute Force ---");
        BruteForce.printTriangle(n);
        System.out.println("--- Optimal ---");
        Optimal.printTriangle(n);
        System.out.println("--- Best ---");
        Best.printTriangle(n);
    }
}
