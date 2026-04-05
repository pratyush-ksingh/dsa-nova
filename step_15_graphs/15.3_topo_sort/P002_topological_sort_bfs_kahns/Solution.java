/**
 * Problem: Topological Sort BFS (Kahn's Algorithm)
 * Difficulty: MEDIUM | XP: 25
 *
 * Given a DAG with V vertices and E edges, find a topological ordering
 * using BFS with in-degree tracking (Kahn's Algorithm).
 * Also detect cycles (if topo sort doesn't include all nodes -> cycle).
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: NAIVE SIMULATION (BRUTE FORCE)
    // Time: O(V^2 + E)  |  Space: O(V + E)
    // Repeatedly scan all nodes to find in-degree 0 node
    // ============================================================
    public static List<Integer> topoSortBrute(int V, List<List<Integer>> adj) {
        int[] inDegree = new int[V];
        for (int u = 0; u < V; u++) {
            for (int v : adj.get(u)) {
                inDegree[v]++;
            }
        }

        boolean[] processed = new boolean[V];
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            // Scan all nodes to find one with in-degree 0
            int picked = -1;
            for (int u = 0; u < V; u++) {
                if (!processed[u] && inDegree[u] == 0) {
                    picked = u;
                    break;
                }
            }

            if (picked == -1) {
                // Cycle detected: no node with in-degree 0
                return new ArrayList<>();
            }

            result.add(picked);
            processed[picked] = true;

            // Decrement in-degree of neighbors
            for (int v : adj.get(picked)) {
                inDegree[v]--;
            }
        }

        return result;
    }

    // ============================================================
    // APPROACH 2: KAHN'S ALGORITHM (OPTIMAL)
    // Time: O(V + E)  |  Space: O(V + E)
    // BFS with queue of in-degree-0 nodes
    // ============================================================
    public static List<Integer> topoSortKahns(int V, List<List<Integer>> adj) {
        // Step 1: Compute in-degrees
        int[] inDegree = new int[V];
        for (int u = 0; u < V; u++) {
            for (int v : adj.get(u)) {
                inDegree[v]++;
            }
        }

        // Step 2: Enqueue all nodes with in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        // Step 3: BFS -- process nodes, decrement neighbors
        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            result.add(u);

            for (int v : adj.get(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }

        // Step 4: Cycle detection
        if (result.size() != V) {
            return new ArrayList<>(); // Cycle exists
        }
        return result;
    }

    // ============================================================
    // APPROACH 3: KAHN'S WITH ALL VALID ORDERINGS (BACKTRACKING)
    // Time: O(V! * V) worst case  |  Space: O(V + E)
    // Enumerate every valid topological ordering
    // ============================================================
    public static List<List<Integer>> allTopoSorts(int V, List<List<Integer>> adj) {
        int[] inDegree = new int[V];
        for (int u = 0; u < V; u++) {
            for (int v : adj.get(u)) {
                inDegree[v]++;
            }
        }

        List<List<Integer>> allResults = new ArrayList<>();
        List<Integer> current = new ArrayList<>();
        boolean[] visited = new boolean[V];

        backtrack(V, adj, inDegree, visited, current, allResults);
        return allResults;
    }

    private static void backtrack(int V, List<List<Integer>> adj, int[] inDegree,
                                   boolean[] visited, List<Integer> current,
                                   List<List<Integer>> allResults) {
        if (current.size() == V) {
            allResults.add(new ArrayList<>(current));
            return;
        }

        for (int u = 0; u < V; u++) {
            if (!visited[u] && inDegree[u] == 0) {
                // Choose u
                visited[u] = true;
                current.add(u);
                for (int v : adj.get(u)) {
                    inDegree[v]--;
                }

                // Recurse
                backtrack(V, adj, inDegree, visited, current, allResults);

                // Undo (backtrack)
                visited[u] = false;
                current.remove(current.size() - 1);
                for (int v : adj.get(u)) {
                    inDegree[v]++;
                }
            }
        }
    }

    // Helper: build adjacency list from edge pairs
    private static List<List<Integer>> buildAdj(int V, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
        }
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== Topological Sort BFS (Kahn's) ===\n");

        // Test 1: Standard DAG
        int[][] edges1 = {{5,2},{5,0},{4,0},{4,1},{2,3},{3,1}};
        List<List<Integer>> adj1 = buildAdj(6, edges1);
        System.out.println("Test 1 (V=6):");
        System.out.println("  Brute:  " + topoSortBrute(6, adj1));
        adj1 = buildAdj(6, edges1); // rebuild since brute modifies in-degree
        System.out.println("  Kahns:  " + topoSortKahns(6, adj1));

        // Test 2: Linear chain
        int[][] edges2 = {{0,1},{1,2},{2,3}};
        List<List<Integer>> adj2 = buildAdj(4, edges2);
        System.out.println("\nTest 2 (linear chain V=4):");
        System.out.println("  Kahns:  " + topoSortKahns(4, adj2));

        // Test 3: Cycle detection
        int[][] edges3 = {{0,1},{1,2},{2,0}};
        List<List<Integer>> adj3 = buildAdj(3, edges3);
        System.out.println("\nTest 3 (cycle V=3):");
        List<Integer> res3 = topoSortKahns(3, adj3);
        System.out.println("  Kahns:  " + (res3.isEmpty() ? "CYCLE DETECTED" : res3));

        // Test 4: All valid orderings
        int[][] edges4 = {{0,2},{1,2},{2,3}};
        List<List<Integer>> adj4 = buildAdj(4, edges4);
        System.out.println("\nTest 4 (all orderings V=4, edges: 0->2, 1->2, 2->3):");
        System.out.println("  All:    " + allTopoSorts(4, adj4));

        // Test 5: Single node
        List<List<Integer>> adj5 = buildAdj(1, new int[][]{});
        System.out.println("\nTest 5 (single node):");
        System.out.println("  Kahns:  " + topoSortKahns(1, adj5));

        // Test 6: No edges
        List<List<Integer>> adj6 = buildAdj(3, new int[][]{});
        System.out.println("\nTest 6 (no edges V=3):");
        System.out.println("  Kahns:  " + topoSortKahns(3, adj6));
    }
}
