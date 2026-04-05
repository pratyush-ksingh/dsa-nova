/**
 * Problem: Find Row with Maximum 1s
 * Difficulty: MEDIUM | XP: 25
 *
 * Key Insight: Rows are sorted, so use binary search per row (O(m log n))
 * or staircase traversal from top-right (O(m + n)).
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Count Each Row
    // Time: O(m * n)  |  Space: O(1)
    //
    // Sum each row, track the maximum.
    // ============================================================
    public static int bruteForce(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int bestRow = 0, maxCount = 0;

        for (int i = 0; i < m; i++) {
            int count = 0;
            for (int j = 0; j < n; j++) {
                count += matrix[i][j];
            }
            if (count > maxCount) {
                maxCount = count;
                bestRow = i;
            }
        }
        return bestRow;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Binary Search per Row
    // Time: O(m log n)  |  Space: O(1)
    //
    // Find leftmost 1 in each row via binary search.
    // count = n - firstOneIndex.
    // ============================================================
    public static int optimal(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int bestRow = 0, maxCount = 0;

        for (int i = 0; i < m; i++) {
            int firstOne = lowerBound(matrix[i], 1);
            int count = n - firstOne;
            if (count > maxCount) {
                maxCount = count;
                bestRow = i;
            }
        }
        return bestRow;
    }

    /** Returns the index of the first element >= target. */
    private static int lowerBound(int[] row, int target) {
        int lo = 0, hi = row.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (row[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    // ============================================================
    // APPROACH 3: BEST -- Staircase Traversal (Top-Right)
    // Time: O(m + n)  |  Space: O(1)
    //
    // Start top-right. Move left on 1 (better count), down on 0.
    // ============================================================
    public static int best(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int row = 0, col = n - 1;
        int bestRow = 0;

        while (row < m && col >= 0) {
            if (matrix[row][col] == 1) {
                bestRow = row;  // this row has 1s extending at least to col
                col--;          // check if it extends further left
            } else {
                row++;          // this row can't beat current best
            }
        }
        return bestRow;
    }

    public static void main(String[] args) {
        System.out.println("=== Find Row with Maximum 1s ===");

        int[][][] tests = {
            {{0,1,1,1},{0,0,1,1},{1,1,1,1},{0,0,0,0}},
            {{0,0},{0,1}},
            {{1,1},{1,1}},
            {{0,0,0},{0,0,0}}
        };

        for (int[][] mat : tests) {
            System.out.println("Matrix:");
            for (int[] row : mat) {
                System.out.println("  " + java.util.Arrays.toString(row));
            }
            System.out.printf("  Brute:   row %d%n", bruteForce(mat));
            System.out.printf("  Optimal: row %d%n", optimal(mat));
            System.out.printf("  Best:    row %d%n", best(mat));
            System.out.println();
        }
    }
}
