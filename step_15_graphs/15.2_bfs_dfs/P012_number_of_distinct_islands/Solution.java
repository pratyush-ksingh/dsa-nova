/**
 * Problem: Number of Distinct Islands (LeetCode 694)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a 2D binary grid (1=land, 0=water), count the number of DISTINCT islands.
 * Two islands are the same if one can be translated to match the other exactly.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static final int[][] DIRS = {{-1,0},{1,0},{0,-1},{0,1}};

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Normalize coordinates, store as set
    // Time: O(mn * k log k)  |  Space: O(mn)
    // ============================================================
    static class BruteForce {
        /**
         * For each island, collect absolute (r, c) coordinates.
         * Translate so the minimum (r, c) is at (0, 0).
         * Store the resulting frozenset of offsets in a global set.
         * Two islands with identical normalized coordinate sets are the same.
         */
        int m, n;
        boolean[][] visited;
        int[][] grid;

        void dfs(int r, int c, List<int[]> coords) {
            if (r<0||r>=m||c<0||c>=n||visited[r][c]||grid[r][c]!=1) return;
            visited[r][c] = true;
            coords.add(new int[]{r, c});
            for (int[] d : DIRS) dfs(r+d[0], c+d[1], coords);
        }

        int numDistinctIslands(int[][] g) {
            grid = g; m = g.length; n = g[0].length;
            visited = new boolean[m][n];
            Set<Set<String>> shapes = new HashSet<>();

            for (int r = 0; r < m; r++) {
                for (int c = 0; c < n; c++) {
                    if (g[r][c]==1 && !visited[r][c]) {
                        List<int[]> coords = new ArrayList<>();
                        dfs(r, c, coords);
                        if (!coords.isEmpty()) {
                            int minR = coords.stream().mapToInt(x->x[0]).min().getAsInt();
                            int minC = coords.stream().mapToInt(x->x[1]).min().getAsInt();
                            Set<String> shape = new HashSet<>();
                            for (int[] p : coords)
                                shape.add((p[0]-minR) + "," + (p[1]-minC));
                            shapes.add(shape);
                        }
                    }
                }
            }
            return shapes.size();
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — DFS path signature encoding
    // Time: O(mn)  |  Space: O(mn)
    // ============================================================
    static class Optimal {
        /**
         * Encode each island as the sequence of DFS direction characters plus
         * 'B' (backtrack) markers. Two islands produce identical strings iff
         * they have the same shape under translation. The backtrack markers
         * prevent ambiguous encodings.
         *
         * Directions: U=up, D=down, L=left, R=right, B=backtrack, S=start
         */
        int m, n;
        boolean[][] visited;
        int[][] grid;
        StringBuilder path;

        void dfs(int r, int c, char dir) {
            if (r<0||r>=m||c<0||c>=n||visited[r][c]||grid[r][c]!=1) return;
            visited[r][c] = true;
            path.append(dir);
            dfs(r-1, c, 'U');
            dfs(r+1, c, 'D');
            dfs(r, c-1, 'L');
            dfs(r, c+1, 'R');
            path.append('B');
        }

        int numDistinctIslands(int[][] g) {
            grid = g; m = g.length; n = g[0].length;
            visited = new boolean[m][n];
            Set<String> shapes = new HashSet<>();

            for (int r = 0; r < m; r++) {
                for (int c = 0; c < n; c++) {
                    if (g[r][c]==1 && !visited[r][c]) {
                        path = new StringBuilder();
                        dfs(r, c, 'S');
                        shapes.add(path.toString());
                    }
                }
            }
            return shapes.size();
        }
    }

    // ============================================================
    // APPROACH 3: BEST — DFS with relative offset set
    // Time: O(mn)  |  Space: O(mn)
    // ============================================================
    static class Best {
        /**
         * Record offsets of each cell from the DFS root (first cell visited
         * in the island). Store as a Set<String> of "dr,dc" pairs.
         * Clean, direct, and avoids backtrack-marker complexity.
         */
        int m, n;
        boolean[][] visited;
        int[][] grid;

        void dfs(int r, int c, int r0, int c0, Set<String> offsets) {
            if (r<0||r>=m||c<0||c>=n||visited[r][c]||grid[r][c]!=1) return;
            visited[r][c] = true;
            offsets.add((r-r0) + "," + (c-c0));
            for (int[] d : DIRS) dfs(r+d[0], c+d[1], r0, c0, offsets);
        }

        int numDistinctIslands(int[][] g) {
            grid = g; m = g.length; n = g[0].length;
            visited = new boolean[m][n];
            Set<Set<String>> shapes = new HashSet<>();

            for (int r = 0; r < m; r++) {
                for (int c = 0; c < n; c++) {
                    if (g[r][c]==1 && !visited[r][c]) {
                        Set<String> offsets = new HashSet<>();
                        dfs(r, c, r, c, offsets);
                        shapes.add(offsets);
                    }
                }
            }
            return shapes.size();
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Number of Distinct Islands ===\n");
        int[][] g1 = {{1,1,0,0,0},{1,1,0,0,0},{0,0,0,1,1},{0,0,0,1,1}};
        // Expected: 1 (both 2x2 squares are identical)
        System.out.println("grid1 Brute:   " + new BruteForce().numDistinctIslands(deepCopy(g1)));
        System.out.println("grid1 Optimal: " + new Optimal().numDistinctIslands(deepCopy(g1)));
        System.out.println("grid1 Best:    " + new Best().numDistinctIslands(deepCopy(g1)));

        int[][] g2 = {{1,1,0,1,1},{1,0,0,0,1},{0,0,0,0,1},{1,1,0,1,1}};
        // Expected: 3
        System.out.println("\ngrid2 Brute:   " + new BruteForce().numDistinctIslands(deepCopy(g2)));
        System.out.println("grid2 Optimal: " + new Optimal().numDistinctIslands(deepCopy(g2)));
        System.out.println("grid2 Best:    " + new Best().numDistinctIslands(deepCopy(g2)));
    }

    static int[][] deepCopy(int[][] g) {
        int[][] c = new int[g.length][];
        for (int i = 0; i < g.length; i++) c[i] = g[i].clone();
        return c;
    }
}
