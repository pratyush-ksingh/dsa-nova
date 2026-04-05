/**
 * Problem: Bellman-Ford Algorithm
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a directed weighted graph (V vertices, E edges, possibly negative weights),
 * find the shortest path from a source vertex to all other vertices.
 * Detect and report negative-weight cycles.
 *
 * @author DSA_Nova
 */
import java.util.*;

public class Solution {

    static final int INF = Integer.MAX_VALUE / 2;

    // Helper: edge record
    static int[] edge(int u, int v, int w) { return new int[]{u, v, w}; }

    // ============================================================
    // APPROACH 1: BRUTE FORCE — DFS over all simple paths
    // Time: O(V! * E) worst case  |  Space: O(V)
    // ============================================================
    static class BruteForce {
        /**
         * Build adjacency list, then DFS from the source. At each step, prune
         * if the current cost does not improve the known best distance. Works
         * correctly on graphs without negative cycles; no cycle detection.
         */
        int V;
        int[] dist;
        Map<Integer, List<int[]>> adj = new HashMap<>();  // u -> [(v, w)]

        void dfs(int u, long cost, boolean[] visited) {
            if (cost >= dist[u]) return;
            dist[u] = (int) cost;
            for (int[] edge : adj.getOrDefault(u, Collections.emptyList())) {
                int v = edge[0], w = edge[1];
                if (!visited[v]) {
                    visited[v] = true;
                    dfs(v, cost + w, visited);
                    visited[v] = false;
                }
            }
        }

        int[] shortestPath(int v, int[][] edges, int src) {
            V = v;
            dist = new int[V];
            Arrays.fill(dist, INF);
            dist[src] = 0;
            for (int[] e : edges)
                adj.computeIfAbsent(e[0], k -> new ArrayList<>()).add(new int[]{e[1], e[2]});
            boolean[] visited = new boolean[V];
            visited[src] = true;
            dfs(src, 0, visited);
            return dist;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL — Classic Bellman-Ford: V-1 relaxations
    // Time: O(V * E)  |  Space: O(V)
    // ============================================================
    static class Optimal {
        /**
         * Any shortest path without negative cycles uses at most V-1 edges.
         * Relax ALL edges V-1 times. After that, one more pass detects negative
         * cycles: if any distance still decreases, a negative cycle is present.
         *
         * Key insight: after i relaxation passes, all shortest paths using
         * at most i edges are correctly computed.
         */
        int[] shortestPath(int V, int[][] edges, int src) {
            int[] dist = new int[V];
            Arrays.fill(dist, INF);
            dist[src] = 0;

            // V-1 relaxation passes
            for (int i = 0; i < V - 1; i++) {
                boolean updated = false;
                for (int[] e : edges) {
                    int u = e[0], v = e[1], w = e[2];
                    if (dist[u] != INF && dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                        updated = true;
                    }
                }
                if (!updated) break; // early convergence
            }

            // Negative cycle detection
            for (int[] e : edges) {
                int u = e[0], v = e[1], w = e[2];
                if (dist[u] != INF && dist[u] + w < dist[v]) {
                    dist[v] = -INF; // affected by negative cycle
                }
            }

            return dist;
        }
    }

    // ============================================================
    // APPROACH 3: BEST — Bellman-Ford + full negative-cycle propagation
    // Time: O(V * E)  |  Space: O(V + E)
    // ============================================================
    static class Best {
        /**
         * Adds to Approach 2: after identifying vertices directly on a negative
         * cycle, BFS to propagate -INF to ALL vertices reachable from those
         * nodes. This correctly marks every vertex whose shortest path goes
         * through any negative cycle.
         */
        int[] shortestPath(int V, int[][] edges, int src) {
            int[] dist = new int[V];
            Arrays.fill(dist, INF);
            dist[src] = 0;

            // V-1 passes with early termination
            for (int i = 0; i < V - 1; i++) {
                boolean updated = false;
                for (int[] e : edges) {
                    int u = e[0], v = e[1], w = e[2];
                    if (dist[u] != INF && dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                        updated = true;
                    }
                }
                if (!updated) break;
            }

            // Collect nodes directly affected by negative cycles
            Set<Integer> negCycle = new HashSet<>();
            for (int[] e : edges) {
                int u = e[0], v = e[1], w = e[2];
                if (dist[u] != INF && dist[u] + w < dist[v])
                    negCycle.add(v);
            }

            // BFS: propagate -INF to all reachable nodes
            if (!negCycle.isEmpty()) {
                Map<Integer, List<Integer>> adj = new HashMap<>();
                for (int[] e : edges)
                    adj.computeIfAbsent(e[0], k -> new ArrayList<>()).add(e[1]);

                Queue<Integer> q = new LinkedList<>(negCycle);
                while (!q.isEmpty()) {
                    int node = q.poll();
                    for (int nb : adj.getOrDefault(node, Collections.emptyList())) {
                        if (!negCycle.contains(nb)) {
                            negCycle.add(nb);
                            q.offer(nb);
                        }
                    }
                }
                for (int node : negCycle) dist[node] = -INF;
            }

            return dist;
        }
    }

    // ============================================================
    // MAIN
    // ============================================================
    public static void main(String[] args) {
        System.out.println("=== Bellman-Ford Algorithm ===\n");

        // 5 vertices, no negative cycle
        int V1 = 5;
        int[][] E1 = {{0,1,4},{0,2,5},{1,3,-3},{2,1,-4},{3,4,2}};
        int src1 = 0;
        // Expected: [0, 1, 5, -2, 0]
        System.out.println("Brute:          " + Arrays.toString(new BruteForce().shortestPath(V1, E1, src1)));
        System.out.println("Optimal:        " + Arrays.toString(new Optimal().shortestPath(V1, E1, src1)));
        System.out.println("Best:           " + Arrays.toString(new Best().shortestPath(V1, E1, src1)));

        // Negative cycle: 1->2->1, weight = -1
        System.out.println("\n--- Negative Cycle ---");
        int V2 = 3;
        int[][] E2 = {{0,1,1},{1,2,-2},{2,1,1}};
        int src2 = 0;
        System.out.println("Optimal:        " + Arrays.toString(new Optimal().shortestPath(V2, E2, src2)));
        System.out.println("Best (full):    " + Arrays.toString(new Best().shortestPath(V2, E2, src2)));
    }
}
