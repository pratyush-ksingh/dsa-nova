/**
 * Problem: Eventual Safe States
 * Difficulty: MEDIUM | XP: 25
 * LeetCode 802
 *
 * A node in a directed graph is "safe" if every path starting from that node
 * eventually leads to a terminal node (no outgoing edges).
 * Return all safe nodes in ascending order.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — DFS from each node with memoization
    // Time: O(V * (V + E)) worst case  |  Space: O(V)
    // For each node, independently check if all paths lead to terminal.
    // Uses a per-call path set to detect cycles.
    // ============================================================
    static class BruteForce {
        private int[] memo;   // -1=unknown, 0=unsafe, 1=safe
        private int[][] graph;

        public List<Integer> eventualSafeNodes(int[][] graph) {
            int n = graph.length;
            this.graph = graph;
            memo = new int[n];
            Arrays.fill(memo, -1);

            List<Integer> result = new ArrayList<>();
            for (int v = 0; v < n; v++) {
                if (isSafe(v, new HashSet<>())) result.add(v);
            }
            return result;
        }

        private boolean isSafe(int node, Set<Integer> path) {
            if (path.contains(node)) { memo[node] = 0; return false; }
            if (memo[node] != -1)    return memo[node] == 1;

            path.add(node);
            for (int neighbor : graph[node]) {
                if (!isSafe(neighbor, path)) {
                    memo[node] = 0;
                    path.remove(node);
                    return false;
                }
            }
            path.remove(node);
            memo[node] = 1;
            return true;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — DFS 3-coloring
    // Time: O(V + E)  |  Space: O(V)
    // 0=unvisited(WHITE), 1=in-stack(GRAY), 2=safe(BLACK).
    // Nodes that end up BLACK are safe; GRAY/0 are unsafe.
    // ============================================================
    static class Optimal {
        private int[] color;
        private int[][] graph;

        public List<Integer> eventualSafeNodes(int[][] graph) {
            int n = graph.length;
            this.graph = graph;
            color = new int[n];  // all 0 (WHITE)

            List<Integer> result = new ArrayList<>();
            for (int v = 0; v < n; v++) {
                if (dfs(v)) result.add(v);
            }
            return result;
        }

        /** Returns true if node is safe (BLACK). */
        private boolean dfs(int node) {
            if (color[node] == 1) return false;  // GRAY -> cycle -> unsafe
            if (color[node] == 2) return true;   // BLACK -> already safe

            color[node] = 1;  // mark GRAY
            for (int neighbor : graph[node]) {
                if (!dfs(neighbor)) return false;
            }
            color[node] = 2;  // mark BLACK (safe)
            return true;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Reverse Graph + Kahn's Algorithm
    // Time: O(V + E)  |  Space: O(V + E)
    // Reverse all edges. Terminal nodes (out-degree 0 in original)
    // have in-degree 0 in reverse graph. Run BFS from terminals.
    // All nodes reachable = safe in original graph.
    // ============================================================
    static class Best {
        public List<Integer> eventualSafeNodes(int[][] graph) {
            int n = graph.length;
            List<List<Integer>> reverseGraph = new ArrayList<>();
            for (int i = 0; i < n; i++) reverseGraph.add(new ArrayList<>());

            int[] outDegree = new int[n];
            for (int u = 0; u < n; u++) {
                outDegree[u] = graph[u].length;
                for (int v : graph[u]) {
                    reverseGraph.get(v).add(u);  // reverse edge v -> u
                }
            }

            // Enqueue terminal nodes (out-degree 0 in original)
            Queue<Integer> queue = new LinkedList<>();
            for (int v = 0; v < n; v++) {
                if (outDegree[v] == 0) queue.offer(v);
            }

            boolean[] safe = new boolean[n];
            while (!queue.isEmpty()) {
                int node = queue.poll();
                safe[node] = true;
                for (int predecessor : reverseGraph.get(node)) {
                    outDegree[predecessor]--;
                    if (outDegree[predecessor] == 0) {
                        queue.offer(predecessor);
                    }
                }
            }

            List<Integer> result = new ArrayList<>();
            for (int v = 0; v < n; v++) {
                if (safe[v]) result.add(v);
            }
            return result;
        }
    }

    // ----------------------------------------------------------------
    // Test driver
    // ----------------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("=== Eventual Safe States ===");

        // graph = [[1,2],[2,3],[5],[0],[5],[],[]]
        // Safe nodes: 2, 4, 5, 6
        int[][] g1 = {{1, 2}, {2, 3}, {5}, {0}, {5}, {}, {}};
        System.out.println("Example 1:");
        System.out.println("  Brute:   " + new BruteForce().eventualSafeNodes(g1));
        System.out.println("  Optimal: " + new Optimal().eventualSafeNodes(g1));
        System.out.println("  Best:    " + new Best().eventualSafeNodes(g1));

        // All terminals
        int[][] g2 = {{}, {}, {}};
        System.out.println("All terminals (all safe):");
        System.out.println("  Brute:   " + new BruteForce().eventualSafeNodes(g2));
        System.out.println("  Optimal: " + new Optimal().eventualSafeNodes(g2));
        System.out.println("  Best:    " + new Best().eventualSafeNodes(g2));

        // All in cycle
        int[][] g3 = {{1}, {2}, {0}};
        System.out.println("All in cycle (none safe):");
        System.out.println("  Brute:   " + new BruteForce().eventualSafeNodes(g3));
        System.out.println("  Optimal: " + new Optimal().eventualSafeNodes(g3));
        System.out.println("  Best:    " + new Best().eventualSafeNodes(g3));
    }
}
