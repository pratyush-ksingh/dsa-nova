/**
 * Problem: Pattern 1 - Rectangular Star
 * Difficulty: EASY | XP: 10
 *
 * Print a rectangle of stars with N rows and M columns.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
    // Time: O(N*M)  |  Space: O(1)
    // Two nested for-loops: outer for rows, inner for columns.
    // ============================================================
    static class BruteForce {
        public static void printRectangle(int n, int m) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (j > 0) System.out.print(" ");
                    System.out.print("*");
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- String Repetition per Row
    // Time: O(N*M)  |  Space: O(M)
    // Build one row string, print it N times.
    // ============================================================
    static class Optimal {
        public static void printRectangle(int n, int m) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < m; j++) {
                if (j > 0) row.append(" ");
                row.append("*");
            }
            String rowStr = row.toString();
            for (int i = 0; i < n; i++) {
                System.out.println(rowStr);
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Full Grid String, Single Print
    // Time: O(N*M)  |  Space: O(N*M)
    // Build entire grid as one string, single I/O call.
    // ============================================================
    static class Best {
        public static void printRectangle(int n, int m) {
            StringBuilder row = new StringBuilder();
            for (int j = 0; j < m; j++) {
                if (j > 0) row.append(" ");
                row.append("*");
            }
            String rowStr = row.toString();

            StringBuilder grid = new StringBuilder();
            for (int i = 0; i < n; i++) {
                grid.append(rowStr);
                if (i < n - 1) grid.append("\n");
            }
            System.out.println(grid.toString());
        }
    }

    public static void main(String[] args) {
        int n = 4, m = 5;
        System.out.println("=== Pattern 1 - Rectangular Star ===");
        System.out.println("--- Brute Force ---");
        BruteForce.printRectangle(n, m);
        System.out.println("--- Optimal ---");
        Optimal.printRectangle(n, m);
        System.out.println("--- Best ---");
        Best.printRectangle(n, m);
    }
}
