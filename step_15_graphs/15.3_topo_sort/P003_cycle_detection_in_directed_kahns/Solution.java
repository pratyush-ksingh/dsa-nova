/**
 * Problem: Cycle Detection in Directed Graph (Kahn's Algorithm)
 * Difficulty: MEDIUM | XP: 25
 *
 * Detect cycle in a directed graph using Kahn's algorithm.
 * If topological sort doesn't include all nodes, a cycle exists.
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: DFS 3-COLOR MARKING
    // Time: O(V + E)  |  Space: O(V)
    // WHITE=0, GRAY=1, BLACK=2
    // ============================================================
    static final int WHITE = 0, GRAY = 1, BLACK = 2;

    public static boolean hasCycleDFS(int V, List<List<Integer>> adj) {
        int[] color = new int[V]; // All WHITE initially

        for (int i = 0; i < V; i++) {
            if (color[i] == WHITE) {
                if (dfs(i, adj, color)) return true;
            }
        }
        return false;
    }

    private static boolean dfs(int u, List<List<Integer>> adj, int[] color) {
        color[u] = GRAY; // Entering DFS for this node

        for (int v : adj.get(u)) {
            if (color[v] == GRAY) {
                return true; // Back edge found -> cycle!
            }
            if (color[v] == WHITE) {
                if (dfs(v, adj, color)) return true;
            }
            // BLACK nodes are already fully processed, skip
        }

        color[u] = BLACK; // Fully processed
        return false;
    }

    // ============================================================
    // APPROACH 2: KAHN'S ALGORITHM (OPTIMAL)
    // Time: O(V + E)  |  Space: O(V + E)
    // If topo sort processes fewer than V nodes -> cycle
    // ============================================================
    public static boolean hasCycleKahns(int V, List<List<Integer>> adj) {
        // Step 1: Compute in-degrees
        int[] inDegree = new int[V];
        for (int u = 0; u < V; u++) {
            for (int v : adj.get(u)) {
                inDegree[v]++;
            }
        }

        // Step 2: Enqueue all in-degree-0 nodes
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        // Step 3: BFS processing
        int count = 0;
        while (!queue.isEmpty()) {
            int u = queue.poll();
            count++;

            for (int v : adj.get(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }

        // Step 4: Cycle detection
        return count < V; // true if cycle exists
    }

    // ============================================================
    // APPROACH 3: KAHN'S WITH CYCLE NODE IDENTIFICATION (BEST)
    // Time: O(V + E)  |  Space: O(V + E)
    // Returns list of nodes stuck in/downstream of cycles
    // ============================================================
    public static List<Integer> findCyclicNodes(int V, List<List<Integer>> adj) {
        int[] inDegree = new int[V];
        for (int u = 0; u < V; u++) {
            for (int v : adj.get(u)) {
                inDegree[v]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < V; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }

        Set<Integer> processed = new HashSet<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            processed.add(u);
            for (int v : adj.get(u)) {
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }

        // Nodes NOT processed are stuck (in or downstream of cycle)
        List<Integer> cyclicNodes = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            if (!processed.contains(i)) {
                cyclicNodes.add(i);
            }
        }
        return cyclicNodes;
    }

    // Helper: build adjacency list
    private static List<List<Integer>> buildAdj(int V, int[][] edges) {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) adj.add(new ArrayList<>());
        for (int[] e : edges) {
            adj.get(e[0]).add(e[1]);
        }
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== Cycle Detection in Directed Graph (Kahn's) ===\n");

        // Test 1: No cycle (linear chain)
        int[][] edges1 = {{0,1},{1,2},{2,3}};
        List<List<Integer>> adj1 = buildAdj(4, edges1);
        System.out.println("Test 1 (linear chain V=4): Expected false");
        System.out.println("  DFS:    " + hasCycleDFS(4, adj1));
        System.out.println("  Kahns:  " + hasCycleKahns(4, buildAdj(4, edges1)));

        // Test 2: Simple cycle
        int[][] edges2 = {{0,1},{1,2},{2,0}};
        List<List<Integer>> adj2 = buildAdj(3, edges2);
        System.out.println("\nTest 2 (3-node cycle): Expected true");
        System.out.println("  DFS:    " + hasCycleDFS(3, adj2));
        System.out.println("  Kahns:  " + hasCycleKahns(3, buildAdj(3, edges2)));
        System.out.println("  Cyclic: " + findCyclicNodes(3, buildAdj(3, edges2)));

        // Test 3: Partial cycle with acyclic component
        int[][] edges3 = {{0,1},{1,2},{2,0},{3,4}};
        List<List<Integer>> adj3 = buildAdj(5, edges3);
        System.out.println("\nTest 3 (partial cycle V=5): Expected true");
        System.out.println("  Kahns:  " + hasCycleKahns(5, adj3));
        System.out.println("  Cyclic: " + findCyclicNodes(5, buildAdj(5, edges3)));

        // Test 4: Self-loop
        int[][] edges4 = {{0,0}};
        List<List<Integer>> adj4 = buildAdj(1, edges4);
        System.out.println("\nTest 4 (self-loop): Expected true");
        System.out.println("  Kahns:  " + hasCycleKahns(1, adj4));

        // Test 5: No edges
        List<List<Integer>> adj5 = buildAdj(3, new int[][]{});
        System.out.println("\nTest 5 (no edges V=3): Expected false");
        System.out.println("  Kahns:  " + hasCycleKahns(3, adj5));

        // Test 6: DAG with diamond shape
        int[][] edges6 = {{0,1},{0,2},{1,3},{2,3}};
        List<List<Integer>> adj6 = buildAdj(4, edges6);
        System.out.println("\nTest 6 (diamond DAG V=4): Expected false");
        System.out.println("  Kahns:  " + hasCycleKahns(4, adj6));

        // Test 7: Two-node cycle
        int[][] edges7 = {{0,1},{1,0}};
        List<List<Integer>> adj7 = buildAdj(2, edges7);
        System.out.println("\nTest 7 (two-node cycle): Expected true");
        System.out.println("  Kahns:  " + hasCycleKahns(2, adj7));
        System.out.println("  Cyclic: " + findCyclicNodes(2, buildAdj(2, edges7)));

        // Test 8: Cycle with downstream nodes
        int[][] edges8 = {{0,1},{1,2},{2,0},{2,3},{3,4}};
        List<List<Integer>> adj8 = buildAdj(5, edges8);
        System.out.println("\nTest 8 (cycle + downstream V=5): Expected true");
        System.out.println("  Kahns:  " + hasCycleKahns(5, adj8));
        System.out.println("  Cyclic: " + findCyclicNodes(5, buildAdj(5, edges8)));
        System.out.println("  (Nodes 0,1,2 in cycle; 3,4 downstream of cycle)");
    }
}
