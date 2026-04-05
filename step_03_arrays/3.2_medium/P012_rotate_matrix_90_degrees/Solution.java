import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(n^2)  |  Space: O(n^2)
// Copy to a new matrix: result[j][n-1-i] = original[i][j]
// (90-degree clockwise rotation formula)
// ============================================================
class BruteForce {
    public static int[][] solve(int[][] matrix) {
        int n = matrix.length;
        int[][] result = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                result[j][n - 1 - i] = matrix[i][j];
        return result;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL (Transpose + Reverse rows)
// Time: O(n^2)  |  Space: O(1)
// Step 1: Transpose (swap matrix[i][j] with matrix[j][i])
// Step 2: Reverse each row
// ============================================================
class Optimal {
    public static int[][] solve(int[][] matrix) {
        int n = matrix.length;
        // Transpose
        for (int i = 0; i < n; i++)
            for (int j = i + 1; j < n; j++) {
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        // Reverse each row
        for (int i = 0; i < n; i++) {
            int lo = 0, hi = n - 1;
            while (lo < hi) {
                int tmp = matrix[i][lo];
                matrix[i][lo] = matrix[i][hi];
                matrix[i][hi] = tmp;
                lo++; hi--;
            }
        }
        return matrix;
    }
}

// ============================================================
// APPROACH 3: BEST (4-way swap in cycles)
// Time: O(n^2)  |  Space: O(1)
// Process the matrix layer by layer (outer->inner).
// For each element in a layer, do a 4-way cyclic rotation.
// ============================================================
class Best {
    public static int[][] solve(int[][] matrix) {
        int n = matrix.length;
        for (int layer = 0; layer < n / 2; layer++) {
            int first = layer;
            int last = n - 1 - layer;
            for (int i = first; i < last; i++) {
                int offset = i - first;
                // Save top
                int top = matrix[first][i];
                // Left -> Top
                matrix[first][i] = matrix[last - offset][first];
                // Bottom -> Left
                matrix[last - offset][first] = matrix[last][last - offset];
                // Right -> Bottom
                matrix[last][last - offset] = matrix[i][last];
                // Top -> Right
                matrix[i][last] = top;
            }
        }
        return matrix;
    }
}

public class Solution {
    static void printMatrix(int[][] m) {
        for (int[] row : m) System.out.println("  " + Arrays.toString(row));
    }

    static int[][] copy(int[][] m) {
        int n = m.length;
        int[][] c = new int[n][n];
        for (int i = 0; i < n; i++) c[i] = m[i].clone();
        return c;
    }

    public static void main(String[] args) {
        System.out.println("=== Rotate Matrix 90 Degrees Clockwise ===");

        int[][] m1 = {{1,2,3},{4,5,6},{7,8,9}};
        int[][] m2 = {{5,1,9,11},{2,4,8,10},{13,3,6,7},{15,14,12,16}};

        for (int[][] m : new int[][][]{m1, m2}) {
            System.out.println("Input:");
            printMatrix(m);
            System.out.println("BruteForce:");
            printMatrix(BruteForce.solve(copy(m)));
            System.out.println("Optimal:");
            printMatrix(Optimal.solve(copy(m)));
            System.out.println("Best:");
            printMatrix(Best.solve(copy(m)));
            System.out.println();
        }
        // Expected for 3x3: [[7,4,1],[8,5,2],[9,6,3]]
    }
}
