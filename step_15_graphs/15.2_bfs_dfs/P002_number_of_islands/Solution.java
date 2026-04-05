/**
 * Problem: Number of Islands (LeetCode #200)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a 2D grid of '1's (land) and '0's (water), count the number
 * of islands. An island is formed by connecting adjacent lands
 * horizontally or vertically.
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: DFS FLOOD FILL
    // Time: O(m * n)  |  Space: O(m * n) recursion stack worst case
    // ============================================================
    public static int numIslandsDFS(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int m = grid.length, n = grid[0].length;
        int count = 0;

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == '1') {
                    count++;
                    dfs(grid, r, c, m, n);
                }
            }
        }
        return count;
    }

    private static void dfs(char[][] grid, int r, int c, int m, int n) {
        // Base case: out of bounds or water
        if (r < 0 || r >= m || c < 0 || c >= n || grid[r][c] != '1') return;

        grid[r][c] = '0'; // Sink the land (mark visited)

        dfs(grid, r + 1, c, m, n); // down
        dfs(grid, r - 1, c, m, n); // up
        dfs(grid, r, c + 1, m, n); // right
        dfs(grid, r, c - 1, m, n); // left
    }

    // ============================================================
    // APPROACH 2: BFS FLOOD FILL
    // Time: O(m * n)  |  Space: O(min(m, n)) queue
    // ============================================================
    public static int numIslandsBFS(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int m = grid.length, n = grid[0].length;
        int count = 0;
        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == '1') {
                    count++;
                    grid[r][c] = '0'; // Mark BEFORE enqueue to prevent duplicates
                    Queue<int[]> queue = new LinkedList<>();
                    queue.offer(new int[]{r, c});

                    while (!queue.isEmpty()) {
                        int[] cell = queue.poll();
                        for (int[] d : dirs) {
                            int nr = cell[0] + d[0];
                            int nc = cell[1] + d[1];
                            if (nr >= 0 && nr < m && nc >= 0 && nc < n
                                    && grid[nr][nc] == '1') {
                                grid[nr][nc] = '0'; // Mark BEFORE enqueue
                                queue.offer(new int[]{nr, nc});
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    // ============================================================
    // APPROACH 3: UNION-FIND
    // Time: O(m * n * alpha(mn)) ~ O(m * n)  |  Space: O(m * n)
    // ============================================================
    static int[] parent;
    static int[] rank;

    public static int numIslandsUnionFind(char[][] grid) {
        if (grid == null || grid.length == 0) return 0;

        int m = grid.length, n = grid[0].length;
        parent = new int[m * n];
        rank = new int[m * n];
        int count = 0;

        // Initialize: each land cell is its own component
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == '1') {
                    int id = r * n + c;
                    parent[id] = id;
                    count++;
                }
            }
        }

        // Union adjacent land cells (only check right and down to avoid duplicates)
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (grid[r][c] == '1') {
                    int id = r * n + c;
                    // Check right neighbor
                    if (c + 1 < n && grid[r][c + 1] == '1') {
                        if (union(id, id + 1)) count--;
                    }
                    // Check down neighbor
                    if (r + 1 < m && grid[r + 1][c] == '1') {
                        if (union(id, id + n)) count--;
                    }
                }
            }
        }
        return count;
    }

    private static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // Path compression
        }
        return parent[x];
    }

    private static boolean union(int x, int y) {
        int rootX = find(x), rootY = find(y);
        if (rootX == rootY) return false; // Already same component

        // Union by rank
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        return true; // Merged two different components
    }

    // Helper: deep copy grid (since DFS/BFS modify it in place)
    private static char[][] copyGrid(char[][] grid) {
        char[][] copy = new char[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = grid[i].clone();
        }
        return copy;
    }

    public static void main(String[] args) {
        System.out.println("=== Number of Islands ===\n");

        char[][] g1 = {
            {'1','1','1','1','0'},
            {'1','1','0','1','0'},
            {'1','1','0','0','0'},
            {'0','0','0','0','0'}
        };
        System.out.println("Test 1: Expected 1");
        System.out.println("  DFS: " + numIslandsDFS(copyGrid(g1)));
        System.out.println("  BFS: " + numIslandsBFS(copyGrid(g1)));
        System.out.println("  UF:  " + numIslandsUnionFind(copyGrid(g1)));

        char[][] g2 = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        System.out.println("\nTest 2: Expected 3");
        System.out.println("  DFS: " + numIslandsDFS(copyGrid(g2)));
        System.out.println("  BFS: " + numIslandsBFS(copyGrid(g2)));
        System.out.println("  UF:  " + numIslandsUnionFind(copyGrid(g2)));

        char[][] g3 = {{'1'}};
        System.out.println("\nTest 3: Expected 1");
        System.out.println("  DFS: " + numIslandsDFS(copyGrid(g3)));

        char[][] g4 = {{'0'}};
        System.out.println("\nTest 4: Expected 0");
        System.out.println("  DFS: " + numIslandsDFS(copyGrid(g4)));

        char[][] g5 = {};
        System.out.println("\nTest 5 (empty): Expected 0");
        System.out.println("  DFS: " + numIslandsDFS(g5));
    }
}
