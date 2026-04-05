/**
 * Problem: Pattern 21 - Hollow Rectangle
 * Difficulty: EASY | XP: 10
 *
 * For n=4, m=5:
 *   *****
 *   *   *
 *   *   *
 *   *****
 *
 * Stars on first row, last row, first column, last column.
 * Interior cells are spaces.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Explicit Boundary Check per Cell
    // Time: O(n*m)  |  Space: O(1)
    // Iterate every cell; print '*' on border, ' ' inside.
    // ============================================================
    static class BruteForce {
        public static void printHollowRect(int n, int m) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (i == 0 || i == n - 1 || j == 0 || j == m - 1) {
                        System.out.print("*");
                    } else {
                        System.out.print(" ");
                    }
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Pre-built Row Strings
    // Time: O(n*m)  |  Space: O(m)
    // Build border and middle row once; pick which to print.
    // ============================================================
    static class Optimal {
        public static void printHollowRect(int n, int m) {
            String borderRow = "*".repeat(m);
            String middleRow = "*" + " ".repeat(m - 2) + "*";
            for (int i = 0; i < n; i++) {
                if (i == 0 || i == n - 1) {
                    System.out.println(borderRow);
                } else {
                    System.out.println(middleRow);
                }
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Full Grid in StringBuilder, Single Print
    // Time: O(n*m)  |  Space: O(n*m)
    // Assemble all rows in one StringBuilder, single I/O call.
    // ============================================================
    static class Best {
        public static void printHollowRect(int n, int m) {
            String borderRow = "*".repeat(m);
            String middleRow = "*" + " ".repeat(m - 2) + "*";
            StringBuilder grid = new StringBuilder();
            for (int i = 0; i < n; i++) {
                grid.append(i == 0 || i == n - 1 ? borderRow : middleRow);
                if (i < n - 1) grid.append("\n");
            }
            System.out.println(grid);
        }
    }

    public static void main(String[] args) {
        int n = 4, m = 5;
        System.out.println("=== Pattern 21 - Hollow Rectangle ===");
        System.out.println("--- Brute Force ---");
        BruteForce.printHollowRect(n, m);
        System.out.println("--- Optimal ---");
        Optimal.printHollowRect(n, m);
        System.out.println("--- Best ---");
        Best.printHollowRect(n, m);
    }
}
