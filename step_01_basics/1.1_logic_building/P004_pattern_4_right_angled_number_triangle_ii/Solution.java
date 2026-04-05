/**
 * Problem: Pattern 4 - Right Angled Number Triangle II
 * Difficulty: EASY | XP: 10
 *
 * Print a right-angled triangle with sequential numbers 1, 2, 3, ... across rows.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: NESTED LOOPS WITH RUNNING COUNTER
    // Time: O(N^2)  |  Space: O(1)
    // Maintain a counter that increments on every print.
    // ============================================================
    static class RunningCounter {
        public static void printPattern(int n) {
            int num = 1;
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= i; j++) {
                    System.out.print(num);
                    if (j < i) System.out.print(" ");
                    num++;
                }
                System.out.println();
            }
        }
    }

    // ============================================================
    // APPROACH 2: FORMULA-BASED (Compute Start per Row)
    // Time: O(N^2)  |  Space: O(1)
    // Row i starts at i*(i-1)/2 + 1. No running counter needed.
    // ============================================================
    static class FormulaBased {
        public static void printPattern(int n) {
            for (int i = 1; i <= n; i++) {
                int start = i * (i - 1) / 2 + 1;
                for (int j = 0; j < i; j++) {
                    if (j > 0) System.out.print(" ");
                    System.out.print(start + j);
                }
                System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        int[] tests = {1, 3, 5};

        for (int n : tests) {
            System.out.println("=== Pattern 4: N = " + n + " ===");

            System.out.println("--- Running Counter ---");
            RunningCounter.printPattern(n);

            System.out.println("--- Formula Based ---");
            FormulaBased.printPattern(n);

            System.out.println();
        }
    }
}
