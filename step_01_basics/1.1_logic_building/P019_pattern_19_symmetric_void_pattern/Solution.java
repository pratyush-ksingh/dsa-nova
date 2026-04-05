/**
 * Problem: Pattern 19 - Symmetric Void Pattern
 * Difficulty: EASY | XP: 10
 *
 * Print symmetric void (hourglass of spaces between stars). For n=5:
 * **********
 * ****  ****
 * ***    ***
 * **      **
 * *        *
 * *        *
 * **      **
 * ***    ***
 * ****  ****
 * **********
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Separate Top and Bottom Halves
    // Time: O(N^2)  |  Space: O(1)
    // Three inner loops per row: left stars, spaces, right stars.
    // ============================================================
    static class BruteForce {
        public static void print(int n) {
            // Upper half
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n - i; j++) System.out.print("*");
                for (int j = 0; j < 2 * i; j++) System.out.print(" ");
                for (int j = 0; j < n - i; j++) System.out.print("*");
                System.out.println();
            }
            // Lower half
            for (int i = 0; i < n; i++) {
                for (int j = 0; j <= i; j++) System.out.print("*");
                for (int j = 0; j < 2 * (n - 1 - i); j++) System.out.print(" ");
                for (int j = 0; j <= i; j++) System.out.print("*");
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Unified Loop with Conditional
    // Time: O(N^2)  |  Space: O(1)
    // Single loop of 2*n rows, compute stars/spaces from index.
    // ============================================================
    static class Optimal {
        public static void print(int n) {
            for (int i = 0; i < 2 * n; i++) {
                int stars, spaces;
                if (i < n) {
                    stars = n - i;
                    spaces = 2 * i;
                } else {
                    stars = i - n + 1;
                    spaces = 2 * (2 * n - 1 - i);
                }
                for (int j = 0; j < stars; j++) System.out.print("*");
                for (int j = 0; j < spaces; j++) System.out.print(" ");
                for (int j = 0; j < stars; j++) System.out.print("*");
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- StringBuilder, Single Print
    // Time: O(N^2)  |  Space: O(N^2)
    // Build entire pattern, single I/O call.
    // ============================================================
    static class Best {
        public static void print(int n) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 2 * n; i++) {
                int stars, spaces;
                if (i < n) {
                    stars = n - i;
                    spaces = 2 * i;
                } else {
                    stars = i - n + 1;
                    spaces = 2 * (2 * n - 1 - i);
                }
                for (int j = 0; j < stars; j++) sb.append('*');
                for (int j = 0; j < spaces; j++) sb.append(' ');
                for (int j = 0; j < stars; j++) sb.append('*');
                if (i < 2 * n - 1) sb.append('\n');
            }
            System.out.println(sb.toString());
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("=== Pattern 19 - Symmetric Void Pattern ===");
        System.out.println("--- Brute Force ---");
        BruteForce.print(n);
        System.out.println("--- Optimal ---");
        Optimal.print(n);
        System.out.println("--- Best ---");
        Best.print(n);
    }
}
