/**
 * Problem: Rat in a Maze
 * Difficulty: HARD | XP: 50
 *
 * Find all paths from (0,0) to (n-1,n-1) in an N×N grid.
 * Cell 1 = passable, 0 = blocked. Can move U/D/L/R.
 * Return sorted list of direction strings (e.g., "DDRR").
 * Real-life use: Pathfinding, maze solving, game AI.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE
    // Simple DFS/backtracking without any extra pruning.
    // Marks visited cells to avoid cycles.
    // Time: O(4^(N^2))  |  Space: O(N^2) visited + O(N^2) path
    // ============================================================
    static class BruteForce {
        private static final int[] DR = {1, -1, 0, 0};
        private static final int[] DC = {0, 0, -1, 1};
        private static final char[] DIR = {'D', 'U', 'L', 'R'};

        public static List<String> findPaths(int[][] maze) {
            int n = maze.length;
            List<String> result = new ArrayList<>();
            if (maze[0][0] == 0 || maze[n - 1][n - 1] == 0) return result;
            boolean[][] visited = new boolean[n][n];
            visited[0][0] = true;
            dfs(maze, n, 0, 0, new StringBuilder(), visited, result);
            Collections.sort(result);
            return result;
        }

        private static void dfs(int[][] maze, int n, int r, int c,
                                 StringBuilder path, boolean[][] visited, List<String> result) {
            if (r == n - 1 && c == n - 1) {
                result.add(path.toString());
                return;
            }
            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d], nc = c + DC[d];
                if (nr >= 0 && nr < n && nc >= 0 && nc < n
                        && !visited[nr][nc] && maze[nr][nc] == 1) {
                    visited[nr][nc] = true;
                    path.append(DIR[d]);
                    dfs(maze, n, nr, nc, path, visited, result);
                    path.deleteCharAt(path.length() - 1);
                    visited[nr][nc] = false;
                }
            }
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL
    // Backtracking: same as brute force but prunes if cell is 0 in-place
    // by temporarily marking maze[r][c] = 0 instead of using a separate visited array.
    // Saves O(N^2) extra space.
    // Time: O(4^(N^2))  |  Space: O(N^2) path
    // ============================================================
    static class Optimal {
        private static final int[] DR = {1, -1, 0, 0};
        private static final int[] DC = {0, 0, -1, 1};
        private static final char[] DIR = {'D', 'U', 'L', 'R'};

        public static List<String> findPaths(int[][] maze) {
            int n = maze.length;
            List<String> result = new ArrayList<>();
            if (maze[0][0] == 0 || maze[n - 1][n - 1] == 0) return result;
            dfs(maze, n, 0, 0, new StringBuilder(), result);
            Collections.sort(result);
            return result;
        }

        private static void dfs(int[][] maze, int n, int r, int c,
                                 StringBuilder path, List<String> result) {
            if (r == n - 1 && c == n - 1) {
                result.add(path.toString());
                return;
            }
            maze[r][c] = 0; // mark visited in-place
            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d], nc = c + DC[d];
                if (nr >= 0 && nr < n && nc >= 0 && nc < n && maze[nr][nc] == 1) {
                    path.append(DIR[d]);
                    dfs(maze, n, nr, nc, path, result);
                    path.deleteCharAt(path.length() - 1);
                }
            }
            maze[r][c] = 1; // restore
        }
    }

    // ============================================================
    // APPROACH 3: BEST
    // Same in-place backtracking but with direction order D<L<R<U
    // to yield lexicographically sorted paths naturally (no post-sort needed).
    // Time: O(4^(N^2))  |  Space: O(N^2) — same, just avoids Collections.sort()
    // ============================================================
    static class Best {
        // Directions in lexicographic order: D, L, R, U
        private static final int[] DR = {1,  0,  0, -1};
        private static final int[] DC = {0, -1,  1,  0};
        private static final char[] DIR = {'D', 'L', 'R', 'U'};

        public static List<String> findPaths(int[][] maze) {
            int n = maze.length;
            List<String> result = new ArrayList<>();
            if (maze[0][0] == 0 || maze[n - 1][n - 1] == 0) return result;
            dfs(maze, n, 0, 0, new StringBuilder(), result);
            return result;
        }

        private static void dfs(int[][] maze, int n, int r, int c,
                                 StringBuilder path, List<String> result) {
            if (r == n - 1 && c == n - 1) {
                result.add(path.toString());
                return;
            }
            maze[r][c] = 0;
            for (int d = 0; d < 4; d++) {
                int nr = r + DR[d], nc = c + DC[d];
                if (nr >= 0 && nr < n && nc >= 0 && nc < n && maze[nr][nc] == 1) {
                    path.append(DIR[d]);
                    dfs(maze, n, nr, nc, path, result);
                    path.deleteCharAt(path.length() - 1);
                }
            }
            maze[r][c] = 1;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Rat in a Maze ===");

        int[][] maze1 = {{1, 0, 0, 0}, {1, 1, 0, 1}, {1, 1, 0, 0}, {0, 1, 1, 1}};
        System.out.println("\n4x4 maze:");
        System.out.println("  Brute  : " + BruteForce.findPaths(deepCopy(maze1)));
        System.out.println("  Optimal: " + Optimal.findPaths(deepCopy(maze1)));
        System.out.println("  Best   : " + Best.findPaths(deepCopy(maze1)));

        int[][] maze2 = {{1, 1}, {1, 1}};
        System.out.println("\n2x2 fully open:");
        System.out.println("  Best: " + Best.findPaths(deepCopy(maze2)));

        int[][] maze3 = {{1, 0}, {0, 1}};
        System.out.println("\n2x2 blocked (no path):");
        System.out.println("  Best: " + Best.findPaths(deepCopy(maze3)));
    }

    private static int[][] deepCopy(int[][] m) {
        int[][] copy = new int[m.length][];
        for (int i = 0; i < m.length; i++) copy[i] = m[i].clone();
        return copy;
    }
}
