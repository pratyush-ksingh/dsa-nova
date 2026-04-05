/**
 * Problem: Path with Minimum Effort (LeetCode #1631)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a rows x columns grid of heights, find the minimum effort path
 * from top-left to bottom-right. Effort = max absolute height difference
 * between consecutive cells along the path.
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    static final int[][] DIRS = {{0,1},{0,-1},{1,0},{-1,0}};

    // ============================================================
    // APPROACH 1: BRUTE FORCE (DFS trying all paths)
    // Time: O(4^(m*n)) worst case  |  Space: O(m * n)
    // ============================================================
    static class BruteForce {
        int result;

        public int minimumEffortPath(int[][] heights) {
            int rows = heights.length, cols = heights[0].length;
            result = Integer.MAX_VALUE;
            boolean[][] visited = new boolean[rows][cols];
            visited[0][0] = true;
            dfs(heights, 0, 0, 0, visited);
            return result;
        }

        private void dfs(int[][] heights, int r, int c, int maxEffort, boolean[][] visited) {
            int rows = heights.length, cols = heights[0].length;
            if (r == rows - 1 && c == cols - 1) {
                result = Math.min(result, maxEffort);
                return;
            }
            if (maxEffort >= result) return; // prune

            for (int[] d : DIRS) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc]) {
                    int effort = Math.abs(heights[nr][nc] - heights[r][c]);
                    int newMax = Math.max(maxEffort, effort);
                    if (newMax < result) {
                        visited[nr][nc] = true;
                        dfs(heights, nr, nc, newMax, visited);
                        visited[nr][nc] = false;
                    }
                }
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (Dijkstra with priority queue)
    // Time: O(m * n * log(m * n))  |  Space: O(m * n)
    // ============================================================
    static class Optimal {
        public int minimumEffortPath(int[][] heights) {
            int rows = heights.length, cols = heights[0].length;
            if (rows == 1 && cols == 1) return 0;

            int[][] dist = new int[rows][cols];
            for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
            dist[0][0] = 0;

            // Min-heap: [effort, row, col]
            PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
            pq.offer(new int[]{0, 0, 0});

            while (!pq.isEmpty()) {
                int[] curr = pq.poll();
                int effort = curr[0], r = curr[1], c = curr[2];

                if (r == rows - 1 && c == cols - 1) return effort;
                if (effort > dist[r][c]) continue;

                for (int[] d : DIRS) {
                    int nr = r + d[0], nc = c + d[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols) {
                        int newEffort = Math.max(effort, Math.abs(heights[nr][nc] - heights[r][c]));
                        if (newEffort < dist[nr][nc]) {
                            dist[nr][nc] = newEffort;
                            pq.offer(new int[]{newEffort, nr, nc});
                        }
                    }
                }
            }
            return dist[rows - 1][cols - 1];
        }
    }

    // ============================================================
    // APPROACH 3: BEST (Binary search + BFS feasibility check)
    // Time: O(m * n * log(max_height))  |  Space: O(m * n)
    // ============================================================
    static class Best {
        public int minimumEffortPath(int[][] heights) {
            int rows = heights.length, cols = heights[0].length;

            // Find max height for binary search range
            int maxH = 0;
            for (int[] row : heights) {
                for (int h : row) {
                    maxH = Math.max(maxH, h);
                }
            }

            int lo = 0, hi = maxH;
            while (lo < hi) {
                int mid = lo + (hi - lo) / 2;
                if (canReach(heights, mid)) {
                    hi = mid;
                } else {
                    lo = mid + 1;
                }
            }
            return lo;
        }

        private boolean canReach(int[][] heights, int maxEffort) {
            int rows = heights.length, cols = heights[0].length;
            if (rows == 1 && cols == 1) return true;

            boolean[][] visited = new boolean[rows][cols];
            visited[0][0] = true;
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{0, 0});

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int r = curr[0], c = curr[1];
                if (r == rows - 1 && c == cols - 1) return true;

                for (int[] d : DIRS) {
                    int nr = r + d[0], nc = c + d[1];
                    if (nr >= 0 && nr < rows && nc >= 0 && nc < cols
                            && !visited[nr][nc]
                            && Math.abs(heights[nr][nc] - heights[r][c]) <= maxEffort) {
                        visited[nr][nc] = true;
                        queue.offer(new int[]{nr, nc});
                    }
                }
            }
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Path with Minimum Effort ===\n");

        BruteForce bf = new BruteForce();
        Optimal opt = new Optimal();
        Best best = new Best();

        // Test 1
        int[][] h1 = {{1,2,2},{3,8,2},{5,3,5}};
        System.out.println("Test 1: Expected 2");
        System.out.println("  Brute:    " + bf.minimumEffortPath(h1));
        System.out.println("  Dijkstra: " + opt.minimumEffortPath(h1));
        System.out.println("  BinSearch: " + best.minimumEffortPath(h1));

        // Test 2
        int[][] h2 = {{1,2,3},{3,8,4},{5,3,5}};
        System.out.println("\nTest 2: Expected 1");
        System.out.println("  Brute:    " + new BruteForce().minimumEffortPath(h2));
        System.out.println("  Dijkstra: " + new Optimal().minimumEffortPath(h2));
        System.out.println("  BinSearch: " + new Best().minimumEffortPath(h2));

        // Test 3
        int[][] h3 = {{1,2,1,1,1},{1,2,1,2,1},{1,2,1,2,1},{1,2,1,2,1},{1,1,1,2,1}};
        System.out.println("\nTest 3: Expected 0");
        System.out.println("  Brute:    " + new BruteForce().minimumEffortPath(h3));
        System.out.println("  Dijkstra: " + new Optimal().minimumEffortPath(h3));
        System.out.println("  BinSearch: " + new Best().minimumEffortPath(h3));

        // Test 4: single cell
        int[][] h4 = {{5}};
        System.out.println("\nTest 4 (single): Expected 0");
        System.out.println("  Brute:    " + new BruteForce().minimumEffortPath(h4));
        System.out.println("  Dijkstra: " + new Optimal().minimumEffortPath(h4));
        System.out.println("  BinSearch: " + new Best().minimumEffortPath(h4));
    }
}
