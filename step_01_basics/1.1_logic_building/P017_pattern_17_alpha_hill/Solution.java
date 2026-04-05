/**
 * Problem: Pattern 17 - Alpha Hill
 * Difficulty: EASY | XP: 10
 *
 * Print an alpha hill pattern. For n=3:
 *   A
 *  ABA
 * ABCBA
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
    // Time: O(N^2)  |  Space: O(1)
    // Three inner loops: spaces, ascending chars, descending chars.
    // ============================================================
    static class BruteForce {
        public static void print(int n) {
            for (int i = 0; i < n; i++) {
                // leading spaces
                for (int j = 0; j < n - i - 1; j++) {
                    System.out.print(" ");
                }
                // ascending A to A+i
                for (int j = 0; j <= i; j++) {
                    System.out.print((char) ('A' + j));
                }
                // descending A+i-1 to A
                for (int j = i - 1; j >= 0; j--) {
                    System.out.print((char) ('A' + j));
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Symmetry with Single Inner Loop
    // Time: O(N^2)  |  Space: O(1)
    // Use distance from center to determine character.
    // ============================================================
    static class Optimal {
        public static void print(int n) {
            for (int i = 0; i < n; i++) {
                // leading spaces
                for (int j = 0; j < n - i - 1; j++) {
                    System.out.print(" ");
                }
                // 2*i+1 characters using distance from center
                for (int j = 0; j < 2 * i + 1; j++) {
                    int dist = Math.abs(i - j);
                    System.out.print((char) ('A' + i - dist));
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- StringBuilder, Single Print
    // Time: O(N^2)  |  Space: O(N^2)
    // Build entire pattern as one string, single I/O call.
    // ============================================================
    static class Best {
        public static void print(int n) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                // leading spaces
                for (int j = 0; j < n - i - 1; j++) {
                    sb.append(' ');
                }
                // ascending
                for (int j = 0; j <= i; j++) {
                    sb.append((char) ('A' + j));
                }
                // descending
                for (int j = i - 1; j >= 0; j--) {
                    sb.append((char) ('A' + j));
                }
                // trailing spaces (for centering)
                for (int j = 0; j < n - i - 1; j++) {
                    sb.append(' ');
                }
                if (i < n - 1) sb.append('\n');
            }
            System.out.println(sb.toString());
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("=== Pattern 17 - Alpha Hill ===");
        System.out.println("--- Brute Force ---");
        BruteForce.print(n);
        System.out.println("--- Optimal ---");
        Optimal.print(n);
        System.out.println("--- Best ---");
        Best.print(n);
    }
}
