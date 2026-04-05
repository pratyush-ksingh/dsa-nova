/**
 * Problem: Shortest Path in Undirected Unit Weight Graph
 * Difficulty: MEDIUM | XP: 25
 *
 * Find shortest distance from source to all vertices in
 * an unweighted undirected graph using BFS.
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BFS (OPTIMAL)
    // Time: O(V + E)  |  Space: O(V)
    //
    // BFS naturally explores nodes in order of increasing distance.
    // First visit = shortest path for unweighted graphs.
    // ============================================================
    public static int[] shortestPath(int V, List<List<Integer>> adj, int src) {
        int[] dist = new int[V];
        Arrays.fill(dist, -1); // -1 = unreachable

        dist[src] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(src);

        while (!queue.isEmpty()) {
            int node = queue.poll();

            for (int neighbor : adj.get(node)) {
                if (dist[neighbor] == -1) { // not visited
                    dist[neighbor] = dist[node] + 1;
                    queue.offer(neighbor);
                }
            }
        }

        return dist;
    }

    // ============================================================
    // APPROACH 2: BFS WITH PATH RECONSTRUCTION
    // Time: O(V + E)  |  Space: O(V)
    //
    // Same BFS but also tracks parent[] to reconstruct actual path.
    // ============================================================
    public static int[] shortestPathWithParent(int V, List<List<Integer>> adj,
                                                int src, int[] parent) {
        int[] dist = new int[V];
        Arrays.fill(dist, -1);
        Arrays.fill(parent, -1);

        dist[src] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(src);

        while (!queue.isEmpty()) {
            int node = queue.poll();

            for (int neighbor : adj.get(node)) {
                if (dist[neighbor] == -1) {
                    dist[neighbor] = dist[node] + 1;
                    parent[neighbor] = node;
                    queue.offer(neighbor);
                }
            }
        }

        return dist;
    }

    /**
     * Reconstruct path from src to dest using parent array.
     * Returns empty list if dest is unreachable.
     */
    public static List<Integer> reconstructPath(int[] parent, int src, int dest) {
        if (parent[dest] == -1 && dest != src) {
            return Collections.emptyList(); // unreachable
        }

        List<Integer> path = new ArrayList<>();
        for (int node = dest; node != -1; node = parent[node]) {
            path.add(node);
        }
        Collections.reverse(path);
        return path;
    }

    // ============================================================
    // HELPER: Build undirected adjacency list
    // ============================================================
    private static List<List<Integer>> buildGraph(int V, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]);
        }
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== Shortest Path in Undirected Unit Weight Graph ===\n");

        // Test 1:
        //   0 --- 1 --- 2
        //   |           |
        //   3           4
        //         |
        //         5
        int[][] edges1 = {{0,1}, {1,2}, {0,3}, {2,4}, {3,5}};
        List<List<Integer>> adj1 = buildGraph(6, edges1);

        System.out.println("Test 1 (src=0):");
        int[] dist1 = shortestPath(6, adj1, 0);
        System.out.println("  Distances: " + Arrays.toString(dist1));
        // Expected: [0, 1, 2, 1, 3, 2]

        // Test with path reconstruction
        int[] parent = new int[6];
        shortestPathWithParent(6, adj1, 0, parent);
        System.out.println("  Path 0->4: " + reconstructPath(parent, 0, 4));
        // Expected: [0, 1, 2, 4]

        // Test 2: Disconnected graph
        //   0 --- 1     3 --- 4
        //         |
        //         2
        int[][] edges2 = {{0,1}, {1,2}, {3,4}};
        List<List<Integer>> adj2 = buildGraph(5, edges2);

        System.out.println("\nTest 2 (disconnected, src=0):");
        int[] dist2 = shortestPath(5, adj2, 0);
        System.out.println("  Distances: " + Arrays.toString(dist2));
        // Expected: [0, 1, 2, -1, -1]

        // Test 3: Single node
        List<List<Integer>> adj3 = buildGraph(1, new int[][]{});
        System.out.println("\nTest 3 (single node):");
        System.out.println("  Distances: " + Arrays.toString(shortestPath(1, adj3, 0)));
        // Expected: [0]

        // Test 4: Source in the middle
        //   0 --- 1 --- 2 --- 3 --- 4
        int[][] edges4 = {{0,1}, {1,2}, {2,3}, {3,4}};
        List<List<Integer>> adj4 = buildGraph(5, edges4);
        System.out.println("\nTest 4 (chain, src=2):");
        System.out.println("  Distances: " + Arrays.toString(shortestPath(5, adj4, 2)));
        // Expected: [2, 1, 0, 1, 2]
    }
}
