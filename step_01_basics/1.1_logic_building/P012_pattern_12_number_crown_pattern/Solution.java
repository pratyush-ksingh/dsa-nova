/**
 * Problem: Pattern 12 - Number Crown Pattern
 * Difficulty: EASY | XP: 10
 *
 * Print a number crown: increasing numbers on left, spaces in middle,
 * mirror-decreasing numbers on right.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Three Inner Loops per Row
    // Time: O(N^2)  |  Space: O(1)
    // Left numbers + spaces + right numbers using explicit loops.
    // ============================================================
    static class BruteForce {
        public static void printPattern(int n) {
            for (int i = 1; i <= n; i++) {
                // Left side: 1 to i
                for (int j = 1; j <= i; j++) {
                    System.out.print(j);
                    System.out.print(" ");
                }
                // Middle spaces: 2*(N-i) spaces (each slot is 2 chars)
                for (int s = 0; s < 2 * (n - i); s++) {
                    System.out.print(" ");
                }
                // Right side: i to 1
                for (int j = i; j >= 1; j--) {
                    System.out.print(j);
                    if (j > 1) System.out.print(" ");
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- String Building per Row
    // Time: O(N^2)  |  Space: O(N)
    // Build each row as a StringBuilder with three sections.
    // ============================================================
    static class Optimal {
        public static void printPattern(int n) {
            for (int i = 1; i <= n; i++) {
                StringBuilder row = new StringBuilder();
                // Left side
                for (int j = 1; j <= i; j++) {
                    row.append(j);
                    row.append(" ");
                }
                // Middle spaces
                for (int s = 0; s < 2 * (n - i); s++) {
                    row.append(" ");
                }
                // Right side
                for (int j = i; j >= 1; j--) {
                    row.append(j);
                    if (j > 1) row.append(" ");
                }
                System.out.println(row);
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Full Grid String, Single Print
    // Time: O(N^2)  |  Space: O(N^2)
    // Build entire crown as one string, single I/O call.
    // ============================================================
    static class Best {
        public static void printPattern(int n) {
            StringBuilder grid = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                if (i > 1) grid.append("\n");
                // Left side
                for (int j = 1; j <= i; j++) {
                    grid.append(j);
                    grid.append(" ");
                }
                // Middle spaces
                for (int s = 0; s < 2 * (n - i); s++) {
                    grid.append(" ");
                }
                // Right side
                for (int j = i; j >= 1; j--) {
                    grid.append(j);
                    if (j > 1) grid.append(" ");
                }
            }
            System.out.println(grid);
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("=== Pattern 12 - Number Crown Pattern ===");

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
