/**
 * Problem: Pattern 5 - Inverted Right Triangle
 * Difficulty: EASY | XP: 10
 *
 * Print an inverted right-angled triangle of stars.
 * Row i (1-indexed) has (N - i + 1) stars.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
    // Time: O(N^2)  |  Space: O(1)
    // Outer loop for rows, inner loop prints decreasing stars.
    // ============================================================
    static class BruteForce {
        public static void printPattern(int n) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n - i + 1; j++) {
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
    // Build each row using string join, print row by row.
    // ============================================================
    static class Optimal {
        public static void printPattern(int n) {
            for (int i = 1; i <= n; i++) {
                int stars = n - i + 1;
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < stars; j++) {
                    if (j > 0) row.append(" ");
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
        public static void printPattern(int n) {
            StringBuilder grid = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                int stars = n - i + 1;
                for (int j = 0; j < stars; j++) {
                    if (j > 0) grid.append(" ");
                    grid.append("*");
                }
                if (i < n) grid.append("\n");
            }
            System.out.println(grid.toString());
        }
    }

    // ============================================================
    // TESTS
    // ============================================================
    public static void main(String[] args) {
        int[] testCases = {1, 3, 5};

        for (int n : testCases) {
            System.out.println("=== N = " + n + " ===");

            System.out.println("--- Brute Force ---");
            BruteForce.printPattern(n);

            System.out.println("--- Optimal ---");
            Optimal.printPattern(n);

            System.out.println("--- Best ---");
            Best.printPattern(n);

            System.out.println();
        }
    }
}
