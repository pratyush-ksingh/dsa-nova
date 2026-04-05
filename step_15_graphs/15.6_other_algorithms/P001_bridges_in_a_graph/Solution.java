/**
 * Problem: Bridges in a Graph (Critical Connections)
 * Difficulty: HARD | XP: 50
 * LeetCode 1192
 *
 * A bridge (critical connection) in an undirected graph is an edge whose
 * removal increases the number of connected components.
 * Find all such bridges.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE — Remove each edge, check connectivity
    // Time: O(E * (V + E))  |  Space: O(V + E)
    // For each edge: remove it, BFS/DFS to check if graph is still
    // connected. If not, it's a bridge. Then restore the edge.
    // ============================================================
    static class BruteForce {
        public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
            // Build adjacency list with Set for easy removal
            List<Set<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new HashSet<>());
            for (List<Integer> conn : connections) {
                adj.get(conn.get(0)).add(conn.get(1));
                adj.get(conn.get(1)).add(conn.get(0));
            }

            List<List<Integer>> bridges = new ArrayList<>();
            for (List<Integer> conn : connections) {
                int u = conn.get(0), v = conn.get(1);
                // Temporarily remove edge
                adj.get(u).remove(v);
                adj.get(v).remove(u);

                if (!isConnected(adj, n)) {
                    bridges.add(Arrays.asList(u, v));
                }

                // Restore edge
                adj.get(u).add(v);
                adj.get(v).add(u);
            }
            return bridges;
        }

        private boolean isConnected(List<Set<Integer>> adj, int n) {
            boolean[] visited = new boolean[n];
            Deque<Integer> stack = new ArrayDeque<>();
            stack.push(0);
            visited[0] = true;
            int count = 1;
            while (!stack.isEmpty()) {
                int node = stack.pop();
                for (int neighbor : adj.get(node)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        count++;
                        stack.push(neighbor);
                    }
                }
            }
            return count == n;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Tarjan's Bridge Finding Algorithm
    // Time: O(V + E)  |  Space: O(V)
    //
    // disc[u] = discovery time of node u.
    // low[u]  = min discovery time reachable from subtree of u via back edges.
    //
    // Edge (u -> v) is a bridge if low[v] > disc[u].
    // This means v (and its subtree) cannot reach u without going through (u,v).
    // ============================================================
    static class Optimal {
        private int[] disc, low;
        private int timer;
        private List<List<Integer>> adj;
        private List<List<Integer>> bridges;

        public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
            disc = new int[n];
            low  = new int[n];
            Arrays.fill(disc, -1);

            adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
            for (List<Integer> conn : connections) {
                adj.get(conn.get(0)).add(conn.get(1));
                adj.get(conn.get(1)).add(conn.get(0));
            }

            bridges = new ArrayList<>();
            timer = 0;

            for (int v = 0; v < n; v++) {
                if (disc[v] == -1) dfs(v, -1);
            }
            return bridges;
        }

        private void dfs(int node, int parent) {
            disc[node] = low[node] = timer++;

            for (int neighbor : adj.get(node)) {
                if (disc[neighbor] == -1) {
                    // Tree edge
                    dfs(neighbor, node);
                    low[node] = Math.min(low[node], low[neighbor]);
                    // Bridge condition
                    if (low[neighbor] > disc[node]) {
                        bridges.add(Arrays.asList(node, neighbor));
                    }
                } else if (neighbor != parent) {
                    // Back edge (not parent edge)
                    low[node] = Math.min(low[node], disc[neighbor]);
                }
            }
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Tarjan's (iterative, avoids stack overflow)
    // Time: O(V + E)  |  Space: O(V + E)
    // Same algorithm as Optimal but implemented iteratively.
    // Uses an explicit stack of (node, parent, neighbor index).
    // Critical for large graphs where recursion depth could overflow.
    // ============================================================
    static class Best {
        public List<List<Integer>> criticalConnections(int n, List<List<Integer>> connections) {
            List<List<Integer>> adj = new ArrayList<>();
            for (int i = 0; i < n; i++) adj.add(new ArrayList<>());
            for (List<Integer> conn : connections) {
                adj.get(conn.get(0)).add(conn.get(1));
                adj.get(conn.get(1)).add(conn.get(0));
            }

            int[] disc   = new int[n];
            int[] low    = new int[n];
            int[] parent = new int[n];
            Arrays.fill(disc, -1);
            Arrays.fill(parent, -1);

            List<List<Integer>> bridges = new ArrayList<>();
            int[] timer = {0};

            for (int start = 0; start < n; start++) {
                if (disc[start] != -1) continue;

                // Stack: [node, index into adj[node]]
                Deque<int[]> stack = new ArrayDeque<>();
                stack.push(new int[]{start, 0});
                disc[start] = low[start] = timer[0]++;

                while (!stack.isEmpty()) {
                    int[] top = stack.peek();
                    int node = top[0];
                    int idx  = top[1];

                    if (idx < adj.get(node).size()) {
                        top[1]++;  // advance neighbor index
                        int neighbor = adj.get(node).get(idx);

                        if (disc[neighbor] == -1) {
                            // Tree edge: push neighbor
                            parent[neighbor] = node;
                            disc[neighbor] = low[neighbor] = timer[0]++;
                            stack.push(new int[]{neighbor, 0});
                        } else if (neighbor != parent[node]) {
                            // Back edge: update low
                            low[node] = Math.min(low[node], disc[neighbor]);
                        }
                    } else {
                        // Done with this node: pop and update parent's low
                        stack.pop();
                        if (!stack.isEmpty()) {
                            int par = stack.peek()[0];
                            low[par] = Math.min(low[par], low[node]);
                            if (low[node] > disc[par]) {
                                bridges.add(Arrays.asList(par, node));
                            }
                        }
                    }
                }
            }
            return bridges;
        }
    }

    // ----------------------------------------------------------------
    // Test driver
    // ----------------------------------------------------------------
    public static void main(String[] args) {
        System.out.println("=== Bridges in a Graph ===");

        // Triangle + pendant edge: bridge = [1, 3]
        List<List<Integer>> conns1 = Arrays.asList(
            Arrays.asList(0, 1), Arrays.asList(1, 2),
            Arrays.asList(2, 0), Arrays.asList(1, 3)
        );
        System.out.println("Triangle + pendant (bridge=1-3):");
        System.out.println("  Brute:   " + new BruteForce().criticalConnections(4, conns1));
        System.out.println("  Optimal: " + new Optimal().criticalConnections(4, conns1));
        System.out.println("  Best:    " + new Best().criticalConnections(4, conns1));

        // Path graph: all edges are bridges
        List<List<Integer>> conns2 = Arrays.asList(
            Arrays.asList(0, 1), Arrays.asList(1, 2), Arrays.asList(2, 3)
        );
        System.out.println("Path graph (all bridges):");
        System.out.println("  Brute:   " + new BruteForce().criticalConnections(4, conns2));
        System.out.println("  Optimal: " + new Optimal().criticalConnections(4, conns2));
        System.out.println("  Best:    " + new Best().criticalConnections(4, conns2));

        // Complete graph K4: no bridges
        List<List<Integer>> conns3 = Arrays.asList(
            Arrays.asList(0,1), Arrays.asList(0,2), Arrays.asList(0,3),
            Arrays.asList(1,2), Arrays.asList(1,3), Arrays.asList(2,3)
        );
        System.out.println("Complete K4 (no bridges):");
        System.out.println("  Brute:   " + new BruteForce().criticalConnections(4, conns3));
        System.out.println("  Optimal: " + new Optimal().criticalConnections(4, conns3));
        System.out.println("  Best:    " + new Best().criticalConnections(4, conns3));
    }
}
