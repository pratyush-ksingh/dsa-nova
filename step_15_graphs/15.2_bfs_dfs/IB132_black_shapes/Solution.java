import java.util.*;

/**
 * Problem: Black Shapes
 * Difficulty: MEDIUM | XP: 25
 * Source: InterviewBit
 *
 * Given a character grid of 'X' (black) and 'O' (white),
 * count the number of connected components formed by 'X' characters.
 *
 * @author DSA_Nova
 */

// ============================================================
// APPROACH 1: BRUTE FORCE
// DFS flood fill — recursively mark connected 'X' cells
// Time: O(M*N)  |  Space: O(M*N) recursion stack
// ============================================================
class BruteForce {
    static char[][] grid;
    static int rows, cols;

    static void dfs(int r, int c) {
        if (r < 0 || r >= rows || c < 0 || c >= cols || grid[r][c] != 'X') return;
        grid[r][c] = '#'; // mark visited
        dfs(r + 1, c);
        dfs(r - 1, c);
        dfs(r, c + 1);
        dfs(r, c - 1);
    }

    static int countBlackShapes(char[][] A) {
        if (A == null || A.length == 0) return 0;
        // Deep copy to avoid mutation of input
        rows = A.length;
        cols = A[0].length;
        grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) grid[i] = A[i].clone();

        int count = 0;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 'X') {
                    dfs(r, c);
                    count++;
                }
            }
        }
        return count;
    }
}

// ============================================================
// APPROACH 2: OPTIMAL
// BFS flood fill — iterative, avoids stack overflow for large grids
// Time: O(M*N)  |  Space: O(min(M,N)) BFS queue
// ============================================================
class Optimal {
    static int countBlackShapes(char[][] A) {
        if (A == null || A.length == 0) return 0;
        int rows = A.length, cols = A[0].length;
        char[][] grid = new char[rows][cols];
        for (int i = 0; i < rows; i++) grid[i] = A[i].clone();

        int[] dr = {0, 0, 1, -1};
        int[] dc = {1, -1, 0, 0};
        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (grid[r][c] == 'X') {
                    count++;
                    Queue<int[]> queue = new LinkedList<>();
                    queue.offer(new int[]{r, c});
                    grid[r][c] = '#';
                    while (!queue.isEmpty()) {
                        int[] cur = queue.poll();
                        for (int d = 0; d < 4; d++) {
                            int nr = cur[0] + dr[d];
                            int nc = cur[1] + dc[d];
                            if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] == 'X') {
                                grid[nr][nc] = '#';
                                queue.offer(new int[]{nr, nc});
                            }
                        }
                    }
                }
            }
        }
        return count;
    }
}

// ============================================================
// APPROACH 3: BEST
// Union-Find for connected components — O(alpha) per operation
// Time: O(M*N * alpha)  |  Space: O(M*N)
// ============================================================
class Best {
    static int[] parent, rnk;

    static int find(int x) {
        if (parent[x] != x) parent[x] = find(parent[x]);
        return parent[x];
    }

    static void union(int x, int y) {
        int px = find(x), py = find(y);
        if (px == py) return;
        if (rnk[px] < rnk[py]) { int t = px; px = py; py = t; }
        parent[py] = px;
        if (rnk[px] == rnk[py]) rnk[px]++;
    }

    static int countBlackShapes(char[][] A) {
        if (A == null || A.length == 0) return 0;
        int rows = A.length, cols = A[0].length;
        int total = rows * cols;
        parent = new int[total];
        rnk = new int[total];
        for (int i = 0; i < total; i++) parent[i] = i;

        int[] dr = {0, 1};
        int[] dc = {1, 0};

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (A[r][c] == 'X') {
                    for (int d = 0; d < 2; d++) {
                        int nr = r + dr[d], nc = c + dc[d];
                        if (nr < rows && nc < cols && A[nr][nc] == 'X') {
                            union(r * cols + c, nr * cols + nc);
                        }
                    }
                }
            }
        }

        Set<Integer> components = new HashSet<>();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (A[r][c] == 'X') components.add(find(r * cols + c));
            }
        }
        return components.size();
    }
}

public class Solution {
    public static void main(String[] args) {
        System.out.println("=== Black Shapes ===");

        char[][] grid1 = {
            {'X', 'O', 'X'},
            {'O', 'O', 'O'},
            {'X', 'O', 'X'}
        };
        System.out.println("BruteForce grid1: " + BruteForce.countBlackShapes(grid1));  // 4
        System.out.println("Optimal    grid1: " + Optimal.countBlackShapes(grid1));     // 4
        System.out.println("Best       grid1: " + Best.countBlackShapes(grid1));        // 4

        char[][] grid2 = {
            {'X', 'X'},
            {'X', 'O'}
        };
        System.out.println("BruteForce grid2: " + BruteForce.countBlackShapes(grid2));  // 1
        System.out.println("Optimal    grid2: " + Optimal.countBlackShapes(grid2));     // 1

        char[][] grid3 = {{'O', 'O'}, {'O', 'O'}};
        System.out.println("All O's: " + Optimal.countBlackShapes(grid3));  // 0
    }
}
