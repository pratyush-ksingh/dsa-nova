import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Binary search on time + BFS
// Time: O(N^2 log N)  |  Space: O(N^2)
// Binary search on the answer T; check if (0,0)->(N-1,N-1) reachable
// using only cells with elevation <= T
// ============================================================
class BruteForce {
    public static int solve(int[][] grid) {
        int n = grid.length;
        int lo = grid[0][0], hi = n * n - 1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            if (canReach(grid, n, mid)) hi = mid;
            else lo = mid + 1;
        }
        return lo;
    }

    private static boolean canReach(int[][] grid, int n, int t) {
        if (grid[0][0] > t) return false;
        boolean[][] visited = new boolean[n][n];
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{0, 0});
        visited[0][0] = true;
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            if (cur[0] == n-1 && cur[1] == n-1) return true;
            for (int[] d : dirs) {
                int nr = cur[0]+d[0], nc = cur[1]+d[1];
                if (nr >= 0 && nr < n && nc >= 0 && nc < n && !visited[nr][nc] && grid[nr][nc] <= t) {
                    visited[nr][nc] = true;
                    q.offer(new int[]{nr, nc});
                }
            }
        }
        return false;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Dijkstra-like with min-heap
// Time: O(N^2 log N)  |  Space: O(N^2)
// Treat cell elevation as the "cost". Use a min-heap to always
// extend the path through the cell with minimum max-elevation seen so far.
// ============================================================
class Optimal {
    public static int solve(int[][] grid) {
        int n = grid.length;
        int[][] dist = new int[n][n];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);
        dist[0][0] = grid[0][0];
        // PQ: [max_elevation_on_path, row, col]
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{grid[0][0], 0, 0});
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int d = cur[0], r = cur[1], c = cur[2];
            if (d > dist[r][c]) continue;
            if (r == n-1 && c == n-1) return d;
            for (int[] dir : dirs) {
                int nr = r+dir[0], nc = c+dir[1];
                if (nr >= 0 && nr < n && nc >= 0 && nc < n) {
                    int nd = Math.max(d, grid[nr][nc]);
                    if (nd < dist[nr][nc]) {
                        dist[nr][nc] = nd;
                        pq.offer(new int[]{nd, nr, nc});
                    }
                }
            }
        }
        return dist[n-1][n-1];
    }
}

// ============================================================
// APPROACH 3: BEST - Union-Find: add cells in order of elevation
// Time: O(N^2 * alpha(N^2))  |  Space: O(N^2)
// Sort all cells by elevation. Add them one by one. Stop when (0,0) connects to (N-1,N-1).
// ============================================================
class Best {
    static int[] parent, rank;

    private static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    private static void union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return;
        if (rank[px] < rank[py]) { int t = px; px = py; py = t; }
        parent[py] = px;
        if (rank[px] == rank[py]) rank[px]++;
    }

    public static int solve(int[][] grid) {
        int n = grid.length;
        parent = new int[n * n];
        rank = new int[n * n];
        boolean[] inGrid = new boolean[n * n];
        for (int i = 0; i < n * n; i++) parent[i] = i;

        // Sort cells by elevation
        Integer[] cells = new Integer[n * n];
        for (int i = 0; i < n * n; i++) cells[i] = i;
        Arrays.sort(cells, (a, b) -> grid[a / n][a % n] - grid[b / n][b % n]);

        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        for (int cell : cells) {
            int r = cell / n, c = cell % n;
            inGrid[cell] = true;
            for (int[] d : dirs) {
                int nr = r+d[0], nc = c+d[1];
                int nid = nr * n + nc;
                if (nr >= 0 && nr < n && nc >= 0 && nc < n && inGrid[nid]) {
                    union(cell, nid);
                }
            }
            if (find(0) == find(n * n - 1)) return grid[r][c];
        }
        return -1;
    }
}

public class Solution {
    public static void main(String[] args) {
        int[][] g1 = {{0,2},{1,3}};
        System.out.println("Test 1 [[0,2],[1,3]] expected 3:");
        System.out.println("  Brute=" + BruteForce.solve(g1) + " Optimal=" + Optimal.solve(g1) + " Best=" + Best.solve(g1));

        int[][] g2 = {
            {0,1,2,3,4},
            {24,23,22,21,5},
            {12,13,14,15,16},
            {11,17,18,19,20},   // fixed: was wrong
            {10,9,8,7,6}
        };
        // Actually LeetCode example: [[0,1,2,3,4],[24,23,22,21,5],[12,13,14,15,16],[11,17,18,19,20],[10,9,8,7,6]] -> 16
        System.out.println("Test 2 (5x5 spiral) expected 16:");
        System.out.println("  Brute=" + BruteForce.solve(g2) + " Optimal=" + Optimal.solve(g2) + " Best=" + Best.solve(g2));
    }
}
