import java.util.*;

/**
 * Problem: Increasing Path in Matrix
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Find the length of the longest strictly increasing path in a matrix.
 * Can move in 4 directions; no cycles possible since path is strictly increasing.
 *
 * @author DSA_Nova
 */

// ============================================================
// APPROACH 1: BRUTE FORCE
// DFS from every cell with memoization
// Time: O(M*N)  |  Space: O(M*N)
// ============================================================
class BruteForce {
    static int[][] matrix, memo;
    static int rows, cols;
    static int[] dr = {0, 0, 1, -1};
    static int[] dc = {1, -1, 0, 0};

    static int dfs(int r, int c) {
        if (memo[r][c] != 0) return memo[r][c];
        int best = 1;
        for (int d = 0; d < 4; d++) {
            int nr = r + dr[d], nc = c + dc[d];
            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && matrix[nr][nc] > matrix[r][c]) {
                best = Math.max(best, 1 + dfs(nr, nc));
            }
        }
        return memo[r][c] = best;
    }

    static int longestIncreasingPath(int[][] mat) {
        if (mat == null || mat.length == 0) return 0;
        matrix = mat;
        rows = mat.length;
        cols = mat[0].length;
        memo = new int[rows][cols];
        int ans = 0;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                ans = Math.max(ans, dfs(r, c));
        return ans;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// Topological sort (BFS) — process cells from smallest to largest
// Build outdegree (number of neighbors with larger value), use BFS layers
// Time: O(M*N)  |  Space: O(M*N)
// ============================================================
class Optimal {
    static int longestIncreasingPath(int[][] mat) {
        if (mat == null || mat.length == 0) return 0;
        int rows = mat.length, cols = mat[0].length;
        int[] dr = {0, 0, 1, -1};
        int[] dc = {1, -1, 0, 0};

        // outdegree = number of neighbors with strictly greater value
        int[][] outdegree = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d], nc = c + dc[d];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && mat[nr][nc] > mat[r][c]) {
                        outdegree[r][c]++;
                    }
                }
            }
        }

        // Start from cells with outdegree 0 (local maxima)
        Queue<int[]> queue = new LinkedList<>();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (outdegree[r][c] == 0) queue.offer(new int[]{r, c});

        int length = 0;
        while (!queue.isEmpty()) {
            length++;
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int[] cur = queue.poll();
                int r = cur[0], c = cur[1];
                for (int d = 0; d < 4; d++) {
                    int nr = r + dr[d], nc = c + dc[d];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && mat[nr][nc] < mat[r][c]) {
                        if (--outdegree[nr][nc] == 0) queue.offer(new int[]{nr, nc});
                    }
                }
            }
        }
        return length;
    }
}

// ============================================================
// APPROACH 3: BEST
// DFS + memoization with clean iterative-style using sorted cells
// Sort cells by value; process from smallest; dp[r][c] = max path starting here
// Time: O(M*N log(M*N))  |  Space: O(M*N)
// ============================================================
class Best {
    static int longestIncreasingPath(int[][] mat) {
        if (mat == null || mat.length == 0) return 0;
        int rows = mat.length, cols = mat[0].length;
        int[] dr = {0, 0, 1, -1};
        int[] dc = {1, -1, 0, 0};

        // Sort cells by value descending
        List<int[]> cells = new ArrayList<>();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                cells.add(new int[]{r, c});
        cells.sort((a, b) -> mat[b[0]][b[1]] - mat[a[0]][a[1]]);

        int[][] dp = new int[rows][cols];
        int ans = 0;

        for (int[] cell : cells) {
            int r = cell[0], c = cell[1];
            dp[r][c] = 1;
            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d], nc = c + dc[d];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && mat[nr][nc] > mat[r][c]) {
                    dp[r][c] = Math.max(dp[r][c], 1 + dp[nr][nc]);
                }
            }
            ans = Math.max(ans, dp[r][c]);
        }
        return ans;
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Increasing Path in Matrix ===");

        int[][] m1 = {{9,9,4},{6,6,8},{2,1,1}};
        System.out.println("BruteForce m1: " + BruteForce.longestIncreasingPath(m1));  // 4: 1->2->6->9
        System.out.println("Optimal    m1: " + Optimal.longestIncreasingPath(m1));     // 4
        System.out.println("Best       m1: " + Best.longestIncreasingPath(m1));        // 4

        int[][] m2 = {{3,4,5},{3,2,6},{2,2,1}};
        System.out.println("BruteForce m2: " + BruteForce.longestIncreasingPath(m2));  // 4: 3->4->5->6
        System.out.println("Optimal    m2: " + Optimal.longestIncreasingPath(m2));     // 4
        System.out.println("Best       m2: " + Best.longestIncreasingPath(m2));        // 4

        int[][] m3 = {{1}};
        System.out.println("Single: " + Optimal.longestIncreasingPath(m3));  // 1
    }
}
