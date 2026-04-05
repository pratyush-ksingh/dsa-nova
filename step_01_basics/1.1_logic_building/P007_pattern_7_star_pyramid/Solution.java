/**
 * Problem: Pattern 7 - Star Pyramid
 * Difficulty: EASY | XP: 10
 *
 * Print a centered star pyramid with N rows.
 * Row i has (N-i) leading spaces and (2*i-1) stars.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Three Nested Loops
    // Time: O(N^2)  |  Space: O(1)
    // One loop for spaces, one loop for stars per row.
    // ============================================================
    static class BruteForce {
        public static void printPattern(int n) {
            for (int i = 1; i <= n; i++) {
                // Print leading spaces
                for (int s = 0; s < n - i; s++) {
                    System.out.print(" ");
                }
                // Print stars
                for (int j = 0; j < 2 * i - 1; j++) {
                    System.out.print("*");
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- String Repetition per Row
    // Time: O(N^2)  |  Space: O(N)
    // Build each row using string repetition.
    // ============================================================
    static class Optimal {
        public static void printPattern(int n) {
            for (int i = 1; i <= n; i++) {
                StringBuilder row = new StringBuilder();
                // Leading spaces
                for (int s = 0; s < n - i; s++) {
                    row.append(" ");
                }
                // Stars
                for (int j = 0; j < 2 * i - 1; j++) {
                    row.append("*");
                }
                System.out.println(row.toString());
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Full Grid String, Single Print
    // Time: O(N^2)  |  Space: O(N^2)
    // Build entire pyramid as one string, single I/O call.
    // ============================================================
    static class Best {
        public static void printPattern(int n) {
            StringBuilder grid = new StringBuilder();
            for (int i = 1; i <= n; i++) {
                // Leading spaces
                for (int s = 0; s < n - i; s++) {
                    grid.append(" ");
                }
                // Stars
                for (int j = 0; j < 2 * i - 1; j++) {
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
