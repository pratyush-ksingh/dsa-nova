import java.util.*;

/**
 * Problem: 01 Matrix (LeetCode #542)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a binary matrix, find the distance of the nearest 0 for each cell.
 * Distance is the number of 4-directional steps.
 *
 * @author DSA_Nova
 */
public class Solution {

    static final int[] DR = {-1, 1, 0, 0};
    static final int[] DC = {0, 0, -1, 1};

    // ============================================================
    // APPROACH 1: BRUTE FORCE -- BFS from each non-zero cell
    // Time: O((m*n)^2)  |  Space: O(m*n)
    // For every cell containing 1, run a separate BFS to find the
    // nearest 0. Each BFS costs O(m*n), and there can be m*n cells.
    // ============================================================
    public static int[][] bruteForce(int[][] mat) {
        int rows = mat.length, cols = mat[0].length;
        int[][] result = new int[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (mat[r][c] == 0) {
                    result[r][c] = 0;
                } else {
                    result[r][c] = bfsNearest(mat, r, c, rows, cols);
                }
            }
        }
        return result;
    }

    private static int bfsNearest(int[][] mat, int sr, int sc, int rows, int cols) {
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{sr, sc, 0});
        visited[sr][sc] = true;

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1], dist = cell[2];
            if (mat[r][c] == 0) return dist;

            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d], nc = c + DC[d];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    queue.offer(new int[]{nr, nc, dist + 1});
                }
            }
        }
        return Integer.MAX_VALUE;
    }

    // ============================================================
    // APPROACH 2: OPTIMAL -- Multi-Source BFS from all 0s
    // Time: O(m*n)  |  Space: O(m*n)
    // Seed all 0-cells into the BFS queue at distance 0.
    // Expand outward; the first time any cell is reached its distance
    // is the minimum possible. Each cell is enqueued exactly once.
    // ============================================================
    public static int[][] optimal(int[][] mat) {
        int rows = mat.length, cols = mat[0].length;
        int[][] dist = new int[rows][cols];
        Queue<int[]> queue = new LinkedList<>();

        // Seed all 0s; mark 1s as unvisited with a large sentinel
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (mat[r][c] == 0) {
                    dist[r][c] = 0;
                    queue.offer(new int[]{r, c});
                } else {
                    dist[r][c] = Integer.MAX_VALUE;
                }
            }
        }

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];

            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d], nc = c + DC[d];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    if (dist[r][c] + 1 < dist[nr][nc]) {
                        dist[nr][nc] = dist[r][c] + 1;
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }
        }
        return dist;
    }

    // ============================================================
    // APPROACH 3: BEST -- Multi-Source BFS with flat index
    // Time: O(m*n)  |  Space: O(m*n)
    // Same algorithm as optimal but stores flat indices (r*cols+c)
    // in the queue to reduce per-entry overhead.
    // ============================================================
    public static int[][] best(int[][] mat) {
        int rows = mat.length, cols = mat[0].length;
        int[] dist = new int[rows * cols];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Queue<Integer> queue = new LinkedList<>();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (mat[r][c] == 0) {
                    int idx = r * cols + c;
                    dist[idx] = 0;
                    queue.offer(idx);
                }
            }
        }

        while (!queue.isEmpty()) {
            int idx = queue.poll();
            int r = idx / cols, c = idx % cols;

            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d], nc = c + DC[d];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                    int nidx = nr * cols + nc;
                    if (dist[idx] + 1 < dist[nidx]) {
                        dist[nidx] = dist[idx] + 1;
                        queue.offer(nidx);
                    }
                }
            }
        }

        // Reshape into 2-D result
        int[][] result = new int[rows][cols];
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                result[r][c] = dist[r * cols + c];
        return result;
    }

    // Helper to print a 2-D matrix
    private static void print2D(int[][] mat) {
        for (int[] row : mat) System.out.println(Arrays.toString(row));
    }

    public static void main(String[] args) {
        System.out.println("=== 01 Matrix ===\n");

        int[][] mat1 = {{0,0,0},{0,1,0},{0,0,0}};
        System.out.println("Brute:");   print2D(bruteForce(mat1));
        System.out.println("Optimal:"); print2D(optimal(mat1));
        System.out.println("Best:");    print2D(best(mat1));
        // Expected: [[0,0,0],[0,1,0],[0,0,0]]

        System.out.println();
        int[][] mat2 = {{0,0,0},{0,1,0},{1,1,1}};
        System.out.println("Optimal:"); print2D(optimal(mat2));
        // Expected: [[0,0,0],[0,1,0],[1,2,1]]

        System.out.println();
        int[][] mat3 = {{1,1,1},{1,1,1},{1,1,0}};
        System.out.println("Far corner:"); print2D(optimal(mat3));
        // Expected: [[4,3,2],[3,2,1],[2,1,0]]
    }
}
