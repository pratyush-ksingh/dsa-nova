/**
 * Problem: Pattern 3 - Right Angled Number Triangle
 * Difficulty: EASY | XP: 10
 *
 * Print a right-angled triangle where row i has i copies of i.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: NESTED LOOPS (Standard)
    // Time: O(N^2)  |  Space: O(1)
    // Outer loop for rows, inner loop prints row number i times.
    // ============================================================
    static class NestedLoops {
        public static void printPattern(int n) {
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= i; j++) {
                    System.out.print(i);
                    if (j < i) System.out.print(" ");
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: STRING REPEAT (StringBuilder)
    // Time: O(N^2)  |  Space: O(N) for current row string
    // Build each row as a string using StringBuilder, then print.
    // ============================================================
    static class StringRepeat {
        public static void printPattern(int n) {
            for (int i = 1; i <= n; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 1; j <= i; j++) {
                    if (j > 1) sb.append(" ");
                    sb.append(i);
                }
                System.out.println(sb.toString());
            }
        }
    }

    public static void main(String[] args) {
        int[] tests = {1, 3, 5};

        for (int n : tests) {
            System.out.println("=== Pattern 3: N = " + n + " ===");

            System.out.println("--- Nested Loops ---");
            NestedLoops.printPattern(n);

            System.out.println("--- String Repeat ---");
            StringRepeat.printPattern(n);

            System.out.println();
        }
    }
}
