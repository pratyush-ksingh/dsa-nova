import java.util.*;

// ============================================================
// APPROACH 1: BRUTE FORCE - Try flipping each 0, count resulting island
// Time: O(N^4)  |  Space: O(N^2)
// For each 0, temporarily set to 1 and BFS/DFS to count connected component
// ============================================================
class BruteForce {
    private static int N;

    public static int solve(int[][] grid) {
        N = grid.length;
        int maxSize = 0;
        // Check if already all 1s
        boolean hasZero = false;
        for (int[] row : grid) for (int v : row) if (v == 0) { hasZero = true; break; }
        if (!hasZero) return N * N;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 0) {
                    grid[i][j] = 1;
                    maxSize = Math.max(maxSize, bfsSize(grid, i, j));
                    grid[i][j] = 0;
                }
            }
        }
        return maxSize;
    }

    private static int bfsSize(int[][] grid, int sr, int sc) {
        Queue<int[]> q = new LinkedList<>();
        boolean[][] visited = new boolean[N][N];
        q.offer(new int[]{sr, sc});
        visited[sr][sc] = true;
        int size = 0;
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            size++;
            for (int[] d : dirs) {
                int nr = cur[0] + d[0], nc = cur[1] + d[1];
                if (nr >= 0 && nr < N && nc >= 0 && nc < N && grid[nr][nc] == 1 && !visited[nr][nc]) {
                    visited[nr][nc] = true;
                    q.offer(new int[]{nr, nc});
                }
            }
        }
        return size;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL - DFS to label islands + check neighbors of each 0
// Time: O(N^2)  |  Space: O(N^2)
// Label each island with unique ID and precompute sizes.
// For each 0, sum up distinct neighboring island sizes + 1.
// ============================================================
class Optimal {
    private static int N;
    private static int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

    public static int solve(int[][] grid) {
        N = grid.length;
        int[][] label = new int[N][N];
        Map<Integer, Integer> islandSize = new HashMap<>();
        int islandId = 2; // start from 2 (0=water,1=land unlabeled)

        // Label all islands
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 1 && label[i][j] == 0) {
                    int size = dfs(grid, label, i, j, islandId);
                    islandSize.put(islandId, size);
                    islandId++;
                }
            }
        }

        int maxSize = islandSize.values().stream().mapToInt(x -> x).max().orElse(0);

        // Try flipping each 0
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 0) {
                    Set<Integer> seen = new HashSet<>();
                    int total = 1; // the flipped cell itself
                    for (int[] d : dirs) {
                        int nr = i + d[0], nc = j + d[1];
                        if (nr >= 0 && nr < N && nc >= 0 && nc < N && label[nr][nc] > 0) {
                            int id = label[nr][nc];
                            if (seen.add(id)) total += islandSize.get(id);
                        }
                    }
                    maxSize = Math.max(maxSize, total);
                }
            }
        }
        return maxSize;
    }

    private static int dfs(int[][] grid, int[][] label, int r, int c, int id) {
        if (r < 0 || r >= N || c < 0 || c >= N || grid[r][c] != 1 || label[r][c] != 0) return 0;
        label[r][c] = id;
        int size = 1;
        for (int[] d : dirs) size += dfs(grid, label, r + d[0], c + d[1], id);
        return size;
    }
}

// ============================================================
// APPROACH 3: BEST - Union-Find with size tracking
// Time: O(N^2 * alpha(N^2))  |  Space: O(N^2)
// Use DSU to build islands, then for each 0 check union of neighbors
// ============================================================
class Best {
    static int[] parent, size;
    static int N;

    private static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    private static void union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return;
        if (size[px] < size[py]) { int t = px; px = py; py = t; }
        parent[py] = px;
        size[px] += size[py];
    }

    public static int solve(int[][] grid) {
        N = grid.length;
        parent = new int[N * N];
        size = new int[N * N];
        for (int i = 0; i < N * N; i++) { parent[i] = i; size[i] = 1; }

        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 1) {
                    for (int[] d : dirs) {
                        int nr = i + d[0], nc = j + d[1];
                        if (nr >= 0 && nr < N && nc >= 0 && nc < N && grid[nr][nc] == 1) {
                            union(i * N + j, nr * N + nc);
                        }
                    }
                }
            }
        }

        int maxSize = 0;
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (grid[i][j] == 1) maxSize = Math.max(maxSize, size[find(i * N + j)]);

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 0) {
                    Set<Integer> seen = new HashSet<>();
                    int total = 1;
                    for (int[] d : dirs) {
                        int nr = i + d[0], nc = j + d[1];
                        if (nr >= 0 && nr < N && nc >= 0 && nc < N && grid[nr][nc] == 1) {
                            int root = find(nr * N + nc);
                            if (seen.add(root)) total += size[root];
                        }
                    }
                    maxSize = Math.max(maxSize, total);
                }
            }
        }
        return maxSize;
    }
}

public class Solution {
    public static void main(String[] args) {
        int[][] g1 = {{1,0},{0,1}};
        int[][] g2 = {{1,1},{1,0}};
        int[][] g3 = {{1,1},{1,1}};

        System.out.println("Test 1 [[1,0],[0,1]] expected 3:");
        System.out.println("  Brute=" + BruteForce.solve(copyGrid(g1)) + " Optimal=" + Optimal.solve(copyGrid(g1)) + " Best=" + Best.solve(copyGrid(g1)));
        System.out.println("Test 2 [[1,1],[1,0]] expected 4:");
        System.out.println("  Brute=" + BruteForce.solve(copyGrid(g2)) + " Optimal=" + Optimal.solve(copyGrid(g2)) + " Best=" + Best.solve(copyGrid(g2)));
        System.out.println("Test 3 [[1,1],[1,1]] expected 4:");
        System.out.println("  Optimal=" + Optimal.solve(copyGrid(g3)) + " Best=" + Best.solve(copyGrid(g3)));
    }

    static int[][] copyGrid(int[][] g) {
        int[][] copy = new int[g.length][];
        for (int i = 0; i < g.length; i++) copy[i] = g[i].clone();
        return copy;
    }
}
