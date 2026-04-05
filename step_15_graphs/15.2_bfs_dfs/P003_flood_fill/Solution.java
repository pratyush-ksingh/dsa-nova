/**
 * Problem: Flood Fill (LeetCode #733)
 * Difficulty: EASY | XP: 10
 *
 * Given image (2D grid), starting pixel, and new color, flood fill.
 *
 * @author DSA_Nova
 */
import java.util.*;

// ============================================================
// Approach 1: DFS (Recursive)
// Time: O(m * n) | Space: O(m * n) recursion stack
// ============================================================
class DFSFloodFill {
    public int[][] floodFill(int[][] image, int sr, int sc, int color) {
        int originalColor = image[sr][sc];
        if (originalColor == color) return image; // crucial edge case!

        dfs(image, sr, sc, originalColor, color);
        return image;
    }

    private void dfs(int[][] image, int r, int c, int originalColor, int newColor) {
        // Bounds check + color check
        if (r < 0 || r >= image.length || c < 0 || c >= image[0].length) return;
        if (image[r][c] != originalColor) return;

        image[r][c] = newColor; // fill (also marks as visited)

        // Explore 4 directions
        dfs(image, r + 1, c, originalColor, newColor); // down
        dfs(image, r - 1, c, originalColor, newColor); // up
        dfs(image, r, c + 1, originalColor, newColor); // right
        dfs(image, r, c - 1, originalColor, newColor); // left
    }
}

// ============================================================
// Approach 2: BFS (Iterative)
// Time: O(m * n) | Space: O(m * n) queue
// ============================================================
class BFSFloodFill {
    private static final int[][] DIRS = {{1,0},{-1,0},{0,1},{0,-1}};

    public int[][] floodFill(int[][] image, int sr, int sc, int color) {
        int originalColor = image[sr][sc];
        if (originalColor == color) return image;

        int m = image.length, n = image[0].length;
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{sr, sc});
        image[sr][sc] = color;

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            for (int[] d : DIRS) {
                int nr = cell[0] + d[0], nc = cell[1] + d[1];
                if (nr >= 0 && nr < m && nc >= 0 && nc < n
                        && image[nr][nc] == originalColor) {
                    image[nr][nc] = color;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }
        return image;
    }
}

// ============================================================
// Main driver
// ============================================================
public class Solution {

    private static String gridToString(int[][] grid) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < grid.length; i++) {
            sb.append(Arrays.toString(grid[i]));
            if (i < grid.length - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }

    private static int[][] copy(int[][] grid) {
        int[][] c = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) c[i] = grid[i].clone();
        return c;
    }

    public static void main(String[] args) {
        System.out.println("=== Flood Fill ===\n");

        DFSFloodFill dfs = new DFSFloodFill();
        BFSFloodFill bfs = new BFSFloodFill();

        // Test 1: Standard case
        int[][] img1 = {{1,1,1},{1,1,0},{1,0,1}};
        int[][] exp1 = {{2,2,2},{2,2,0},{2,0,1}};
        int[][] dfsResult1 = dfs.floodFill(copy(img1), 1, 1, 2);
        int[][] bfsResult1 = bfs.floodFill(copy(img1), 1, 1, 2);
        System.out.println("Test 1:");
        System.out.println("  Input:    " + gridToString(img1));
        System.out.println("  DFS:      " + gridToString(dfsResult1));
        System.out.println("  BFS:      " + gridToString(bfsResult1));
        System.out.println("  Expected: " + gridToString(exp1));
        System.out.println("  Pass: " + Arrays.deepEquals(dfsResult1, exp1) + "\n");

        // Test 2: Same color -- no change
        int[][] img2 = {{0,0,0},{0,0,0}};
        int[][] dfsResult2 = dfs.floodFill(copy(img2), 0, 0, 0);
        System.out.println("Test 2 (same color):");
        System.out.println("  DFS:      " + gridToString(dfsResult2));
        System.out.println("  Expected: " + gridToString(img2));
        System.out.println("  Pass: " + Arrays.deepEquals(dfsResult2, img2) + "\n");

        // Test 3: 1x1 grid
        int[][] img3 = {{5}};
        int[][] dfsResult3 = dfs.floodFill(copy(img3), 0, 0, 3);
        System.out.println("Test 3 (1x1):");
        System.out.println("  DFS:      " + gridToString(dfsResult3));
        System.out.println("  Expected: [[3]]");
        System.out.println("  Pass: " + (dfsResult3[0][0] == 3) + "\n");

        // Test 4: Only starting pixel changes (surrounded by different colors)
        int[][] img4 = {{0,1,0},{1,1,1},{0,1,0}};
        int[][] exp4 = {{0,3,0},{3,3,3},{0,3,0}};
        int[][] dfsResult4 = dfs.floodFill(copy(img4), 1, 1, 3);
        System.out.println("Test 4 (cross pattern):");
        System.out.println("  DFS:      " + gridToString(dfsResult4));
        System.out.println("  Expected: " + gridToString(exp4));
        System.out.println("  Pass: " + Arrays.deepEquals(dfsResult4, exp4));
    }
}
