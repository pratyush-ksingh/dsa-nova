/**
 * Problem: Search a 2D Matrix II (LeetCode #240)
 * Difficulty: MEDIUM | XP: 25
 *
 * Each row sorted left to right. Each column sorted top to bottom.
 * Search for a target value.
 *
 * @author DSA_Nova
 */

// ============================================================
// Approach 1: Brute Force (Linear Scan)
// Time: O(m * n) | Space: O(1)
// ============================================================
class BruteForce {
    public static boolean searchMatrix(int[][] matrix, int target) {
        for (int[] row : matrix) {
            for (int val : row) {
                if (val == target) return true;
            }
        }
        return false;
    }
}

// ============================================================
// Approach 2: Optimal (Staircase / Top-Right Corner Search)
// Time: O(m + n) | Space: O(1)
// ============================================================
class Optimal {
    public static boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return false;

        int m = matrix.length, n = matrix[0].length;
        int row = 0, col = n - 1; // start at top-right

        while (row < m && col >= 0) {
            if (matrix[row][col] == target) {
                return true;
            } else if (matrix[row][col] > target) {
                col--;  // current too large, eliminate this column
            } else {
                row++;  // current too small, eliminate this row
            }
        }
        return false;
    }
}

// ============================================================
// Approach 3: Best (Binary Search Per Row)
// Time: O(m * log(n)) | Space: O(1)
// ============================================================
class Best {
    public static boolean searchMatrix(int[][] matrix, int target) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return false;

        int m = matrix.length, n = matrix[0].length;

        for (int[] row : matrix) {
            // Early skip: if target < first element, all later rows are larger
            if (row[0] > target) break;
            // Skip row if target > last element
            if (row[n - 1] < target) continue;

            // Binary search in this row
            int lo = 0, hi = n - 1;
            while (lo <= hi) {
                int mid = lo + (hi - lo) / 2;
                if (row[mid] == target) return true;
                else if (row[mid] < target) lo = mid + 1;
                else hi = mid - 1;
            }
        }
        return false;
    }
}

// Main driver
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Search a 2D Matrix II ===\n");

        int[][] matrix = {
            {1,  4,  7, 11, 15},
            {2,  5,  8, 12, 19},
            {3,  6,  9, 16, 22},
            {10, 13, 14, 17, 24},
            {18, 21, 23, 26, 30}
        };

        int[] targets = {5, 20, 1, 30, 0};
        boolean[] expected = {true, false, true, true, false};

        for (int t = 0; t < targets.length; t++) {
            boolean b = BruteForce.searchMatrix(matrix, targets[t]);
            boolean o = Optimal.searchMatrix(matrix, targets[t]);
            boolean n = Best.searchMatrix(matrix, targets[t]);
            boolean pass = (b == expected[t] && o == expected[t] && n == expected[t]);

            System.out.println("Target:   " + targets[t]);
            System.out.println("Brute:    " + b);
            System.out.println("Optimal:  " + o);
            System.out.println("Best:     " + n);
            System.out.println("Expected: " + expected[t]);
            System.out.println("Pass:     " + pass);
            System.out.println();
        }
    }
}
