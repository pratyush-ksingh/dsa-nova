import java.util.*;

/**
 * Problem: Rotten Oranges (LeetCode #994)
 * Difficulty: MEDIUM | XP: 25
 *
 * @author DSA_Nova
 */
public class Solution {

    static final int[] DR = {-1, 1, 0, 0};
    static final int[] DC = {0, 0, -1, 1};

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- Repeated Grid Scans
    // Time: O((m*n)^2)  |  Space: O(1)
    // Each minute, scan entire grid to spread rot. Repeat until stable.
    // ============================================================
    public static int bruteForce(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        int minutes = 0;

        while (true) {
            // Track cells to rot this minute (use temp marker 3)
            boolean changed = false;

            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (grid[r][c] == 2) {
                        for (int d = 0; d < 4; d++) {
                            int nr = r + DR[d], nc = c + DC[d];
                            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                                    && grid[nr][nc] == 1) {
                                grid[nr][nc] = 3; // mark as "newly rotten"
                                changed = true;
                            }
                        }
                    }
                }
            }

            if (!changed) break;

            // Convert all 3s to 2s
            for (int r = 0; r < rows; r++)
                for (int c = 0; c < cols; c++)
                    if (grid[r][c] == 3) grid[r][c] = 2;

            minutes++;
        }

        // Check for remaining fresh oranges
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (grid[r][c] == 1) return -1;

        return minutes;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Multi-Source BFS
    // Time: O(m * n)  |  Space: O(m * n)
    // All rotten oranges start BFS simultaneously. Levels = minutes.
    // ============================================================
    public static int optimal(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        Queue<int[]> queue = new LinkedList<>();
        int fresh = 0;

        // Initialize: enqueue all rotten, count fresh
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 2) queue.offer(new int[]{r, c});
                else if (grid[r][c] == 1) fresh++;
            }
        }

        if (fresh == 0) return 0;

        int minutes = 0;

        // BFS level by level
        while (!queue.isEmpty()) {
            int size = queue.size();
            boolean rotted = false;

            for (int i = 0; i < size; i++) {
                int[] cell = queue.poll();
                int r = cell[0], c = cell[1];

                for (int d = 0; d < 4; d++) {
                    int nr = r + DR[d], nc = c + DC[d];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                            && grid[nr][nc] == 1) {
                        grid[nr][nc] = 2;
                        fresh--;
                        queue.offer(new int[]{nr, nc});
                        rotted = true;
                    }
                }
            }

            if (rotted) minutes++;
        }

        return fresh == 0 ? minutes : -1;
    }

    // ============================================================
    // APPROACH 3: BEST -- Multi-Source BFS (Flat Index, Early Exit)
    // Time: O(m * n)  |  Space: O(m * n)
    // Same BFS, but uses flat index (r*cols+c) to reduce overhead.
    // Early exit when fresh reaches 0.
    // ============================================================
    public static int best(int[][] grid) {
        int rows = grid.length, cols = grid[0].length;
        Queue<Integer> queue = new LinkedList<>();  // flat index: r * cols + c
        int fresh = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 2) queue.offer(r * cols + c);
                else if (grid[r][c] == 1) fresh++;
            }
        }

        if (fresh == 0) return 0;

        int minutes = 0;

        while (!queue.isEmpty() && fresh > 0) {
            int size = queue.size();
            for (int i = 0; i < size; i++) {
                int idx = queue.poll();
                int r = idx / cols, c = idx % cols;

                for (int d = 0; d < 4; d++) {
                    int nr = r + DR[d], nc = c + DC[d];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                            && grid[nr][nc] == 1) {
                        grid[nr][nc] = 2;
                        fresh--;
                        queue.offer(nr * cols + nc);
                    }
                }
            }
            minutes++;
        }

        return fresh == 0 ? minutes : -1;
    }

    // Helper to deep copy grid (since BFS modifies it in-place)
    private static int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) copy[i] = grid[i].clone();
        return copy;
    }

    public static void main(String[] args) {
        System.out.println("=== Rotten Oranges ===\n");

        int[][] grid1 = {{2,1,1},{1,1,0},{0,1,1}};
        System.out.println("Brute:   " + bruteForce(copyGrid(grid1)));   // 4
        System.out.println("Optimal: " + optimal(copyGrid(grid1)));      // 4
        System.out.println("Best:    " + best(copyGrid(grid1)));         // 4

        int[][] grid2 = {{2,1,1},{0,1,1},{1,0,1}};
        System.out.println("\nIsolated: " + optimal(copyGrid(grid2)));   // -1

        int[][] grid3 = {{0,2}};
        System.out.println("No fresh: " + optimal(copyGrid(grid3)));     // 0
    }
}
