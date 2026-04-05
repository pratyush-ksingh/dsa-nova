/**
 * Problem: DFS Traversal
 * Difficulty: EASY | XP: 10
 *
 * Implement DFS on a graph. Return nodes in DFS order.
 * Handle disconnected graphs.
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: RECURSIVE DFS
    // Time: O(V + E)  |  Space: O(V)
    // ============================================================
    public static List<Integer> dfsRecursive(int V, List<List<Integer>> adj) {
        boolean[] visited = new boolean[V];
        List<Integer> result = new ArrayList<>();

        // Outer loop handles disconnected components
        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                dfsHelper(i, adj, visited, result);
            }
        }
        return result;
    }

    private static void dfsHelper(int node, List<List<Integer>> adj,
                                   boolean[] visited, List<Integer> result) {
        visited[node] = true;
        result.add(node);

        for (int neighbor : adj.get(node)) {
            if (!visited[neighbor]) {
                dfsHelper(neighbor, adj, visited, result);
            }
        }
    }

    // ============================================================
    // APPROACH 2: ITERATIVE DFS (Explicit Stack)
    // Time: O(V + E)  |  Space: O(V)
    // ============================================================
    public static List<Integer> dfsIterative(int V, List<List<Integer>> adj) {
        boolean[] visited = new boolean[V];
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                Deque<Integer> stack = new ArrayDeque<>();
                stack.push(i);

                while (!stack.isEmpty()) {
                    int node = stack.pop();
                    if (visited[node]) continue;

                    visited[node] = true;
                    result.add(node);

                    // Push neighbors in reverse order so that the smallest
                    // neighbor is processed first (matching recursive behavior)
                    List<Integer> neighbors = adj.get(node);
                    for (int j = neighbors.size() - 1; j >= 0; j--) {
                        int neighbor = neighbors.get(j);
                        if (!visited[neighbor]) {
                            stack.push(neighbor);
                        }
                    }
                }
            }
        }
        return result;
    }

    // ============================================================
    // HELPER: Build adjacency list from edge pairs
    // ============================================================
    private static List<List<Integer>> buildGraph(int V, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]);
            adj.get(edge[1]).add(edge[0]); // undirected
        }
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== DFS Traversal ===\n");

        // Test 1: Connected graph
        //   0 --- 1
        //   |     |
        //   2     4
        int[][] edges1 = {{0,1}, {0,2}, {1,4}};
        List<List<Integer>> adj1 = buildGraph(5, edges1);
        System.out.println("Test 1 (connected):");
        System.out.println("  Recursive:  " + dfsRecursive(5, adj1));
        adj1 = buildGraph(5, edges1); // rebuild for fresh test
        System.out.println("  Iterative:  " + dfsIterative(5, adj1));

        // Test 2: Disconnected graph
        //   0 --- 1       3
        //   |     |       |
        //   2     4       5
        int[][] edges2 = {{0,1}, {0,2}, {1,4}, {3,5}};
        List<List<Integer>> adj2 = buildGraph(6, edges2);
        System.out.println("\nTest 2 (disconnected):");
        System.out.println("  Recursive:  " + dfsRecursive(6, adj2));
        adj2 = buildGraph(6, edges2);
        System.out.println("  Iterative:  " + dfsIterative(6, adj2));

        // Test 3: Single node
        List<List<Integer>> adj3 = buildGraph(1, new int[][]{});
        System.out.println("\nTest 3 (single node):");
        System.out.println("  Recursive:  " + dfsRecursive(1, adj3));

        // Test 4: No edges (all disconnected)
        List<List<Integer>> adj4 = buildGraph(4, new int[][]{});
        System.out.println("\nTest 4 (no edges):");
        System.out.println("  Recursive:  " + dfsRecursive(4, adj4));
    }
}
