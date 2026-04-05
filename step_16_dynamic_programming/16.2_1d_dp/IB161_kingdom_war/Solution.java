import java.util.*;

/**
 * Problem: Kingdom War
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a 2D matrix where values can be negative, find the maximum sum sub-rectangle.
 * Extension of Kadane's algorithm to 2D.
 *
 * Note: InterviewBit's "Kingdom War" asks for max sum submatrix where
 * rows and cols are treated as contributions (can contain negative).
 *
 * @author DSA_Nova
 */

// ============================================================
// APPROACH 1: BRUTE FORCE
// O(N^4) brute force — try all top-left and bottom-right pairs
// Time: O(N^2 * M^2)  |  Space: O(1)
// ============================================================
class BruteForce {
    static int maxSumSubmatrix(int[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length;
        int maxSum = Integer.MIN_VALUE;
        for (int r1 = 0; r1 < rows; r1++) {
            for (int r2 = r1; r2 < rows; r2++) {
                for (int c1 = 0; c1 < cols; c1++) {
                    for (int c2 = c1; c2 < cols; c2++) {
                        int sum = 0;
                        for (int r = r1; r <= r2; r++)
                            for (int c = c1; c <= c2; c++)
                                sum += matrix[r][c];
                        maxSum = Math.max(maxSum, sum);
                    }
                }
            }
        }
        return maxSum;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Fix top and bottom rows; collapse columns to 1D array; apply Kadane's
// Time: O(N^2 * M)  |  Space: O(M)
// ============================================================
class Optimal {
    static int kadane(int[] arr) {
        int maxSum = arr[0], currSum = arr[0];
        for (int i = 1; i < arr.length; i++) {
            currSum = Math.max(arr[i], currSum + arr[i]);
            maxSum = Math.max(maxSum, currSum);
        }
        return maxSum;
    }

    static int maxSumSubmatrix(int[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length;
        int maxSum = Integer.MIN_VALUE;

        for (int top = 0; top < rows; top++) {
            int[] colSum = new int[cols];
            for (int bottom = top; bottom < rows; bottom++) {
                for (int c = 0; c < cols; c++) colSum[c] += matrix[bottom][c];
                maxSum = Math.max(maxSum, kadane(colSum));
            }
        }
        return maxSum;
    }
}

// ============================================================
// APPROACH 3: BEST
// Same as Optimal but transpose if more rows than columns (iterate over shorter dimension)
// Time: O(min(N,M)^2 * max(N,M))  |  Space: O(max(N,M))
// ============================================================
class Best {
    static int maxSumSubmatrix(int[][] matrix) {
        int rows = matrix.length, cols = matrix[0].length;
        // Transpose if rows > cols for better cache performance
        if (rows < cols) return Optimal.maxSumSubmatrix(matrix);

        int[][] transposed = new int[cols][rows];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                transposed[c][r] = matrix[r][c];

        return Optimal.maxSumSubmatrix(transposed);
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Kingdom War ===");

        int[][] m1 = {
            {1, 2, -1, -4, -20},
            {-8, -3, 4, 2, 1},
            {3, 8, 10, 1, 3},
            {-4, -1, 1, 7, -6}
        };
        System.out.println("BruteForce: " + BruteForce.maxSumSubmatrix(m1));  // 29
        System.out.println("Optimal:    " + Optimal.maxSumSubmatrix(m1));     // 29
        System.out.println("Best:       " + Best.maxSumSubmatrix(m1));        // 29

        int[][] m2 = {{-1, -2}, {-3, -4}};
        System.out.println("All negative BruteForce: " + BruteForce.maxSumSubmatrix(m2)); // -1
        System.out.println("All negative Optimal:    " + Optimal.maxSumSubmatrix(m2));    // -1

        int[][] m3 = {{2, 1}, {-1, 3}};
        System.out.println("2x2 Optimal: " + Optimal.maxSumSubmatrix(m3));  // 5
    }
}
