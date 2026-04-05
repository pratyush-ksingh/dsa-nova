/**
 * Problem: Shortest Path in DAG
 * Difficulty: MEDIUM | XP: 25
 *
 * Find shortest paths from a source vertex in a weighted DAG
 * using topological sort + edge relaxation. O(V + E) time.
 * Handles negative edge weights (no cycles in a DAG).
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    static final int INF = Integer.MAX_VALUE;

    // ============================================================
    // APPROACH 1: BFS/DFS EXPLORE ALL PATHS (BRUTE FORCE)
    // Time: O(2^V) worst case  |  Space: O(V)
    // ============================================================
    public static int[] shortestPathBrute(int V, List<List<int[]>> adj, int src) {
        int[] dist = new int[V];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        // DFS from src, exploring all paths and updating min distances
        dfsAllPaths(src, 0, adj, dist);

        // Replace INF with -1 for unreachable
        for (int i = 0; i < V; i++) {
            if (dist[i] == INF) dist[i] = -1;
        }
        return dist;
    }

    private static void dfsAllPaths(int u, int currDist, List<List<int[]>> adj, int[] dist) {
        for (int[] edge : adj.get(u)) {
            int v = edge[0], w = edge[1];
            int newDist = currDist + w;
            if (newDist < dist[v]) {
                dist[v] = newDist;
                dfsAllPaths(v, newDist, adj, dist);
            }
        }
    }

    // ============================================================
    // APPROACH 2: TOPOLOGICAL SORT (DFS) + RELAXATION (OPTIMAL)
    // Time: O(V + E)  |  Space: O(V + E)
    // ============================================================
    public static int[] shortestPathDAG(int V, List<List<int[]>> adj, int src) {
        // Step 1: Topological sort using DFS
        boolean[] visited = new boolean[V];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                topoSortDFS(i, adj, visited, stack);
            }
        }

        // Step 2: Initialize distances
        int[] dist = new int[V];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        // Step 3: Process nodes in topological order, relax edges
        while (!stack.isEmpty()) {
            int u = stack.pop();

            if (dist[u] != INF) { // Only relax from reachable nodes
                for (int[] edge : adj.get(u)) {
                    int v = edge[0], w = edge[1];
                    if (dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                    }
                }
            }
        }

        // Replace INF with -1 for unreachable nodes
        for (int i = 0; i < V; i++) {
            if (dist[i] == INF) dist[i] = -1;
        }
        return dist;
    }

    private static void topoSortDFS(int u, List<List<int[]>> adj,
                                      boolean[] visited, Deque<Integer> stack) {
        visited[u] = true;
        for (int[] edge : adj.get(u)) {
            if (!visited[edge[0]]) {
                topoSortDFS(edge[0], adj, visited, stack);
            }
        }
        stack.push(u); // Post-order: push after all descendants
    }

    // ============================================================
    // APPROACH 3: KAHN'S BFS TOPO SORT + RELAXATION (BEST VARIANT)
    // Time: O(V + E)  |  Space: O(V + E)
    // No recursion; natural cycle detection built in
    // ============================================================
    public static int[] shortestPathDAGKahns(int V, List<List<int[]>> adj, int src) {
        // Step 1: Compute in-degrees
        int[] inDegree = new int[V];
        for (int u = 0; u < V; u++) {
            for (int[] edge : adj.get(u)) {
                inDegree[edge[0]]++;
            }
        }

        // Step 2: Kahn's BFS for topo order
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            topoOrder.add(u);
            for (int[] edge : adj.get(u)) {
                inDegree[edge[0]]--;
                if (inDegree[edge[0]] == 0) {
                    queue.offer(edge[0]);
                }
            }
        }

        // Cycle detection: if topo order doesn't include all nodes
        if (topoOrder.size() != V) {
            System.out.println("  WARNING: Graph has a cycle -- not a valid DAG!");
            return new int[0];
        }

        // Step 3: Relax edges in topo order
        int[] dist = new int[V];
        Arrays.fill(dist, INF);
        dist[src] = 0;

        for (int u : topoOrder) {
            if (dist[u] != INF) {
                for (int[] edge : adj.get(u)) {
                    int v = edge[0], w = edge[1];
                    if (dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                    }
                }
            }
        }

        for (int i = 0; i < V; i++) {
            if (dist[i] == INF) dist[i] = -1;
        }
        return dist;
    }

    // Helper: build weighted adjacency list
    private static List<List<int[]>> buildAdj(int V, int[][] edges) {
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(new int[]{e[1], e[2]}); // e = [from, to, weight]
        }
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== Shortest Path in DAG ===\n");

        // Test 1: Standard DAG
        //     2       3       6
        // 0 -----> 1 -----> 2 -----> 3
        // |                 ^        ^
        // | 1               | 2      | 1
        // +------> 4 ------+   5 --+
        //          |            ^
        //          | 4          |
        //          +------------+
        int[][] edges1 = {{0,1,2},{0,4,1},{1,2,3},{2,3,6},{4,2,2},{4,5,4},{5,3,1}};
        List<List<int[]>> adj1 = buildAdj(6, edges1);
        System.out.println("Test 1 (V=6, src=0): Expected [0, 2, 3, 6, 1, 5]");
        System.out.println("  Brute:  " + Arrays.toString(shortestPathBrute(6, adj1, 0)));
        adj1 = buildAdj(6, edges1);
        System.out.println("  DFS:    " + Arrays.toString(shortestPathDAG(6, adj1, 0)));
        adj1 = buildAdj(6, edges1);
        System.out.println("  Kahns:  " + Arrays.toString(shortestPathDAGKahns(6, adj1, 0)));

        // Test 2: Unreachable nodes
        int[][] edges2 = {{0,1,1},{1,2,2}};
        List<List<int[]>> adj2 = buildAdj(4, edges2);
        System.out.println("\nTest 2 (V=4, src=0): Expected [0, 1, 3, -1]");
        System.out.println("  DFS:    " + Arrays.toString(shortestPathDAG(4, adj2, 0)));

        // Test 3: Source in the middle
        int[][] edges3 = {{0,1,5},{1,2,3},{2,3,1}};
        List<List<int[]>> adj3 = buildAdj(4, edges3);
        System.out.println("\nTest 3 (V=4, src=1): Expected [-1, 0, 3, 4]");
        System.out.println("  DFS:    " + Arrays.toString(shortestPathDAG(4, adj3, 1)));

        // Test 4: Negative weights
        int[][] edges4 = {{0,1,2},{0,2,5},{1,2,-3}};
        List<List<int[]>> adj4 = buildAdj(3, edges4);
        System.out.println("\nTest 4 (negative weights, src=0): Expected [0, 2, -1]");
        System.out.println("  DFS:    " + Arrays.toString(shortestPathDAG(3, adj4, 0)));

        // Test 5: Single node
        List<List<int[]>> adj5 = buildAdj(1, new int[][]{});
        System.out.println("\nTest 5 (single node, src=0): Expected [0]");
        System.out.println("  DFS:    " + Arrays.toString(shortestPathDAG(1, adj5, 0)));
    }
}
