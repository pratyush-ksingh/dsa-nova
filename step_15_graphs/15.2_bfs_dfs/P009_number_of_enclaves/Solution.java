/**
 * Problem: Number of Enclaves (LeetCode 1020)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a 2D binary grid (0 = water, 1 = land), return the number of land
 * cells from which you CANNOT walk off the boundary of the grid.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static final int[][] DIRS = {{-1,0},{1,0},{0,-1},{0,1}};

    // ============================================================
    // APPROACH 1: BRUTE FORCE — BFS from every unvisited land cell
    // Time: O((mn)^2) worst case  |  Space: O(mn)
    // ============================================================
    static class BruteForce {
        /**
         * For each unvisited land cell, run a BFS to find its connected
         * component. Track whether any cell in the component lies on the
         * boundary. If not, add the component size to the answer.
         */
        int numEnclaves(int[][] grid) {
            int m = grid.length, n = grid[0].length;
            boolean[][] visited = new boolean[m][n];
            int ans = 0;

            for (int r = 0; r < m; r++) {
                for (int c = 0; c < n; c++) {
                    if (grid[r][c] == 1 && !visited[r][c]) {
                        Queue<int[]> q = new LinkedList<>();
                        q.offer(new int[]{r, c});
                        visited[r][c] = true;
                        int size = 0;
                        boolean onBoundary = false;
                        while (!q.isEmpty()) {
                            int[] cell = q.poll();
                            int cr = cell[0], cc = cell[1];
                            size++;
                            if (cr == 0 || cr == m-1 || cc == 0 || cc == n-1)
                                onBoundary = true;
                            for (int[] d : DIRS) {
                                int nr = cr+d[0], nc = cc+d[1];
                                if (nr>=0&&nr<m&&nc>=0&&nc<n&&!visited[nr][nc]&&grid[nr][nc]==1) {
                                    visited[nr][nc] = true;
                                    q.offer(new int[]{nr, nc});
                                }
                            }
                        }
                        if (!onBoundary) ans += size;
                    }
                }
            }
            return ans;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Multi-source BFS from boundary 1s
    // Time: O(mn)  |  Space: O(mn)
    // ============================================================
    static class Optimal {
        /**
         * Reverse the question: flood-fill via BFS from all boundary land cells.
         * Everything reachable from the boundary is NOT an enclave.
         * Count remaining unvisited land cells — those are the enclaves.
         */
        int numEnclaves(int[][] grid) {
            int m = grid.length, n = grid[0].length;
            boolean[][] visited = new boolean[m][n];
            Queue<int[]> q = new LinkedList<>();

            for (int r = 0; r < m; r++) {
                for (int c = 0; c < n; c++) {
                    if ((r==0||r==m-1||c==0||c==n-1) && grid[r][c]==1) {
                        visited[r][c] = true;
                        q.offer(new int[]{r, c});
                    }
                }
            }

            while (!q.isEmpty()) {
                int[] cell = q.poll();
                int r = cell[0], c = cell[1];
                for (int[] d : DIRS) {
                    int nr = r+d[0], nc = c+d[1];
                    if (nr>=0&&nr<m&&nc>=0&&nc<n&&!visited[nr][nc]&&grid[nr][nc]==1) {
                        visited[nr][nc] = true;
                        q.offer(new int[]{nr, nc});
                    }
                }
            }

            int ans = 0;
            for (int r = 0; r < m; r++)
                for (int c = 0; c < n; c++)
                    if (grid[r][c]==1 && !visited[r][c]) ans++;
            return ans;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Multi-source DFS, in-place marking
    // Time: O(mn)  |  Space: O(mn) stack
    // ============================================================
    static class Best {
        /**
         * Same boundary-flood idea but DFS modifies the grid in-place,
         * setting boundary-reachable land cells to 0. After the flood,
         * count remaining 1s — all enclaves. No separate visited array needed.
         */
        int m, n;
        int[][] g;

        void dfs(int r, int c) {
            if (r<0||r>=m||c<0||c>=n||g[r][c]!=1) return;
            g[r][c] = 0;
            for (int[] d : DIRS) dfs(r+d[0], c+d[1]);
        }

        int numEnclaves(int[][] grid) {
            g = grid; m = grid.length; n = grid[0].length;
            for (int r = 0; r < m; r++) { dfs(r, 0); dfs(r, n-1); }
            for (int c = 0; c < n; c++) { dfs(0, c); dfs(m-1, c); }
            int ans = 0;
            for (int[] row : g) for (int v : row) ans += v;
            return ans;
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Number of Enclaves ===\n");
        int[][] g1 = {{0,0,0,0},{1,0,1,0},{0,1,1,0},{0,0,0,0}};
        // Expected: 3
        System.out.println("Brute:          " + new BruteForce().numEnclaves(deepCopy(g1)));
        System.out.println("Optimal (BFS):  " + new Optimal().numEnclaves(deepCopy(g1)));
        System.out.println("Best (DFS):     " + new Best().numEnclaves(deepCopy(g1)));

        int[][] g2 = {{0,1,1,0},{0,0,1,0},{0,0,1,0},{0,0,0,0}};
        // Expected: 0
        System.out.println("\nBrute:          " + new BruteForce().numEnclaves(deepCopy(g2)));
        System.out.println("Optimal (BFS):  " + new Optimal().numEnclaves(deepCopy(g2)));
        System.out.println("Best (DFS):     " + new Best().numEnclaves(deepCopy(g2)));
    }

    static int[][] deepCopy(int[][] g) {
        int[][] c = new int[g.length][];
        for (int i = 0; i < g.length; i++) c[i] = g[i].clone();
        return c;
    }
}
