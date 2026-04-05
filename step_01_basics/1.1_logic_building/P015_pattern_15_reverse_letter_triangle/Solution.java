/**
 * Problem: Pattern 15 - Reverse Letter Triangle
 * Difficulty: EASY | XP: 10
 *
 * Print an inverted triangle where each row starts at A and shrinks.
 * A B C D E
 * A B C D
 * A B C
 * A B
 * A
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
    // Time: O(N^2)  |  Space: O(1)
    // Outer loop for rows, inner loop prints A..(A + N-i-1).
    // ============================================================
    static class BruteForce {
        public static void print(int n) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n - i; j++) {
                    if (j > 0) System.out.print(" ");
                    System.out.print((char) ('A' + j));
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Substring per Row
    // Time: O(N^2)  |  Space: O(N)
    // Precompute full row, print shrinking substrings.
    // ============================================================
    static class Optimal {
        public static void print(int n) {
            StringBuilder fullRow = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if (j > 0) fullRow.append(" ");
                fullRow.append((char) ('A' + j));
            }
            String full = fullRow.toString();
            for (int i = 0; i < n; i++) {
                int len = 2 * (n - i) - 1;
                System.out.println(full.substring(0, len));
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
            StringBuilder fullRow = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if (j > 0) fullRow.append(" ");
                fullRow.append((char) ('A' + j));
            }
            String full = fullRow.toString();

            StringBuilder grid = new StringBuilder();
            for (int i = 0; i < n; i++) {
                int len = 2 * (n - i) - 1;
                grid.append(full, 0, len);
                if (i < n - 1) grid.append("\n");
            }
            System.out.println(grid.toString());
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("=== Pattern 15 - Reverse Letter Triangle ===");
        System.out.println("--- Brute Force ---");
        BruteForce.print(n);
        System.out.println("--- Optimal ---");
        Optimal.print(n);
        System.out.println("--- Best ---");
        Best.print(n);
    }
}
