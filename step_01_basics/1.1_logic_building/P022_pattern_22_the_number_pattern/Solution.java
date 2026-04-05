/**
 * Problem: Pattern 22 - The Number Pattern
 * Difficulty: MEDIUM | XP: 15
 *
 * For n=4, print a (2n-1)x(2n-1) grid:
 *   4444444
 *   4333334
 *   4322234
 *   4321234
 *   4322234
 *   4333334
 *   4444444
 *
 * Cell value = n - min(distTop, distBottom, distLeft, distRight)
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Four Explicit Distance Variables
    // Time: O(n^2)  |  Space: O(1)
    // Compute each of the 4 border distances explicitly per cell.
    // ============================================================
    static class BruteForce {
        public static void printPattern(int n) {
            int size = 2 * n - 1;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int distTop    = i;
                    int distBottom = size - 1 - i;
                    int distLeft   = j;
                    int distRight  = size - 1 - j;
                    int minDist = Math.min(
                        Math.min(distTop, distBottom),
                        Math.min(distLeft, distRight)
                    );
                    System.out.print(n - minDist);
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- StringBuilder per Row
    // Time: O(n^2)  |  Space: O(n)
    // Same formula; collect digits in a StringBuilder per row.
    // ============================================================
    static class Optimal {
        public static void printPattern(int n) {
            int size = 2 * n - 1;
            for (int i = 0; i < size; i++) {
                StringBuilder row = new StringBuilder();
                for (int j = 0; j < size; j++) {
                    int minDist = Math.min(
                        Math.min(i, size - 1 - i),
                        Math.min(j, size - 1 - j)
                    );
                    row.append(n - minDist);
                }
                System.out.println(row);
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST -- Full Grid in StringBuilder, Single Print
    // Time: O(n^2)  |  Space: O(n^2)
    // Build entire grid string, print in one call.
    // ============================================================
    static class Best {
        public static void printPattern(int n) {
            int size = 2 * n - 1;
            StringBuilder grid = new StringBuilder();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    int minDist = Math.min(
                        Math.min(i, size - 1 - i),
                        Math.min(j, size - 1 - j)
                    );
                    grid.append(n - minDist);
                }
                if (i < size - 1) grid.append("\n");
            }
            System.out.println(grid);
        }
    }

    public static void main(String[] args) {
        int n = 4;
        System.out.println("=== Pattern 22 - The Number Pattern ===");
        System.out.println("--- Brute Force ---");
        BruteForce.printPattern(n);
        System.out.println("--- Optimal ---");
        Optimal.printPattern(n);
        System.out.println("--- Best ---");
        Best.printPattern(n);
    }
}
