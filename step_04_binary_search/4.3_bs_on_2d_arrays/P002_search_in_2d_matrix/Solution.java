/**
 * Problem: Search in 2D Matrix
 * Difficulty: MEDIUM | XP: 25
 * LeetCode #74
 *
 * Key Insight: The matrix is a flattened sorted array.
 * Use virtual index: row = idx / n, col = idx % n.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Linear Scan
    // Time: O(m * n)  |  Space: O(1)
    //
    // Check every element.
    // ============================================================
    public static boolean bruteForce(int[][] matrix, int target) {
        for (int[] row : matrix) {
            for (int val : row) {
                if (val == target) return true;
            }
        }
        return false;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Two Binary Searches
    // Time: O(log m + log n)  |  Space: O(1)
    //
    // Step 1: Binary search to find the correct row.
    // Step 2: Binary search within that row.
    // ============================================================
    public static boolean optimal(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;

        // Step 1: Find the row where target could reside
        int lo = 0, hi = m - 1;
        int targetRow = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (target < matrix[mid][0]) {
                hi = mid - 1;
            } else if (target > matrix[mid][n - 1]) {
                lo = mid + 1;
            } else {
                targetRow = mid;
                break;
            }
        }
        if (targetRow == -1) return false;

        // Step 2: Binary search within the row
        lo = 0;
        hi = n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (matrix[targetRow][mid] == target) return true;
            if (matrix[targetRow][mid] < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return false;
    }

    // ============================================================
    // APPROACH 3: BEST -- Single Binary Search (Flattened)
    // Time: O(log(m * n))  |  Space: O(1)
    //
    // Treat matrix as 1D sorted array. Map virtual index to 2D.
    // ============================================================
    public static boolean best(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        int lo = 0, hi = m * n - 1;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int row = mid / n;
            int col = mid % n;
            int val = matrix[row][col];

            if (val == target) return true;
            if (val < target) lo = mid + 1;
            else hi = mid - 1;
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println("=== Search in 2D Matrix ===");

        int[][] matrix = {
            {1, 3, 5, 7},
            {10, 11, 16, 20},
            {23, 30, 34, 60}
        };
        int[] targets = {3, 13, 1, 60, 23, 100};

        System.out.println("Matrix:");
        for (int[] row : matrix) {
            System.out.println("  " + java.util.Arrays.toString(row));
        }
        System.out.println();

        for (int t : targets) {
            System.out.printf("Target=%d%n", t);
            System.out.printf("  Brute:   %b%n", bruteForce(matrix, t));
            System.out.printf("  Optimal: %b%n", optimal(matrix, t));
            System.out.printf("  Best:    %b%n", best(matrix, t));
            System.out.println();
        }
    }
}
