/**
 * Problem: Pattern 10 - Half Diamond Star
 * Difficulty: EASY | XP: 10
 *
 * Print a left-aligned half diamond: stars increase from 1 to N,
 * then decrease from N-1 back to 1. Total (2*N - 1) rows.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Two Separate Loops
    // Time: O(N^2)  |  Space: O(1)
    // First loop: increasing 1..N. Second loop: decreasing N-1..1.
    // ============================================================
    static class BruteForce {
        public static void printPattern(int n) {
            // Increasing half: 1 to N stars
            for (int i = 1; i <= n; i++) {
                for (int j = 0; j < i; j++) {
                    if (j > 0) System.out.print(" ");
                    System.out.print("*");
                }
                System.out.println();
            }
            // Decreasing half: N-1 down to 1 star
            for (int i = n - 1; i >= 1; i--) {
                for (int j = 0; j < i; j++) {
                    if (j > 0) System.out.print(" ");
                    System.out.print("*");
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Single Loop with abs() Formula
    // Time: O(N^2)  |  Space: O(N)
    // stars = N - |N - 1 - i| for i in [0, 2N-2].
    // ============================================================
    static class Optimal {
        public static void printPattern(int n) {
            int totalRows = 2 * n - 1;
            for (int i = 0; i < totalRows; i++) {
                int stars = n - Math.abs(n - 1 - i);
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < stars; j++) {
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
    // Build entire half diamond as one string, single I/O call.
    // ============================================================
    static class Best {
        public static void printPattern(int n) {
            StringBuilder grid = new StringBuilder();
            int totalRows = 2 * n - 1;
            for (int i = 0; i < totalRows; i++) {
                if (i > 0) grid.append("\n");
                int stars = n - Math.abs(n - 1 - i);
                for (int j = 0; j < stars; j++) {
                    if (j > 0) grid.append(" ");
                    grid.append("*");
                }
            }
            System.out.println(grid);
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("=== Pattern 10 - Half Diamond Star ===");

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
