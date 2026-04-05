import java.util.*;

/**
 * Problem: Shortest Distance in Binary Maze
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    static final int[] DR = {-1, 1, 0, 0};
    static final int[] DC = {0, 0, -1, 1};

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- DFS Explore All Paths
    // Time: O(4^(n*n)) worst case  |  Space: O(n*n)
    // Explore every path, track minimum distance.
    // ============================================================
    public static int bruteForce(int[][] grid, int[] src, int[] dst) {
        int n = grid.length;
        if (grid[src[0]][src[1]] == 1 || grid[dst[0]][dst[1]] == 1) return -1;
        if (src[0] == dst[0] && src[1] == dst[1]) return 0;

        boolean[][] visited = new boolean[n][n];
        int[] minDist = {Integer.MAX_VALUE};
        visited[src[0]][src[1]] = true;
        dfs(grid, visited, src[0], src[1], dst[0], dst[1], 0, minDist);
        return minDist[0] == Integer.MAX_VALUE ? -1 : minDist[0];
    }

    private static void dfs(int[][] grid, boolean[][] visited, int r, int c,
                             int dr, int dc, int dist, int[] minDist) {
        if (r == dr && c == dc) {
            minDist[0] = Math.min(minDist[0], dist);
            return;
        }

        int n = grid.length;
        for (int d = 0; d < 4; d++) {
            int nr = r + DR[d], nc = c + DC[d];
            if (nr >= 0 && nr < n && nc >= 0 && nc < n
                    && grid[nr][nc] == 0 && !visited[nr][nc]
                    && dist + 1 < minDist[0]) {  // pruning
                visited[nr][nc] = true;
                dfs(grid, visited, nr, nc, dr, dc, dist + 1, minDist);
                visited[nr][nc] = false; // backtrack
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- BFS (Standard)
    // Time: O(n * n)  |  Space: O(n * n)
    // Level-by-level BFS gives shortest path in unweighted grid.
    // ============================================================
    public static int optimal(int[][] grid, int[] src, int[] dst) {
        int n = grid.length;
        if (grid[src[0]][src[1]] == 1 || grid[dst[0]][dst[1]] == 1) return -1;
        if (src[0] == dst[0] && src[1] == dst[1]) return 0;

        boolean[][] visited = new boolean[n][n];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{src[0], src[1]});
        visited[src[0]][src[1]] = true;
        int dist = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            dist++;

            for (int i = 0; i < size; i++) {
                int[] cell = queue.poll();
                int r = cell[0], c = cell[1];

                for (int d = 0; d < 4; d++) {
                    int nr = r + DR[d], nc = c + DC[d];
                    if (nr >= 0 && nr < n && nc >= 0 && nc < n
                            && grid[nr][nc] == 0 && !visited[nr][nc]) {
                        if (nr == dst[0] && nc == dst[1]) return dist;
                        visited[nr][nc] = true;
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }
        }

        return -1;
    }

    // ============================================================
    // APPROACH 3: BEST -- BFS with In-Place Marking + Flat Index
    // Time: O(n * n)  |  Space: O(n * n) for queue, O(1) extra
    // Marks grid cells in-place to avoid visited array.
    // Uses flat index for reduced memory overhead.
    // ============================================================
    public static int best(int[][] grid, int[] src, int[] dst) {
        int n = grid.length;
        if (grid[src[0]][src[1]] == 1 || grid[dst[0]][dst[1]] == 1) return -1;
        if (src[0] == dst[0] && src[1] == dst[1]) return 0;

        int target = dst[0] * n + dst[1];
        Queue<Integer> queue = new LinkedList<>();  // flat index
        int startIdx = src[0] * n + src[1];
        queue.offer(startIdx);
        grid[src[0]][src[1]] = 1; // mark visited in-place
        int dist = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            dist++;

            for (int i = 0; i < size; i++) {
                int idx = queue.poll();
                int r = idx / n, c = idx % n;

                for (int d = 0; d < 4; d++) {
                    int nr = r + DR[d], nc = c + DC[d];
                    if (nr >= 0 && nr < n && nc >= 0 && nc < n
                            && grid[nr][nc] == 0) {
                        int nIdx = nr * n + nc;
                        if (nIdx == target) return dist;
                        grid[nr][nc] = 1; // mark visited
                        queue.offer(nIdx);
                    }
                }
            }
        }

        return -1;
    }

    // Helper to deep copy grid
    private static int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) copy[i] = grid[i].clone();
        return copy;
    }

    public static void main(String[] args) {
        System.out.println("=== Shortest Distance in Binary Maze ===\n");

        int[][] grid1 = {{0,0,0},{0,1,0},{0,0,0}};
        int[] src1 = {0, 0}, dst1 = {2, 2};
        System.out.println("Brute:   " + bruteForce(copyGrid(grid1), src1, dst1)); // 4
        System.out.println("Optimal: " + optimal(copyGrid(grid1), src1, dst1));    // 4
        System.out.println("Best:    " + best(copyGrid(grid1), src1, dst1));       // 4

        // No path
        int[][] grid2 = {{0,1},{1,0}};
        System.out.println("\nBlocked: " + optimal(copyGrid(grid2), new int[]{0,0}, new int[]{1,1})); // -1

        // Same source and destination
        System.out.println("Same:    " + optimal(copyGrid(grid1), new int[]{0,0}, new int[]{0,0})); // 0
    }
}
