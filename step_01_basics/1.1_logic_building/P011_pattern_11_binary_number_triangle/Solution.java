/**
 * Problem: Pattern 11 - Binary Number Triangle
 * Difficulty: EASY | XP: 10
 *
 * Print a triangle of N rows with alternating 1s and 0s.
 * Odd rows start with 1, even rows start with 0.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Toggle Variable per Row
    // Time: O(N^2)  |  Space: O(1)
    // Set start value based on row parity, toggle after each print.
    // ============================================================
    static class BruteForce {
        public static void printPattern(int n) {
            for (int i = 1; i <= n; i++) {
                int val = (i % 2 == 1) ? 1 : 0;  // odd rows start 1, even start 0
                for (int j = 0; j < i; j++) {
                    if (j > 0) System.out.print(" ");
                    System.out.print(val);
                    val = 1 - val;  // toggle
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Direct Formula (i+j) % 2
    // Time: O(N^2)  |  Space: O(1)
    // Value at (i, j) = (i + j) % 2 (0-indexed).
    // ============================================================
    static class Optimal {
        public static void printPattern(int n) {
            for (int i = 0; i < n; i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j <= i; j++) {
                    if (j > 0) row.append(" ");
                    row.append((i + j) % 2);
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
            for (int i = 0; i < n; i++) {
                if (i > 0) grid.append("\n");
                for (int j = 0; j <= i; j++) {
                    if (j > 0) grid.append(" ");
                    grid.append((i + j) % 2);
                }
            }
            System.out.println(grid);
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("=== Pattern 11 - Binary Number Triangle ===");

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
