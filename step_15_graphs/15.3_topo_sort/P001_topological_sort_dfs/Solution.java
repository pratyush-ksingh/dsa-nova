/**
 * Problem: Topological Sort (DFS)
 * Difficulty: MEDIUM | XP: 25
 *
 * Implement topological sort using DFS. Detect cycles.
 * Return valid ordering for a DAG.
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: DFS WITH POST-ORDER STACK
    // Time: O(V + E)  |  Space: O(V)
    //
    // 3-state coloring: 0 = unvisited, 1 = in-progress, 2 = done
    // Push node to stack AFTER all descendants are processed.
    // Pop stack for topological order.
    // ============================================================
    public static int[] topologicalSortDFS(int V, List<List<Integer>> adj) {
        int[] state = new int[V]; // 0=unvisited, 1=in-progress, 2=done
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < V; i++) {
            if (state[i] == 0) {
                if (!dfs(i, adj, state, stack)) {
                    return new int[]{}; // cycle detected
                }
            }
        }

        // Pop from stack to get topological order
        int[] result = new int[V];
        for (int i = 0; i < V; i++) {
            result[i] = stack.pop();
        }
        return result;
    }

    private static boolean dfs(int node, List<List<Integer>> adj,
                                int[] state, Deque<Integer> stack) {
        state[node] = 1; // in-progress

        for (int neighbor : adj.get(node)) {
            if (state[neighbor] == 1) {
                // Back-edge to an in-progress node = CYCLE
                return false;
            }
            if (state[neighbor] == 0) {
                if (!dfs(neighbor, adj, state, stack)) {
                    return false;
                }
            }
            // state[neighbor] == 2: already processed, skip
        }

        state[node] = 2; // done
        stack.push(node); // post-order: push AFTER all descendants
        return true;
    }

    // ============================================================
    // APPROACH 2: KAHN'S ALGORITHM (BFS / IN-DEGREE)
    // Time: O(V + E)  |  Space: O(V)
    //
    // Repeatedly remove nodes with in-degree 0.
    // If result size < V, cycle exists.
    // ============================================================
    public static int[] topologicalSortKahn(int V, List<List<Integer>> adj) {
        int[] inDegree = new int[V];

        // Compute in-degrees
        for (int u = 0; u < V; u++) {
            for (int v : adj.get(u)) {
                inDegree[v]++;
            }
        }

        // Enqueue all nodes with in-degree 0
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        int[] result = new int[V];
        int idx = 0;

        while (!queue.isEmpty()) {
            int node = queue.poll();
            result[idx++] = node;

            for (int neighbor : adj.get(node)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }

        if (idx < V) {
            return new int[]{}; // cycle detected
        }
        return result;
    }

    // ============================================================
    // HELPER: Build directed adjacency list
    // ============================================================
    private static List<List<Integer>> buildGraph(int V, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            adj.get(edge[0]).add(edge[1]); // directed: u -> v
        }
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== Topological Sort (DFS) ===\n");

        // Test 1: DAG
        //   5 --> 0 <-- 4
        //   |           |
        //   v           v
        //   2 --> 3 --> 1
        int[][] edges1 = {{5,0}, {5,2}, {4,0}, {4,1}, {2,3}, {3,1}};
        List<List<Integer>> adj1 = buildGraph(6, edges1);
        System.out.println("Test 1 (DAG with 6 nodes):");
        System.out.println("  DFS:   " + Arrays.toString(topologicalSortDFS(6, adj1)));
        adj1 = buildGraph(6, edges1);
        System.out.println("  Kahn:  " + Arrays.toString(topologicalSortKahn(6, adj1)));

        // Test 2: Linear chain 0->1->2->3
        int[][] edges2 = {{0,1}, {1,2}, {2,3}};
        List<List<Integer>> adj2 = buildGraph(4, edges2);
        System.out.println("\nTest 2 (linear chain):");
        System.out.println("  DFS:   " + Arrays.toString(topologicalSortDFS(4, adj2)));

        // Test 3: Graph with cycle 0->1->2->0
        int[][] edges3 = {{0,1}, {1,2}, {2,0}};
        List<List<Integer>> adj3 = buildGraph(3, edges3);
        System.out.println("\nTest 3 (cycle):");
        int[] res3 = topologicalSortDFS(3, adj3);
        System.out.println("  DFS:   " + (res3.length == 0 ? "CYCLE DETECTED" : Arrays.toString(res3)));
        adj3 = buildGraph(3, edges3);
        int[] res3k = topologicalSortKahn(3, adj3);
        System.out.println("  Kahn:  " + (res3k.length == 0 ? "CYCLE DETECTED" : Arrays.toString(res3k)));

        // Test 4: Single node
        List<List<Integer>> adj4 = buildGraph(1, new int[][]{});
        System.out.println("\nTest 4 (single node):");
        System.out.println("  DFS:   " + Arrays.toString(topologicalSortDFS(1, adj4)));

        // Test 5: Disconnected DAG
        int[][] edges5 = {{0,1}, {2,3}};
        List<List<Integer>> adj5 = buildGraph(4, edges5);
        System.out.println("\nTest 5 (disconnected DAG):");
        System.out.println("  DFS:   " + Arrays.toString(topologicalSortDFS(4, adj5)));
    }
}
