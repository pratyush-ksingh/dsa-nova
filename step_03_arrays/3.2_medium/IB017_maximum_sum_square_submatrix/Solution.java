/**
 * Problem: Maximum Sum Square SubMatrix
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given an N x N integer matrix A and an integer B, find the maximum sum
 * among all B x B square submatrices of A.
 *
 * @author DSA_Nova
 */
public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Time: O(n^2 * B^2)  |  Space: O(1)
    // ============================================================
    /**
     * For each valid top-left corner (i, j) of a B x B submatrix,
     * compute the sum of all B*B elements by iterating over them.
     * Track and return the maximum sum.
     */
    static class BruteForce {
        public int solve(int[][] A, int B) {
            int n = A.length;
            int maxSum = Integer.MIN_VALUE;
            for (int i = 0; i <= n - B; i++) {
                for (int j = 0; j <= n - B; j++) {
                    int sum = 0;
                    for (int r = i; r < i + B; r++) {
                        for (int c = j; c < j + B; c++) {
                            sum += A[r][c];
                        }
                    }
                    maxSum = Math.max(maxSum, sum);
                }
            }
            return maxSum;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — 2D Prefix Sum
    // Time: O(n^2)  |  Space: O(n^2)
    // ============================================================
    /**
     * Build a 2D prefix sum table (1-indexed): prefix[i][j] is the sum of
     * all elements in the rectangle from (0,0) to (i-1,j-1).
     * Any B x B submatrix sum is then computed in O(1) using inclusion-exclusion:
     *   sum = prefix[i][j] - prefix[i-B][j] - prefix[i][j-B] + prefix[i-B][j-B]
     */
    static class Optimal {
        public int solve(int[][] A, int B) {
            int n = A.length;
            int[][] prefix = new int[n + 1][n + 1];
            for (int i = 1; i <= n; i++) {
                for (int j = 1; j <= n; j++) {
                    prefix[i][j] = A[i-1][j-1]
                            + prefix[i-1][j]
                            + prefix[i][j-1]
                            - prefix[i-1][j-1];
                }
            }
            int maxSum = Integer.MIN_VALUE;
            for (int i = B; i <= n; i++) {
                for (int j = B; j <= n; j++) {
                    int sum = prefix[i][j]
                            - prefix[i-B][j]
                            - prefix[i][j-B]
                            + prefix[i-B][j-B];
                    maxSum = Math.max(maxSum, sum);
                }
            }
            return maxSum;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Column Prefix Sums + Sliding Window
    // Time: O(n^2)  |  Space: O(n)
    // ============================================================
    /**
     * Compute column prefix sums so that for any B consecutive rows,
     * the sum of each column segment is available in O(1).
     * Then use a sliding window of size B across each such row-band
     * to get every B x B submatrix sum.
     * Space is O(n) instead of O(n^2).
     */
    static class Best {
        public int solve(int[][] A, int B) {
            int n = A.length;
            // colPrefix[i][j] = sum of A[0..i-1][j]
            int[][] colPrefix = new int[n + 1][n];
            for (int i = 1; i <= n; i++) {
                for (int j = 0; j < n; j++) {
                    colPrefix[i][j] = colPrefix[i-1][j] + A[i-1][j];
                }
            }

            int maxSum = Integer.MIN_VALUE;
            for (int i = B - 1; i < n; i++) {
                // Column sums for rows [i-B+1 .. i]
                int[] colSums = new int[n];
                for (int j = 0; j < n; j++) {
                    colSums[j] = colPrefix[i+1][j] - colPrefix[i-B+1][j];
                }
                // Sliding window of size B on colSums
                int windowSum = 0;
                for (int j = 0; j < B; j++) windowSum += colSums[j];
                maxSum = Math.max(maxSum, windowSum);
                for (int j = 1; j <= n - B; j++) {
                    windowSum += colSums[j + B - 1] - colSums[j - 1];
                    maxSum = Math.max(maxSum, windowSum);
                }
            }
            return maxSum;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Maximum Sum Square SubMatrix ===");
        int[][] A = {
            {1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 16}
        };
        int B = 2;
        System.out.println("4x4 matrix, B=2:");
        System.out.println("Brute:   " + new BruteForce().solve(A, B));   // 54
        System.out.println("Optimal: " + new Optimal().solve(A, B));       // 54
        System.out.println("Best:    " + new Best().solve(A, B));          // 54

        int[][] A2 = {
            {1, -2, 3},
            {-4, 5, -6},
            {7, -8, 9}
        };
        System.out.println("\n3x3 matrix with negatives, B=2:");
        System.out.println("Brute:   " + new BruteForce().solve(A2, B));
        System.out.println("Optimal: " + new Optimal().solve(A2, B));
        System.out.println("Best:    " + new Best().solve(A2, B));
    }
}
