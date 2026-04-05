import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Re-run BFS/DFS after each land addition
// Time: O(K * M * N)  |  Space: O(M * N)
// After each land cell is added, run BFS to count connected components
// ============================================================
class BruteForce {
    private static int[][] grid;
    private static int M, N;

    public static List<Integer> solve(int m, int n, int[][] positions) {
        M = m; N = n;
        grid = new int[m][n];
        List<Integer> result = new ArrayList<>();
        for (int[] pos : positions) {
            grid[pos[0]][pos[1]] = 1;
            result.add(countIslands());
        }
        return result;
    }

    private static int countIslands() {
        boolean[][] visited = new boolean[M][N];
        int count = 0;
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 1 && !visited[i][j]) {
                    bfs(i, j, visited);
                    count++;
                }
            }
        }
        return count;
    }

    private static void bfs(int r, int c, boolean[][] visited) {
        Queue<int[]> q = new LinkedList<>();
        q.offer(new int[]{r, c});
        visited[r][c] = true;
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            for (int[] d : dirs) {
                int nr = cur[0] + d[0], nc = cur[1] + d[1];
                if (nr >= 0 && nr < M && nc >= 0 && nc < N && grid[nr][nc] == 1 && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    q.offer(new int[]{nr, nc});
                }
            }
        }
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - Union-Find (Disjoint Set Union)
// Time: O(K * alpha(M*N))  |  Space: O(M * N)
// Add each land cell, union with adjacent land cells, track island count
// ============================================================
class Optimal {
    static int[] parent, rank;
    static int islands;

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
        islands--;
    }

    public static List<Integer> solve(int m, int n, int[][] positions) {
        parent = new int[m * n];
        rank = new int[m * n];
        Arrays.fill(parent, -1);
        islands = 0;
        List<Integer> result = new ArrayList<>();
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

        for (int[] pos : positions) {
            int r = pos[0], c = pos[1];
            int id = r * n + c;
            if (parent[id] != -1) {
                // Already land (duplicate)
                result.add(islands);
                continue;
            }
            parent[id] = id;
            rank[id] = 0;
            islands++;
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < m && nc >= 0 && nc < n) {
                    int nid = nr * n + nc;
                    if (parent[nid] != -1) union(id, nid);
                }
            }
            result.add(islands);
        }
        return result;
    }
}

// ============================================================
// APPROACH 3: BEST - Union-Find with path compression + rank (same as Optimal)
// Time: O(K * alpha(M*N))  |  Space: O(M * N)
// Using a cleaner DSU class
// ============================================================
class Best {
    static class DSU {
        int[] parent, rank;
        int count;

        DSU(int n) {
            parent = new int[n];
            rank = new int[n];
            Arrays.fill(parent, -1);
            count = 0;
        }

        int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }

        void add(int x) {
            parent[x] = x;
            rank[x] = 0;
            count++;
        }

        boolean isLand(int x) { return parent[x] != -1; }

        void union(int x, int y) {
            int px = find(x), py = find(y);
            if (px == py) return;
            if (rank[px] < rank[py]) { int t = px; px = py; py = t; }
            parent[py] = px;
            if (rank[px] == rank[py]) rank[px]++;
            count--;
        }
    }

    public static List<Integer> solve(int m, int n, int[][] positions) {
        DSU dsu = new DSU(m * n);
        List<Integer> result = new ArrayList<>();
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

        for (int[] pos : positions) {
            int r = pos[0], c = pos[1], id = r * n + c;
            if (!dsu.isLand(id)) {
                dsu.add(id);
                for (int[] d : dirs) {
                    int nr = r + d[0], nc = c + d[1];
                    if (nr >= 0 && nr < m && nc >= 0 && nc < n) {
                        int nid = nr * n + nc;
                        if (dsu.isLand(nid)) dsu.union(id, nid);
                    }
                }
            }
            result.add(dsu.count);
        }
        return result;
    }
}

public class Solution {
    public static void main(String[] args) {
        int m = 3, n = 3;
        int[][] positions = {{0,0},{0,1},{1,2},{2,1},{1,1}};
        System.out.println("BruteForce: " + BruteForce.solve(m, n, positions));  // [1,1,2,3,1]
        System.out.println("Optimal:    " + Optimal.solve(m, n, positions));
        System.out.println("Best:       " + Best.solve(m, n, positions));

        // LeetCode example: m=3,n=3, positions=[[0,0],[0,1],[1,2],[1,2]]
        // Expected: [1,1,2,2]
        int[][] pos2 = {{0,0},{0,1},{1,2},{1,2}};
        System.out.println("Optimal (dup): " + Optimal.solve(m, n, pos2));
    }
}
