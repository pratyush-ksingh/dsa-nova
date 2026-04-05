/**
 * Problem: Count Square Submatrices with All Ones (LeetCode #1277)
 * Difficulty: MEDIUM | XP: 25
 *
 * dp[i][j] = size of largest all-ones square ending at (i,j).
 * Answer = sum of all dp[i][j].
 * Approaches: Brute Force -> Recursive DP -> Tabulation -> Space Optimized
 *
 * @author DSA_Nova
 */
import java.util.Arrays;

// ============================================================
// Approach 1: Brute Force (Check every possible square)
// Time: O(m * n * min(m,n)^2) | Space: O(1)
// ============================================================
class BruteSquares {
    public int countSquares(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int count = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // Try all square sizes starting from (i,j) as top-left
                for (int k = 1; k <= Math.min(m - i, n - j); k++) {
                    if (isAllOnes(matrix, i, j, k)) {
                        count++;
                    } else {
                        break; // Larger squares will also fail
                    }
                }
            }
        }
        return count;
    }

    private boolean isAllOnes(int[][] matrix, int r, int c, int size) {
        for (int i = r; i < r + size; i++) {
            for (int j = c; j < c + size; j++) {
                if (matrix[i][j] == 0) return false;
            }
        }
        return true;
    }
}

// ============================================================
// Approach 2: Memoization (Recursive DP)
// Time: O(m * n) | Space: O(m * n)
// ============================================================
class MemoSquares {
    public int countSquares(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] dp = new int[m][n];
        for (int[] row : dp) Arrays.fill(row, -1);

        int count = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                count += solve(matrix, i, j, dp);
            }
        }
        return count;
    }

    private int solve(int[][] matrix, int i, int j, int[][] dp) {
        if (i < 0 || j < 0) return 0;
        if (matrix[i][j] == 0) return 0;
        if (dp[i][j] != -1) return dp[i][j];

        int up = solve(matrix, i - 1, j, dp);
        int left = solve(matrix, i, j - 1, dp);
        int diag = solve(matrix, i - 1, j - 1, dp);

        dp[i][j] = Math.min(up, Math.min(left, diag)) + 1;
        return dp[i][j];
    }
}

// ============================================================
// Approach 3: Tabulation (Bottom-Up DP)
// Time: O(m * n) | Space: O(m * n)
// ============================================================
class TabSquares {
    public int countSquares(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[][] dp = new int[m][n];
        int count = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    if (i == 0 || j == 0) {
                        // First row or first column: can only be 1x1
                        dp[i][j] = 1;
                    } else {
                        dp[i][j] = Math.min(dp[i - 1][j],
                                   Math.min(dp[i][j - 1], dp[i - 1][j - 1])) + 1;
                    }
                }
                count += dp[i][j];
            }
        }

        return count;
    }
}

// ============================================================
// Approach 4: Space Optimized
// Time: O(m * n) | Space: O(n)
// ============================================================
class SpaceSquares {
    public int countSquares(int[][] matrix) {
        int m = matrix.length, n = matrix[0].length;
        int[] prev = new int[n];
        int count = 0;

        // First row
        for (int j = 0; j < n; j++) {
            prev[j] = matrix[0][j];
            count += prev[j];
        }

        for (int i = 1; i < m; i++) {
            int[] curr = new int[n];
            curr[0] = matrix[i][0];
            count += curr[0];

            for (int j = 1; j < n; j++) {
                if (matrix[i][j] == 1) {
                    curr[j] = Math.min(prev[j],
                              Math.min(curr[j - 1], prev[j - 1])) + 1;
                }
                count += curr[j];
            }
            prev = curr;
        }

        return count;
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Count Square Submatrices with All Ones ===\n");

        BruteSquares brute = new BruteSquares();
        MemoSquares memo = new MemoSquares();
        TabSquares tab = new TabSquares();
        SpaceSquares space = new SpaceSquares();

        int[][][] matrices = {
            {{0,1,1,1},{1,1,1,1},{0,1,1,1}},
            {{1,0,1},{1,1,0},{1,1,0}},
            {{1}},
            {{0}},
            {{1,1},{1,1}},
            {{0,0},{0,0}},
        };
        int[] expected = {15, 7, 1, 0, 5, 0};

        for (int t = 0; t < matrices.length; t++) {
            int b = brute.countSquares(matrices[t]);
            int m = memo.countSquares(matrices[t]);
            int tb = tab.countSquares(matrices[t]);
            int s = space.countSquares(matrices[t]);

            boolean pass = b == expected[t] && m == expected[t]
                    && tb == expected[t] && s == expected[t];

            System.out.println("Matrix " + (t + 1) + ":");
            for (int[] row : matrices[t]) {
                System.out.println("  " + Arrays.toString(row));
            }
            System.out.println("  Brute=" + b + " | Memo=" + m + " | Tab=" + tb + " | Space=" + s);
            System.out.println("  Expected=" + expected[t] + " | Pass=" + pass + "\n");
        }
    }
}
