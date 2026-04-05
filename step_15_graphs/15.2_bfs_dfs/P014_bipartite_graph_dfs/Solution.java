/**
 * Problem: Bipartite Graph using DFS
 * Difficulty: MEDIUM | XP: 25
 * LeetCode 785
 *
 * A graph is bipartite if nodes can be split into two independent sets A, B
 * such that every edge connects a node in A to a node in B.
 * Equivalently: the graph is 2-colorable (contains no odd-length cycles).
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Try all 2^n color assignments
    // Time: O(2^n * (V+E))  |  Space: O(n)
    // Enumerate every possible 2-coloring and validate.
    // Only works for tiny graphs (n <= ~20); purely illustrative.
    // ============================================================
    static class BruteForce {
        public boolean isBipartite(int[][] graph) {
            int n = graph.length;
            // Each bit of mask represents color (0 or 1) for node i
            for (int mask = 0; mask < (1 << n); mask++) {
                boolean valid = true;
                outer:
                for (int u = 0; u < n; u++) {
                    int colorU = (mask >> u) & 1;
                    for (int v : graph[u]) {
                        int colorV = (mask >> v) & 1;
                        if (colorU == colorV) {
                            valid = false;
                            break outer;
                        }
                    }
                }
                if (valid) return true;
            }
            return false;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — DFS 2-coloring
    // Time: O(V + E)  |  Space: O(V) — color array + call stack
    // Assign alternating colors during DFS. If a neighbor has the
    // same color as the current node, the graph is not bipartite.
    // ============================================================
    static class Optimal {
        private int[] color;

        public boolean isBipartite(int[][] graph) {
            int n = graph.length;
            color = new int[n];
            Arrays.fill(color, -1);  // -1 = unvisited

            for (int start = 0; start < n; start++) {
                if (color[start] == -1) {
                    if (!dfs(graph, start, 0)) return false;
                }
            }
            return true;
        }

        private boolean dfs(int[][] graph, int node, int c) {
            color[node] = c;
            for (int neighbor : graph[node]) {
                if (color[neighbor] == -1) {
                    if (!dfs(graph, neighbor, 1 - c)) return false;
                } else if (color[neighbor] == c) {
                    return false;   // same color -> not bipartite
                }
            }
            return true;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — BFS 2-coloring (iterative, no stack overflow)
    // Time: O(V + E)  |  Space: O(V)
    // Same logic as DFS but uses BFS queue. Safer for very deep graphs.
    // Preferred in interviews for its clarity and iterative nature.
    // ============================================================
    static class Best {
        public boolean isBipartite(int[][] graph) {
            int n = graph.length;
            int[] color = new int[n];
            Arrays.fill(color, -1);

            for (int start = 0; start < n; start++) {
                if (color[start] != -1) continue;

                Queue<Integer> queue = new LinkedList<>();
                queue.offer(start);
                color[start] = 0;

                while (!queue.isEmpty()) {
                    int node = queue.poll();
                    for (int neighbor : graph[node]) {
                        if (color[neighbor] == -1) {
                            color[neighbor] = 1 - color[node];
                            queue.offer(neighbor);
                        } else if (color[neighbor] == color[node]) {
                            return false;  // same color -> not bipartite
                        }
                    }
                }
            }
            return true;
        }
    }

    // ----------------------------------------------------------------
    // Test driver
    // ----------------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("=== Bipartite Graph DFS ===");

        // Even cycle (bipartite): 0-1-2-3-0
        int[][] g1 = {{1, 3}, {0, 2}, {1, 3}, {0, 2}};
        System.out.println("Even cycle (bipartite):    Brute="
            + new BruteForce().isBipartite(g1)
            + "  Optimal=" + new Optimal().isBipartite(g1)
            + "  Best="    + new Best().isBipartite(g1));  // all true

        // Odd cycle (not bipartite): 0-1-2-0
        int[][] g2 = {{1, 2}, {0, 2}, {0, 1}};
        System.out.println("Odd cycle (not bipartite): Brute="
            + new BruteForce().isBipartite(g2)
            + "  Optimal=" + new Optimal().isBipartite(g2)
            + "  Best="    + new Best().isBipartite(g2));  // all false

        // Disconnected bipartite
        int[][] g3 = {{1}, {0}, {3}, {2}};
        System.out.println("Disconnected bipartite:    Brute="
            + new BruteForce().isBipartite(g3)
            + "  Optimal=" + new Optimal().isBipartite(g3)
            + "  Best="    + new Best().isBipartite(g3));  // all true
    }
}
