/**
 * Problem: Maximal Rectangle
 * Difficulty: HARD | XP: 50
 *
 * Given a binary matrix filled with '0' and '1', find the largest rectangle
 * containing only '1's and return its area.
 *
 * @author DSA_Nova
 */
import java.util.Stack;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE - O(M^2 * N^2) via every sub-rectangle
    // Time: O(M^2 * N^2)  |  Space: O(MN) prefix sums
    // Use row prefix sums to check if a rectangle is all-1s.
    // ============================================================
    public static int bruteForce(char[][] matrix) {
        if (matrix.length == 0) return 0;
        int m = matrix.length, n = matrix[0].length;
        // row prefix sums: rowSum[i][j] = # consecutive 1s ending at (i,j)
        int[][] rs = new int[m][n];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                rs[i][j] = (matrix[i][j] == '0') ? 0 : (j == 0 ? 1 : rs[i][j-1] + 1);

        int maxArea = 0;
        for (int r2 = 0; r2 < m; r2++)
            for (int c2 = 0; c2 < n; c2++)
                for (int r1 = r2; r1 >= 0; r1--) {
                    int minW = rs[r1][c2];
                    for (int r = r1 + 1; r <= r2; r++) minW = Math.min(minW, rs[r][c2]);
                    maxArea = Math.max(maxArea, minW * (r2 - r1 + 1));
                }
        return maxArea;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL - Build histogram per row + Largest Rect in Histogram
    // Time: O(M * N)  |  Space: O(N)
    // For each row, compute cumulative heights of 1s above (histogram).
    // Apply the O(N) monotonic stack histogram solution row by row.
    // ============================================================
    public static int optimal(char[][] matrix) {
        if (matrix.length == 0) return 0;
        int m = matrix.length, n = matrix[0].length;
        int[] heights = new int[n];
        int maxArea = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++)
                heights[j] = (matrix[i][j] == '0') ? 0 : heights[j] + 1;
            maxArea = Math.max(maxArea, largestRectHistogram(heights));
        }
        return maxArea;
    }

    private static int largestRectHistogram(int[] heights) {
        Stack<Integer> stack = new Stack<>();
        int max = 0, n = heights.length;
        for (int i = 0; i <= n; i++) {
            int h = (i == n) ? 0 : heights[i];
            while (!stack.isEmpty() && heights[stack.peek()] > h) {
                int ht = heights[stack.pop()];
                int w = stack.isEmpty() ? i : i - stack.peek() - 1;
                max = Math.max(max, ht * w);
            }
            stack.push(i);
        }
        return max;
    }

    // ============================================================
    // APPROACH 3: BEST - DP approach with height/left/right arrays
    // Time: O(M * N)  |  Space: O(N)
    // For each row maintain: height[j], left[j] (leftmost valid col), right[j].
    // Rectangle at (i,j) = height[j] * (right[j] - left[j]).
    // ============================================================
    public static int best(char[][] matrix) {
        if (matrix.length == 0) return 0;
        int m = matrix.length, n = matrix[0].length;
        int[] height = new int[n];
        int[] left = new int[n];
        int[] right = new int[n];
        java.util.Arrays.fill(right, n);
        int maxArea = 0;

        for (int i = 0; i < m; i++) {
            // Update height
            for (int j = 0; j < n; j++)
                height[j] = (matrix[i][j] == '0') ? 0 : height[j] + 1;

            // Update left boundary
            int curLeft = 0;
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == '1') left[j] = Math.max(left[j], curLeft);
                else { left[j] = 0; curLeft = j + 1; }
            }

            // Update right boundary
            int curRight = n;
            for (int j = n - 1; j >= 0; j--) {
                if (matrix[i][j] == '1') right[j] = Math.min(right[j], curRight);
                else { right[j] = n; curRight = j; }
            }

            // Compute max area
            for (int j = 0; j < n; j++)
                maxArea = Math.max(maxArea, height[j] * (right[j] - left[j]));
        }
        return maxArea;
    }

    public static void main(String[] args) {
        System.out.println("=== Maximal Rectangle ===");

        char[][] mat1 = {
            {'1','0','1','0','0'},
            {'1','0','1','1','1'},
            {'1','1','1','1','1'},
            {'1','0','0','1','0'}
        };  // expected: 6

        char[][] mat2 = {{'0'}};          // expected: 0
        char[][] mat3 = {{'1'}};          // expected: 1
        char[][] mat4 = {{'1','1','1','1'},{'1','1','1','1'}}; // expected: 8

        System.out.println("mat1: brute=" + bruteForce(mat1) + " opt=" + optimal(mat1) + " best=" + best(mat1)); // 6
        System.out.println("mat2: brute=" + bruteForce(mat2) + " opt=" + optimal(mat2) + " best=" + best(mat2)); // 0
        System.out.println("mat3: brute=" + bruteForce(mat3) + " opt=" + optimal(mat3) + " best=" + best(mat3)); // 1
        System.out.println("mat4: brute=" + bruteForce(mat4) + " opt=" + optimal(mat4) + " best=" + best(mat4)); // 8
    }
}
