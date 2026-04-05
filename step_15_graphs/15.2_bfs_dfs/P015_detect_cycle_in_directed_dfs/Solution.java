/**
 * Problem: Detect Cycle in Directed Graph using DFS
 * Difficulty: MEDIUM | XP: 25
 * GFG / Striver Graph Series
 *
 * Given a directed graph with V vertices and adjacency list,
 * return true if the graph contains a cycle, false otherwise.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — DFS from every node, fresh visited set
    // Time: O(V * (V + E))  |  Space: O(V)
    // For every node, independently track the current DFS path.
    // If we revisit a node in the same path -> cycle.
    // Inefficient: recomputes from scratch for each starting node.
    // ============================================================
    static class BruteForce {
        private int V;
        private List<List<Integer>> adj;

        public boolean isCyclic(int V, List<List<Integer>> adj) {
            this.V = V;
            this.adj = adj;
            for (int v = 0; v < V; v++) {
                if (dfsFromNode(v)) return true;
            }
            return false;
        }

        private boolean dfsFromNode(int start) {
            Set<Integer> path    = new HashSet<>();
            Set<Integer> visited = new HashSet<>();

            return dfs(start, path, visited);
        }

        private boolean dfs(int node, Set<Integer> path, Set<Integer> visited) {
            if (path.contains(node))    return true;   // back edge
            if (visited.contains(node)) return false;  // fully explored

            path.add(node);
            for (int neighbor : adj.get(node)) {
                if (dfs(neighbor, path, visited)) return true;
            }
            path.remove(node);
            visited.add(node);
            return false;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — DFS with 3-color marking
    // Time: O(V + E)  |  Space: O(V)
    // vis[node]: 0=unvisited(WHITE), 1=in-stack(GRAY), 2=done(BLACK)
    // Back edge = edge to a GRAY node -> cycle exists.
    // ============================================================
    static class Optimal {
        private int[] vis;
        private List<List<Integer>> adj;

        public boolean isCyclic(int V, List<List<Integer>> adj) {
            this.adj = adj;
            vis = new int[V];   // all 0 (WHITE) initially

            for (int v = 0; v < V; v++) {
                if (vis[v] == 0) {
                    if (dfs(v)) return true;
                }
            }
            return false;
        }

        private boolean dfs(int node) {
            vis[node] = 1;  // GRAY: currently in stack
            for (int neighbor : adj.get(node)) {
                if (vis[neighbor] == 0) {
                    if (dfs(neighbor)) return true;
                } else if (vis[neighbor] == 1) {
                    return true;  // back edge -> cycle
                }
                // vis[neighbor] == 2 (BLACK): safe, skip
            }
            vis[node] = 2;  // BLACK: done
            return false;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Kahn's Algorithm (Topological Sort via BFS)
    // Time: O(V + E)  |  Space: O(V)
    // If a valid topological order processes all V nodes, no cycle.
    // If count of processed nodes < V, some are stuck in a cycle.
    // ============================================================
    static class Best {
        public boolean isCyclic(int V, List<List<Integer>> adj) {
            int[] inDegree = new int[V];
            for (int u = 0; u < V; u++) {
                for (int v : adj.get(u)) {
                    inDegree[v]++;
                }
            }

            Queue<Integer> queue = new LinkedList<>();
            for (int v = 0; v < V; v++) {
                if (inDegree[v] == 0) queue.offer(v);
            }

            int count = 0;
            while (!queue.isEmpty()) {
                int node = queue.poll();
                count++;
                for (int neighbor : adj.get(node)) {
                    if (--inDegree[neighbor] == 0) {
                        queue.offer(neighbor);
                    }
                }
            }

            return count != V;  // fewer nodes processed -> cycle exists
        }
    }

    // ----------------------------------------------------------------
    // Test helpers
    // ----------------------------------------------------------------
    static List<List<Integer>> buildAdj(int V, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) adj.get(e[0]).add(e[1]);
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== Detect Cycle in Directed Graph ===");

        // Cycle: 0->1->2->0
        List<List<Integer>> adj1 = buildAdj(3, new int[][]{{0,1},{1,2},{2,0}});
        System.out.println("Cycle 0->1->2->0:");
        System.out.println("  Brute="   + new BruteForce().isCyclic(3, adj1));
        System.out.println("  Optimal=" + new Optimal().isCyclic(3, adj1));
        System.out.println("  Best="    + new Best().isCyclic(3, adj1));

        // DAG: 0->1->2, 0->2
        List<List<Integer>> adj2 = buildAdj(3, new int[][]{{0,1},{0,2},{1,2}});
        System.out.println("DAG (no cycle):");
        System.out.println("  Brute="   + new BruteForce().isCyclic(3, adj2));
        System.out.println("  Optimal=" + new Optimal().isCyclic(3, adj2));
        System.out.println("  Best="    + new Best().isCyclic(3, adj2));

        // Self-loop: 0->0
        List<List<Integer>> adj3 = buildAdj(1, new int[][]{{0,0}});
        System.out.println("Self-loop:");
        System.out.println("  Brute="   + new BruteForce().isCyclic(1, adj3));
        System.out.println("  Optimal=" + new Optimal().isCyclic(1, adj3));
        System.out.println("  Best="    + new Best().isCyclic(1, adj3));
    }
}
