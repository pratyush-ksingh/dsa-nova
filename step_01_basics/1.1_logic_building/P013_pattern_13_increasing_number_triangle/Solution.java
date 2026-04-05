/**
 * Problem: Pattern 13 - Increasing Number Triangle (Floyd's Triangle)
 * Difficulty: EASY | XP: 10
 *
 * Print triangle with continuously increasing numbers across rows.
 * Row 1: 1, Row 2: 2 3, Row 3: 4 5 6, ...
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Counter Variable
    // Time: O(N^2)  |  Space: O(1)
    // Persistent counter incremented across all rows.
    // ============================================================
    static class BruteForce {
        public static void printPattern(int n) {
            int counter = 1;
            for (int i = 1; i <= n; i++) {
                for (int j = 0; j < i; j++) {
                    if (j > 0) System.out.print(" ");
                    System.out.print(counter);
                    counter++;
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Direct Formula per Row
    // Time: O(N^2)  |  Space: O(1)
    // Row i starts at i*(i-1)/2 + 1 (triangular number formula).
    // ============================================================
    static class Optimal {
        public static void printPattern(int n) {
            for (int i = 1; i <= n; i++) {
                int start = i * (i - 1) / 2 + 1;
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < i; j++) {
                    if (j > 0) row.append(" ");
                    row.append(start + j);
                }
                System.out.println(row);
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
            int counter = 1;
            for (int i = 1; i <= n; i++) {
                if (i > 1) grid.append("\n");
                for (int j = 0; j < i; j++) {
                    if (j > 0) grid.append(" ");
                    grid.append(counter);
                    counter++;
                }
            }
            System.out.println(grid);
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("=== Pattern 13 - Increasing Number Triangle ===");

        System.out.println("--- Brute Force (N=5) ---");
        BruteForce.printPattern(n);

        System.out.println("--- Optimal (N=5) ---");
        Optimal.printPattern(n);

        System.out.println("--- Best (N=5) ---");
        Best.printPattern(n);

        // Edge cases
        System.out.println("--- N=1 ---");
        Best.printPattern(1);

        System.out.println("--- N=3 ---");
        Best.printPattern(3);
    }
}
