/**
 * Problem: Is Graph Bipartite? (LeetCode 785)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an undirected graph as an adjacency list, determine if it is bipartite.
 * A graph is bipartite iff it can be 2-colored with no two adjacent vertices
 * sharing the same color (equivalently: no odd-length cycles).
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Try all 2^n colorings
    // Time: O(2^n * (V + E))  |  Space: O(n)
    // ============================================================
    static class BruteForce {
        /**
         * Enumerate all 2^n assignments of vertices to two groups (0 or 1).
         * For each assignment, verify that every edge connects vertices of
         * different colors. Return true if any valid coloring exists.
         * Purely educational — impractical for large graphs.
         */
        boolean isBipartite(int[][] graph) {
            int n = graph.length;
            for (int mask = 0; mask < (1 << n); mask++) {
                boolean valid = true;
                outer:
                for (int u = 0; u < n; u++) {
                    int cu = (mask >> u) & 1;
                    for (int v : graph[u]) {
                        int cv = (mask >> v) & 1;
                        if (cu == cv) { valid = false; break outer; }
                    }
                }
                if (valid) return true;
            }
            return false;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — BFS 2-coloring
    // Time: O(V + E)  |  Space: O(V)
    // ============================================================
    static class Optimal {
        /**
         * Assign color 0 to the source, then BFS: each neighbor gets the
         * opposite color (1 - currentColor). If a neighbor already has the
         * same color as the current vertex, the graph is not bipartite.
         * Handle disconnected graphs by repeating for each uncolored vertex.
         */
        boolean isBipartite(int[][] graph) {
            int n = graph.length;
            int[] color = new int[n];
            Arrays.fill(color, -1);

            for (int start = 0; start < n; start++) {
                if (color[start] != -1) continue;
                Queue<Integer> q = new LinkedList<>();
                q.offer(start);
                color[start] = 0;
                while (!q.isEmpty()) {
                    int u = q.poll();
                    for (int v : graph[u]) {
                        if (color[v] == -1) {
                            color[v] = 1 - color[u];
                            q.offer(v);
                        } else if (color[v] == color[u]) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — DFS 2-coloring (recursive)
    // Time: O(V + E)  |  Space: O(V) stack
    // ============================================================
    static class Best {
        /**
         * Same 2-coloring logic as Approach 2, implemented with DFS.
         * Assign color c to vertex u; recurse with 1-c for each uncolored
         * neighbor. If a neighbor already has color c, return false.
         * DFS is often more concise; BFS is safer for very deep graphs.
         */
        int[] color;
        int[][] graph;

        boolean dfs(int u, int c) {
            color[u] = c;
            for (int v : graph[u]) {
                if (color[v] == -1) {
                    if (!dfs(v, 1 - c)) return false;
                } else if (color[v] == c) {
                    return false;
                }
            }
            return true;
        }

        boolean isBipartite(int[][] g) {
            graph = g;
            int n = g.length;
            color = new int[n];
            Arrays.fill(color, -1);
            for (int start = 0; start < n; start++) {
                if (color[start] == -1 && !dfs(start, 0)) return false;
            }
            return true;
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Is Graph Bipartite? ===\n");

        // 4-cycle (even): bipartite
        int[][] g1 = {{1,3},{0,2},{1,3},{0,2}};
        // Expected: true
        System.out.println("g1 (4-cycle) Brute:   " + new BruteForce().isBipartite(g1));
        System.out.println("g1 (4-cycle) Optimal: " + new Optimal().isBipartite(g1));
        System.out.println("g1 (4-cycle) Best:    " + new Best().isBipartite(g1));

        // Triangle (odd cycle): not bipartite
        int[][] g2 = {{1,2},{0,2},{0,1}};
        // Expected: false
        System.out.println("\ng2 (triangle) Brute:   " + new BruteForce().isBipartite(g2));
        System.out.println("g2 (triangle) Optimal: " + new Optimal().isBipartite(g2));
        System.out.println("g2 (triangle) Best:    " + new Best().isBipartite(g2));
    }
}
