/**
 * Problem: Pattern 14 - Increasing Letter Triangle
 * Difficulty: EASY | XP: 10
 *
 * Print a triangle where row i contains letters A through the i-th letter.
 * A
 * A B
 * A B C
 * A B C D
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
    // Time: O(N^2)  |  Space: O(1)
    // Outer loop for rows, inner loop prints letters A..(A+i).
    // ============================================================
    static class BruteForce {
        public static void print(int n) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= i; j++) {
                    if (j > 0) System.out.print(" ");
                    System.out.print((char) ('A' + j));
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Incremental String Building per Row
    // Time: O(N^2)  |  Space: O(N)
    // Grow a row string by appending the next letter each iteration.
    // ============================================================
    static class Optimal {
        public static void print(int n) {
            StringBuilder row = new StringBuilder();
            for (int i = 0; i < n; i++) {
                if (i > 0) row.append(" ");
                row.append((char) ('A' + i));
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
            // Precompute full alphabet row with spaces
            StringBuilder fullRow = new StringBuilder();
            for (int j = 0; j < n; j++) {
                if (j > 0) fullRow.append(" ");
                fullRow.append((char) ('A' + j));
            }
            String full = fullRow.toString();

            StringBuilder grid = new StringBuilder();
            for (int i = 0; i < n; i++) {
                // Each row i uses first (2*i + 1) chars: "A" -> "A B" -> "A B C"
                grid.append(full, 0, 2 * i + 1);
                if (i < n - 1) grid.append("\n");
            }
            System.out.println(grid.toString());
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("=== Pattern 14 - Increasing Letter Triangle ===");
        System.out.println("--- Brute Force ---");
        BruteForce.print(n);
        System.out.println("--- Optimal ---");
        Optimal.print(n);
        System.out.println("--- Best ---");
        Best.print(n);
    }
}
