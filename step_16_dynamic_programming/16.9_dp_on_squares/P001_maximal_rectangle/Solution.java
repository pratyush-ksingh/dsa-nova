/**
 * Problem: Maximal Rectangle  (LeetCode 85)
 * Difficulty: HARD | XP: 50
 *
 * Given a rows x cols binary matrix filled with '0's and '1's, find the
 * largest rectangle containing only 1s and return its area.
 *
 * Key insight: For each row, build a histogram of consecutive 1s above
 * (including the current row). The answer is the max rectangle in any
 * of these histograms. Histogram problem solved in O(n) with a monotonic stack.
 *
 * @author DSA_Nova
 */
import java.util.Stack;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(m^2 * n^2)  |  Space: O(1)
    // ============================================================
    static class BruteForce {
        /**
         * For each top-left corner (r1, c1), extend downward row by row.
         * Track the minimum width available at each depth; update max area.
         * If a cell in the current row is '0', the width shrinks to 0 and
         * we can stop extending downward for this top-left corner.
         */
        static int maximalRectangle(char[][] matrix) {
            if (matrix == null || matrix.length == 0) return 0;
            int m = matrix.length, n = matrix[0].length;
            int maxArea = 0;

            for (int r1 = 0; r1 < m; r1++) {
                for (int c1 = 0; c1 < n; c1++) {
                    if (matrix[r1][c1] == '0') continue;
                    int minWidth = n;
                    for (int r2 = r1; r2 < m; r2++) {
                        // Width available in row r2 starting from c1
                        int width = 0;
                        for (int c = c1; c < n; c++) {
                            if (matrix[r2][c] == '1') width++;
                            else break;
                        }
                        minWidth = Math.min(minWidth, width);
                        if (minWidth == 0) break;
                        int height = r2 - r1 + 1;
                        maxArea = Math.max(maxArea, height * minWidth);
                    }
                }
            }
            return maxArea;
        }
    }

    // ============================================================
    // HELPER: Largest Rectangle in Histogram (monotonic stack, O(n))
    // ============================================================
    static int largestRectInHistogram(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int maxArea = 0;
        int n = heights.length;

        for (int i = 0; i <= n; i++) {
            int h = (i == n) ? 0 : heights[i];   // sentinel 0 at end
            while (!stack.isEmpty() && heights[stack.peek()] > h) {
                int height = heights[stack.pop()];
                int width  = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxArea = Math.max(maxArea, height * width);
            }
            stack.push(i);
        }
        return maxArea;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (Row-by-row histogram + monotonic stack)
    // Time: O(m * n)  |  Space: O(n)
    // ============================================================
    static class Optimal {
        /**
         * Build heights[] row by row: heights[c] = consecutive 1s ending at
         * current row in column c (reset to 0 if current cell is '0').
         * Apply Largest Rectangle in Histogram on heights[] after each row update.
         */
        static int maximalRectangle(char[][] matrix) {
            if (matrix == null || matrix.length == 0) return 0;
            int m = matrix.length, n = matrix[0].length;
            int[] heights = new int[n];
            int maxArea = 0;

            for (int r = 0; r < m; r++) {
                for (int c = 0; c < n; c++) {
                    heights[c] = (matrix[r][c] == '1') ? heights[c] + 1 : 0;
                }
                maxArea = Math.max(maxArea, largestRectInHistogram(heights));
            }
            return maxArea;
        }
    }

    // ============================================================
    // APPROACH 3: BEST (DP with left/right boundary arrays)
    // Time: O(m * n)  |  Space: O(n)
    // ============================================================
    static class Best {
        /**
         * Pure DP without an explicit stack. Maintain three arrays:
         *   height[c] = consecutive 1s above and including row r at column c
         *   left[c]   = leftmost column index reachable at height >= height[c]
         *   right[c]  = rightmost column index + 1 reachable at height >= height[c]
         * Area for each cell = height[c] * (right[c] - left[c]).
         *
         * left[] and right[] carry over from the previous row and are tightened
         * by the current row's 0 positions.
         */
        static int maximalRectangle(char[][] matrix) {
            if (matrix == null || matrix.length == 0) return 0;
            int m = matrix.length, n = matrix[0].length;
            int[] height = new int[n];
            int[] left   = new int[n];
            int[] right  = new int[n];
            java.util.Arrays.fill(right, n);
            int maxArea = 0;

            for (int r = 0; r < m; r++) {
                // Update height
                for (int c = 0; c < n; c++) {
                    height[c] = (matrix[r][c] == '1') ? height[c] + 1 : 0;
                }

                // Update left boundary (scan left to right)
                int curLeft = 0;
                for (int c = 0; c < n; c++) {
                    if (matrix[r][c] == '1') {
                        left[c] = Math.max(left[c], curLeft);
                    } else {
                        left[c] = 0;
                        curLeft = c + 1;
                    }
                }

                // Update right boundary (scan right to left)
                int curRight = n;
                for (int c = n - 1; c >= 0; c--) {
                    if (matrix[r][c] == '1') {
                        right[c] = Math.min(right[c], curRight);
                    } else {
                        right[c] = n;
                        curRight = c;
                    }
                }

                // Compute max area for this row
                for (int c = 0; c < n; c++) {
                    maxArea = Math.max(maxArea, height[c] * (right[c] - left[c]));
                }
            }
            return maxArea;
        }
    }

    // ============================================================
    // MAIN — run all approaches on test cases
    // ============================================================
    public static void main(String[] args) {
        char[][][] tests = {
            {{'1','0','1','0','0'},
             {'1','0','1','1','1'},
             {'1','1','1','1','1'},
             {'1','0','0','1','0'}},
            {{'0'}},
            {{'1'}},
            {{'0','1'},{'1','0'}},
            {{'1','1','1','1'},
             {'1','1','1','1'},
             {'1','1','1','1'}}
        };
        int[] expected = {6, 0, 1, 1, 12};

        System.out.println("=== Maximal Rectangle ===\n");
        for (int t = 0; t < tests.length; t++) {
            int b  = BruteForce.maximalRectangle(tests[t]);
            int o  = Optimal.maximalRectangle(tests[t]);
            int be = Best.maximalRectangle(tests[t]);
            String status = (b == o && o == be && be == expected[t]) ? "PASS" : "FAIL";
            System.out.printf("Test %d:  Brute=%d  Optimal=%d  Best=%d  Expected=%d  [%s]%n",
                              t+1, b, o, be, expected[t], status);
        }
    }
}
