import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - DFS flood-fill, mark visited
// Time: O(R * C)  |  Space: O(R * C) recursion + visited array
// ============================================================
// For each unvisited cell with value 1, run DFS to count the
// connected region size. Track the maximum. Mark visited cells
// by flipping them to 0 or using a separate boolean grid.
// ============================================================

class BruteForce {
    int rows, cols;
    boolean[][] vis;

    private int dfs(int[][] A, int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols) return 0;
        if (vis[r][c] || A[r][c] == 0) return 0;
        vis[r][c] = true;
        int count = 1;
        // 8-directional
        int[] dr = {-1,-1,-1,0,0,1,1,1};
        int[] dc = {-1,0,1,-1,1,-1,0,1};
        for (int d = 0; d < 8; d++) {
            count += dfs(A, r + dr[d], c + dc[d]);
        }
        return count;
    }

    public int solve(int[][] A) {
        rows = A.length;
        cols = A[0].length;
        vis = new boolean[rows][cols];
        int maxRegion = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!vis[i][j] && A[i][j] == 1) {
                    maxRegion = Math.max(maxRegion, dfs(A, i, j));
                }
            }
        }
        return maxRegion;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - BFS flood-fill with queue
// Time: O(R * C)  |  Space: O(R * C)
// ============================================================
// BFS avoids recursion depth limits. Use a queue, mark visited
// on enqueue to prevent re-adding. Count all cells in the BFS.
// ============================================================

class Optimal {
    static final int[] DR = {-1,-1,-1,0,0,1,1,1};
    static final int[] DC = {-1,0,1,-1,1,-1,0,1};

    public int solve(int[][] A) {
        int rows = A.length, cols = A[0].length;
        boolean[][] vis = new boolean[rows][cols];
        int maxRegion = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!vis[i][j] && A[i][j] == 1) {
                    int size = 0;
                    Queue<int[]> q = new LinkedList<>();
                    q.add(new int[]{i, j});
                    vis[i][j] = true;
                    while (!q.isEmpty()) {
                        int[] cell = q.poll();
                        size++;
                        for (int d = 0; d < 8; d++) {
                            int nr = cell[0] + DR[d];
                            int nc = cell[1] + DC[d];
                            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                                    && !vis[nr][nc] && A[nr][nc] == 1) {
                                vis[nr][nc] = true;
                                q.add(new int[]{nr, nc});
                            }
                        }
                    }
                    maxRegion = Math.max(maxRegion, size);
                }
            }
        }
        return maxRegion;
    }
}

// ============================================================
// APPROACH 3: BEST - In-place DFS (no extra visited array)
// Time: O(R * C)  |  Space: O(R * C) worst-case recursion
// ============================================================
// Flip visited 1s to 0 in-place to avoid a separate visited
// array. Saves O(R*C) space on the visited array (uses O(1)
// extra data structures beyond the recursion stack).
// Real-life use: counting contiguous land masses in satellite
// or geographic raster images for cartography analysis.
// ============================================================

class Best {
    int rows, cols;
    int[][] grid;

    private int dfs(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] == 0) return 0;
        grid[r][c] = 0; // mark visited in-place
        int cnt = 1;
        int[] dr = {-1,-1,-1,0,0,1,1,1};
        int[] dc = {-1,0,1,-1,1,-1,0,1};
        for (int d = 0; d < 8; d++) cnt += dfs(r + dr[d], c + dc[d]);
        return cnt;
    }

    public int solve(int[][] A) {
        // Work on a copy to not mutate input
        rows = A.length;
        cols = A[0].length;
        grid = new int[rows][cols];
        for (int i = 0; i < rows; i++) grid[i] = A[i].clone();

        int maxRegion = 0;
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                if (grid[i][j] == 1)
                    maxRegion = Math.max(maxRegion, dfs(i, j));
        return maxRegion;
    }
}

// ============================================================
// TEST HARNESS
// ============================================================

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Region in Binary Matrix ===");

        // Test 1: InterviewBit example
        int[][] m1 = {
            {0,0,1,1,0},
            {1,0,1,1,0},
            {0,1,0,0,0},
            {0,0,0,0,1}
        };
        System.out.println("Test1 Brute   (expect 6): " + new BruteForce().solve(m1));
        System.out.println("Test1 Optimal (expect 6): " + new Optimal().solve(m1));
        System.out.println("Test1 Best    (expect 6): " + new Best().solve(m1));

        // Test 2: All zeros
        int[][] m2 = {{0,0},{0,0}};
        System.out.println("Test2 Best    (expect 0): " + new Best().solve(m2));

        // Test 3: All ones 3x3
        int[][] m3 = {{1,1,1},{1,1,1},{1,1,1}};
        System.out.println("Test3 Best    (expect 9): " + new Best().solve(m3));

        // Test 4: Single row
        int[][] m4 = {{1,0,1,1,0,1}};
        System.out.println("Test4 Best    (expect 2): " + new Best().solve(m4));
    }
}
