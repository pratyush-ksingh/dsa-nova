/**
 * Problem: Pattern 9 - Diamond Star Pattern
 * Difficulty: EASY | XP: 10
 *
 * Print a full diamond: upright pyramid (N rows) + inverted pyramid (N rows).
 * Total 2*N rows. Widest row has (2*N - 1) stars.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Two Separate Pyramid Loops
    // Time: O(N^2)  |  Space: O(1)
    // Print upper half, then lower half, each with nested loops.
    // ============================================================
    static class BruteForce {
        public static void printPattern(int n) {
            // Upper half: upright pyramid
            for (int i = 0; i < n; i++) {
                for (int s = 0; s < n - 1 - i; s++) {
                    System.out.print(" ");
                }
                for (int j = 0; j < 2 * i + 1; j++) {
                    if (j > 0) System.out.print(" ");
                    System.out.print("*");
                }
                System.out.println();
            }
            // Lower half: inverted pyramid
            for (int i = 0; i < n; i++) {
                for (int s = 0; s < i; s++) {
                    System.out.print(" ");
                }
                int starCount = 2 * (n - i) - 1;
                for (int j = 0; j < starCount; j++) {
                    if (j > 0) System.out.print(" ");
                    System.out.print("*");
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Single Loop with Conditional
    // Time: O(N^2)  |  Space: O(N)
    // One loop over 2N rows, conditional formula for each half.
    // ============================================================
    static class Optimal {
        public static void printPattern(int n) {
            for (int i = 0; i < 2 * n; i++) {
                int spaces, stars;
                if (i < n) {
                    spaces = n - 1 - i;
                    stars = 2 * i + 1;
                } else {
                    int j = i - n;
                    spaces = j;
                    stars = 2 * (n - j) - 1;
                }
                StringBuilder row = new StringBuilder();
                for (int s = 0; s < spaces; s++) {
                    row.append(" ");
                }
                for (int k = 0; k < stars; k++) {
                    if (k > 0) row.append(" ");
                    row.append("*");
                }
                System.out.println(row);
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Full Grid String, Single Print
    // Time: O(N^2)  |  Space: O(N^2)
    // Build entire diamond as one string, single I/O call.
    // ============================================================
    static class Best {
        public static void printPattern(int n) {
            StringBuilder grid = new StringBuilder();
            for (int i = 0; i < 2 * n; i++) {
                if (i > 0) grid.append("\n");
                int spaces, stars;
                if (i < n) {
                    spaces = n - 1 - i;
                    stars = 2 * i + 1;
                } else {
                    int j = i - n;
                    spaces = j;
                    stars = 2 * (n - j) - 1;
                }
                for (int s = 0; s < spaces; s++) {
                    grid.append(" ");
                }
                for (int k = 0; k < stars; k++) {
                    if (k > 0) grid.append(" ");
                    grid.append("*");
                }
            }
            System.out.println(grid);
        }
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("=== Pattern 9 - Diamond Star Pattern ===");

        System.out.println("--- Brute Force (N=5) ---");
        BruteForce.printPattern(n);

        System.out.println("--- Optimal (N=5) ---");
        Optimal.printPattern(n);

        System.out.println("--- Best (N=5) ---");
        Best.printPattern(n);

        // Edge case
        System.out.println("--- N=1 ---");
        Best.printPattern(1);

        System.out.println("--- N=3 ---");
        Best.printPattern(3);
    }
}
