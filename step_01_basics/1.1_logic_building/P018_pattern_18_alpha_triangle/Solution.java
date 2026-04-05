/**
 * Problem: Pattern 18 - Alpha Triangle
 * Difficulty: EASY | XP: 10
 *
 * Print reverse alpha triangle. For n=5 (E):
 * E
 * DE
 * CDE
 * BCDE
 * ABCDE
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Explicit Nested Loops
    // Time: O(N^2)  |  Space: O(1)
    // For each row i, determine start character and print ascending.
    // ============================================================
    static class BruteForce {
        public static void print(int n) {
            for (int i = 0; i < n; i++) {
                int start = n - 1 - i; // offset from 'A'
                for (int j = 0; j <= i; j++) {
                    System.out.print((char) ('A' + start + j));
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Direct Character Range
    // Time: O(N^2)  |  Space: O(1)
    // Row i prints from (endChar - i) to endChar.
    // ============================================================
    static class Optimal {
        public static void print(int n) {
            char endChar = (char) ('A' + n - 1);
            for (int i = 0; i < n; i++) {
                char startChar = (char) (endChar - i);
                for (char ch = startChar; ch <= endChar; ch++) {
                    System.out.print(ch);
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- StringBuilder, Single Print
    // Time: O(N^2)  |  Space: O(N^2)
    // Pre-build alphabet string, substring for each row.
    // ============================================================
    static class Best {
        public static void print(int n) {
            // Build "ABCDE..."
            StringBuilder alpha = new StringBuilder();
            for (int i = 0; i < n; i++) {
                alpha.append((char) ('A' + i));
            }
            String alphaStr = alpha.toString();

            StringBuilder result = new StringBuilder();
            for (int i = 0; i < n; i++) {
                result.append(alphaStr.substring(n - 1 - i));
                if (i < n - 1) result.append('\n');
            }
            System.out.println(result.toString());
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("=== Pattern 18 - Alpha Triangle ===");
        System.out.println("--- Brute Force ---");
        BruteForce.print(n);
        System.out.println("--- Optimal ---");
        Optimal.print(n);
        System.out.println("--- Best ---");
        Best.print(n);
    }
}
