import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE
// Time: O(m*n)  |  Space: O(1)
// Check every cell: a peak must be strictly greater than all
// 4 neighbours (or lie on boundary). Return [row, col].
// ============================================================
class BruteForce {
    public static int[] solve(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                boolean isPeak = true;
                for (int d = 0; d < 4; d++) {
                    int ni = i + dr[d], nj = j + dc[d];
                    if (ni >= 0 && ni < m && nj >= 0 && nj < n && mat[ni][nj] >= mat[i][j]) {
                        isPeak = false; break;
                    }
                }
                if (isPeak) return new int[]{i, j};
            }
        }
        return new int[]{-1, -1};
    }
}

// ============================================================
// APPROACH 2: OPTIMAL (Binary Search on columns)
// Time: O(m log n)  |  Space: O(1)
// Binary search on column index mid.
// Find the row with max value in column mid.
// If left neighbour > mat[row][mid]: search left half.
// If right neighbour > mat[row][mid]: search right half.
// Else: mat[row][mid] is a peak.
// ============================================================
class Optimal {
    public static int[] solve(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int lo = 0, hi = n - 1;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            // Find row with max in column mid
            int maxRow = 0;
            for (int r = 0; r < m; r++)
                if (mat[r][mid] > mat[maxRow][mid]) maxRow = r;

            boolean leftBig  = mid > 0     && mat[maxRow][mid - 1] > mat[maxRow][mid];
            boolean rightBig = mid < n - 1 && mat[maxRow][mid + 1] > mat[maxRow][mid];

            if (!leftBig && !rightBig) return new int[]{maxRow, mid};
            else if (leftBig) hi = mid - 1;
            else lo = mid + 1;
        }
        return new int[]{-1, -1};
    }
}

// ============================================================
// APPROACH 3: BEST (Binary Search on rows)
// Time: O(n log m)  |  Space: O(1)
// Symmetric to OPTIMAL but binary-searches the row dimension.
// ============================================================
class Best {
    public static int[] solve(int[][] mat) {
        int m = mat.length, n = mat[0].length;
        int lo = 0, hi = m - 1;
        while (lo <= hi) {
            int mid = (lo + hi) / 2;
            // Find column with max in row mid
            int maxCol = 0;
            for (int c = 0; c < n; c++)
                if (mat[mid][c] > mat[mid][maxCol]) maxCol = c;

            boolean upBig   = mid > 0     && mat[mid - 1][maxCol] > mat[mid][maxCol];
            boolean downBig = mid < m - 1 && mat[mid + 1][maxCol] > mat[mid][maxCol];

            if (!upBig && !downBig) return new int[]{mid, maxCol};
            else if (upBig) hi = mid - 1;
            else lo = mid + 1;
        }
        return new int[]{-1, -1};
    }
}

public class Solution {
    static boolean verifyPeak(int[][] mat, int r, int c) {
        int m = mat.length, n = mat[0].length;
        int[] dr = {-1, 1, 0, 0}, dc = {0, 0, -1, 1};
        for (int d = 0; d < 4; d++) {
            int nr = r + dr[d], nc = c + dc[d];
            if (nr >= 0 && nr < m && nc >= 0 && nc < n && mat[nr][nc] >= mat[r][c]) return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("=== Find Peak Element in 2D Matrix ===");
        int[][] m1 = {{1,4},{3,2}};
        int[][] m2 = {{10,20,15},{21,30,14},{7,16,32}};
        int[][] m3 = {{1,2,3,4},{2,3,4,5},{3,4,5,6}};

        for (int[][] m : new int[][][]{m1, m2, m3}) {
            int[] bf  = BruteForce.solve(m);
            int[] op  = Optimal.solve(m);
            int[] bst = Best.solve(m);
            System.out.println("BF  [" + bf[0]  + "," + bf[1]  + "] val=" + (bf[0]>=0?m[bf[0]][bf[1]]:-1)
                + " isPeak=" + (bf[0]>=0 && verifyPeak(m, bf[0], bf[1])));
            System.out.println("OPT [" + op[0]  + "," + op[1]  + "] val=" + (op[0]>=0?m[op[0]][op[1]]:-1)
                + " isPeak=" + (op[0]>=0 && verifyPeak(m, op[0], op[1])));
            System.out.println("BEST[" + bst[0] + "," + bst[1] + "] val=" + (bst[0]>=0?m[bst[0]][bst[1]]:-1)
                + " isPeak=" + (bst[0]>=0 && verifyPeak(m, bst[0], bst[1])));
            System.out.println();
        }
    }
}
