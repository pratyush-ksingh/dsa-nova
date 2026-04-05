/**
 * Problem: Pattern 6 - Inverted Numbered Triangle
 * Difficulty: EASY | XP: 10
 *
 * Print an inverted triangle where row i has numbers 1 to (N-i+1).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
    // Time: O(N^2)  |  Space: O(1)
    // Outer loop for rows, inner loop prints 1..(N-i+1).
    // ============================================================
    static class BruteForce {
        public static void printPattern(int n) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n - i + 1; j++) {
                    if (j > 1) System.out.print(" ");
                    System.out.print(j);
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Range-Based String per Row
    // Time: O(N^2)  |  Space: O(N)
    // Build each row with StringBuilder, print row by row.
    // ============================================================
    static class Optimal {
        public static void printPattern(int n) {
            for (int i = 1; i <= n; i++) {
                int count = n - i + 1;
                StringBuilder row = new StringBuilder();
                for (int j = 1; j <= count; j++) {
                    if (j > 1) row.append(" ");
                    row.append(j);
                }
                System.out.println(row.toString());
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Full Grid String, Single Print
    // Time: O(N^2)  |  Space: O(N^2)
    // Build entire grid as one string, single I/O call.
    // ============================================================
    static class Best {
        public static void printPattern(int n) {
            StringBuilder grid = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                int count = n - i + 1;
                for (int j = 1; j <= count; j++) {
                    if (j > 1) grid.append(" ");
                    grid.append(j);
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
