/**
 * Problem: Pattern 16 - Alpha Ramp
 * Difficulty: EASY | XP: 10
 *
 * Print a triangle where row i has the i-th letter repeated i times.
 * A
 * B B
 * C C C
 * D D D D
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
    // Time: O(N^2)  |  Space: O(1)
    // Outer loop picks the letter, inner loop repeats it.
    // ============================================================
    static class BruteForce {
        public static void print(int n) {
            for (int i = 0; i < n; i++) {
                char ch = (char) ('A' + i);
                for (int j = 0; j <= i; j++) {
                    if (j > 0) System.out.print(" ");
                    System.out.print(ch);
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- String Repetition per Row
    // Time: O(N^2)  |  Space: O(N)
    // Build each row using String repetition, one print per row.
    // ============================================================
    static class Optimal {
        public static void print(int n) {
            for (int i = 0; i < n; i++) {
                char ch = (char) ('A' + i);
                StringBuilder row = new StringBuilder();
                for (int j = 0; j <= i; j++) {
                    if (j > 0) row.append(" ");
                    row.append(ch);
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
        public static void print(int n) {
            StringBuilder grid = new StringBuilder();
            for (int i = 0; i < n; i++) {
                char ch = (char) ('A' + i);
                for (int j = 0; j <= i; j++) {
                    if (j > 0) grid.append(" ");
                    grid.append(ch);
                }
                if (i < n - 1) grid.append("\n");
            }
            System.out.println(grid.toString());
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("=== Pattern 16 - Alpha Ramp ===");
        System.out.println("--- Brute Force ---");
        BruteForce.print(n);
        System.out.println("--- Optimal ---");
        Optimal.print(n);
        System.out.println("--- Best ---");
        Best.print(n);
    }
}
