/**
 * Problem: Pattern 8 - Inverted Star Pyramid
 * Difficulty: EASY | XP: 10
 *
 * Print an inverted centered star pyramid with N rows.
 * Row 0 (top) has (2*N - 1) stars, row N-1 (bottom) has 1 star.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
    // Time: O(N^2)  |  Space: O(1)
    // Three loops per row: spaces, stars, newline.
    // ============================================================
    static class BruteForce {
        public static void printPattern(int n) {
            for (int i = 0; i < n; i++) {
                // Print leading spaces
                for (int s = 0; s < i; s++) {
                    System.out.print(" ");
                }
                // Print stars with spaces between them
                int starCount = 2 * (n - i) - 1;
                for (int j = 0; j < starCount; j++) {
                    if (j > 0) System.out.print(" ");
                    System.out.print("*");
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- String Repetition per Row
    // Time: O(N^2)  |  Space: O(N)
    // Build each row using StringBuilder, print row by row.
    // ============================================================
    static class Optimal {
        public static void printPattern(int n) {
            for (int i = 0; i < n; i++) {
                StringBuilder row = new StringBuilder();
                // Leading spaces
                for (int s = 0; s < i; s++) {
                    row.append(" ");
                }
                // Stars with spaces between
                int starCount = 2 * (n - i) - 1;
                for (int j = 0; j < starCount; j++) {
                    if (j > 0) row.append(" ");
                    row.append("*");
                }
                System.out.println(row);
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Full Grid String, Single Print
    // Time: O(N^2)  |  Space: O(N^2)
    // Build entire pattern as one string, single I/O call.
    // ============================================================
    static class Best {
        public static void printPattern(int n) {
            StringBuilder grid = new StringBuilder();
            for (int i = 0; i < n; i++) {
                if (i > 0) grid.append("\n");
                // Leading spaces
                for (int s = 0; s < i; s++) {
                    grid.append(" ");
                }
                // Stars with spaces between
                int starCount = 2 * (n - i) - 1;
                for (int j = 0; j < starCount; j++) {
                    if (j > 0) grid.append(" ");
                    grid.append("*");
                }
            }
            System.out.println(grid);
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("=== Pattern 8 - Inverted Star Pyramid ===");

        System.out.println("--- Brute Force (N=5) ---");
        BruteForce.printPattern(n);

        System.out.println("--- Optimal (N=5) ---");
        Optimal.printPattern(n);

        System.out.println("--- Best (N=5) ---");
        Best.printPattern(n);

        // Edge case tests
        System.out.println("--- N=1 ---");
        BruteForce.printPattern(1);

        System.out.println("--- N=3 ---");
        Best.printPattern(3);
    }
}
