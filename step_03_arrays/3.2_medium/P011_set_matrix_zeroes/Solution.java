/**
 * Problem: Set Matrix Zeroes
 * Difficulty: MEDIUM | XP: 25
 * LeetCode: 73
 *
 * Given an m x n integer matrix, if an element is 0, set its entire row
 * and column to 0. Do this in-place.
 *
 * @author DSA_Nova
 */
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(m*n*(m+n))  |  Space: O(m*n)
    // ============================================================
    /**
     * Copy the matrix. Scan the copy for zeros. For each zero found,
     * zero out the entire row and column in the original matrix.
     * The copy prevents cascade: if we zeroed the original while scanning,
     * newly introduced zeros would incorrectly trigger further zeroing.
     */
    static class BruteForce {
        public void setZeroes(int[][] matrix) {
            int m = matrix.length, n = matrix[0].length;
            int[][] copy = new int[m][n];
            for (int i = 0; i < m; i++)
                copy[i] = Arrays.copyOf(matrix[i], n);

            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (copy[i][j] == 0) {
                        for (int c = 0; c < n; c++) matrix[i][c] = 0;
                        for (int r = 0; r < m; r++) matrix[r][j] = 0;
                    }
                }
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Row/Column Boolean Sets
    // Time: O(m*n)  |  Space: O(m+n)
    // ============================================================
    /**
     * First pass: collect all row and column indices that contain zeros.
     * Second pass: zero out any cell whose row or column index is in those sets.
     */
    static class Optimal {
        public void setZeroes(int[][] matrix) {
            int m = matrix.length, n = matrix[0].length;
            Set<Integer> zeroRows = new HashSet<>();
            Set<Integer> zeroCols = new HashSet<>();

            for (int i = 0; i < m; i++)
                for (int j = 0; j < n; j++)
                    if (matrix[i][j] == 0) {
                        zeroRows.add(i);
                        zeroCols.add(j);
                    }

            for (int i = 0; i < m; i++)
                for (int j = 0; j < n; j++)
                    if (zeroRows.contains(i) || zeroCols.contains(j))
                        matrix[i][j] = 0;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — In-Place O(1) Space (First Row/Col as Markers)
    // Time: O(m*n)  |  Space: O(1)
    // ============================================================
    /**
     * Use matrix[0][j] as the marker for column j, and matrix[i][0] as the
     * marker for row i. Two extra booleans track whether the first row and
     * first column themselves originally contained zeros (since we overwrite them).
     *
     * Steps:
     * 1. Check if row 0 or col 0 has any original zeros.
     * 2. Scan rows 1..m-1, cols 1..n-1: if matrix[i][j]==0, set matrix[i][0]=0 and matrix[0][j]=0.
     * 3. Use markers to zero out rows 1..m-1 and cols 1..n-1.
     * 4. Apply the original first-row/first-col zero flags.
     */
    static class Best {
        public void setZeroes(int[][] matrix) {
            int m = matrix.length, n = matrix[0].length;

            boolean firstRowZero = false, firstColZero = false;
            for (int j = 0; j < n; j++) if (matrix[0][j] == 0) firstRowZero = true;
            for (int i = 0; i < m; i++) if (matrix[i][0] == 0) firstColZero = true;

            // Use first row/col as markers
            for (int i = 1; i < m; i++)
                for (int j = 1; j < n; j++)
                    if (matrix[i][j] == 0) {
                        matrix[i][0] = 0;
                        matrix[0][j] = 0;
                    }

            // Zero out based on markers
            for (int i = 1; i < m; i++)
                for (int j = 1; j < n; j++)
                    if (matrix[i][0] == 0 || matrix[0][j] == 0)
                        matrix[i][j] = 0;

            // Handle first row
            if (firstRowZero)
                for (int j = 0; j < n; j++) matrix[0][j] = 0;

            // Handle first column
            if (firstColZero)
                for (int i = 0; i < m; i++) matrix[i][0] = 0;
        }
    }

    static void printMatrix(int[][] m) {
        for (int[] row : m) System.out.println("  " + Arrays.toString(row));
    }

    static int[][] deepCopy(int[][] m) {
        int[][] copy = new int[m.length][];
        for (int i = 0; i < m.length; i++) copy[i] = Arrays.copyOf(m[i], m[i].length);
        return copy;
    }

    public static void main(String[] args) {
        System.out.println("=== Set Matrix Zeroes ===");

        int[][] matrix1 = {{1,1,1},{1,0,1},{1,1,1}};
        System.out.println("Input:"); printMatrix(matrix1);
        int[][] m1a = deepCopy(matrix1), m1b = deepCopy(matrix1), m1c = deepCopy(matrix1);
        new BruteForce().setZeroes(m1a); System.out.println("Brute:");   printMatrix(m1a);
        new Optimal().setZeroes(m1b);    System.out.println("Optimal:"); printMatrix(m1b);
        new Best().setZeroes(m1c);       System.out.println("Best:");    printMatrix(m1c);

        int[][] matrix2 = {{0,1,2,0},{3,4,5,2},{1,3,1,5}};
        System.out.println("\nInput:"); printMatrix(matrix2);
        int[][] m2a = deepCopy(matrix2), m2b = deepCopy(matrix2), m2c = deepCopy(matrix2);
        new BruteForce().setZeroes(m2a); System.out.println("Brute:");   printMatrix(m2a);
        new Optimal().setZeroes(m2b);    System.out.println("Optimal:"); printMatrix(m2b);
        new Best().setZeroes(m2c);       System.out.println("Best:");    printMatrix(m2c);
    }
}
