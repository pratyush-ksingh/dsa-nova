/**
 * Problem: Minimum Spanning Tree (Prim's Algorithm)
 * Difficulty: MEDIUM | XP: 25
 *
 * Find MST using Prim's algorithm.
 * Return total weight and MST edges.
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: NAIVE PRIM'S (No Heap)
    // Time: O(V^2)  |  Space: O(V)
    //
    // Good for dense graphs (adjacency matrix).
    // At each step, scan all vertices for the minimum key.
    // ============================================================
    public static int primsNaive(int V, List<List<int[]>> adj) {
        int[] key = new int[V];       // minimum weight edge to connect to MST
        boolean[] inMST = new boolean[V];
        int[] parent = new int[V];

        Arrays.fill(key, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        key[0] = 0; // start from node 0

        int totalWeight = 0;

        for (int count = 0; count < V; count++) {
            // Find vertex with minimum key not yet in MST
            int u = -1;
            for (int v = 0; v < V; v++) {
                if (!inMST[v] && (u == -1 || key[v] < key[u])) {
                    u = v;
                }
            }

            inMST[u] = true;
            totalWeight += key[u];

            // Update keys of neighbors
            for (int[] edge : adj.get(u)) {
                int v = edge[0], w = edge[1];
                if (!inMST[v] && w < key[v]) {
                    key[v] = w;
                    parent[v] = u;
                }
            }
        }

        // Print MST edges
        System.out.println("    MST Edges (Naive):");
        for (int i = 1; i < V; i++) {
            System.out.println("      " + parent[i] + " -- " + i + " (w=" + key[i] + ")");
        }

        return totalWeight;
    }

    // ============================================================
    // APPROACH 2: PRIM'S WITH MIN-HEAP (Optimal for Sparse)
    // Time: O(E log V)  |  Space: O(V + E)
    //
    // Use a priority queue to efficiently find the minimum edge.
    // Lazy deletion: skip stale entries (nodes already in MST).
    // ============================================================
    public static int primsHeap(int V, List<List<int[]>> adj) {
        boolean[] inMST = new boolean[V];
        // PQ stores: [weight, node]
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{0, 0}); // (weight=0, start node=0)

        int totalWeight = 0;
        int edgesAdded = 0;

        while (!pq.isEmpty() && edgesAdded < V) {
            int[] top = pq.poll();
            int weight = top[0], node = top[1];

            if (inMST[node]) continue; // skip stale entry

            inMST[node] = true;
            totalWeight += weight;
            edgesAdded++;

            for (int[] edge : adj.get(node)) {
                int v = edge[0], w = edge[1];
                if (!inMST[v]) {
                    pq.offer(new int[]{w, v});
                }
            }
        }

        return totalWeight;
    }

    // ============================================================
    // APPROACH 3: PRIM'S WITH MST EDGE COLLECTION
    // Time: O(E log V)  |  Space: O(V + E)
    //
    // Same as Approach 2 but collects the actual MST edges.
    // PQ stores: [weight, node, parent]
    // ============================================================
    public static int primsWithEdges(int V, List<List<int[]>> adj,
                                      List<int[]> mstEdges) {
        boolean[] inMST = new boolean[V];
        // PQ stores: [weight, node, parent]
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
        pq.offer(new int[]{0, 0, -1});

        int totalWeight = 0;

        while (!pq.isEmpty()) {
            int[] top = pq.poll();
            int weight = top[0], node = top[1], parent = top[2];

            if (inMST[node]) continue;

            inMST[node] = true;
            totalWeight += weight;

            if (parent != -1) {
                mstEdges.add(new int[]{parent, node, weight});
            }

            for (int[] edge : adj.get(node)) {
                int v = edge[0], w = edge[1];
                if (!inMST[v]) {
                    pq.offer(new int[]{w, v, node});
                }
            }
        }

        return totalWeight;
    }

    // ============================================================
    // HELPER: Build weighted undirected adjacency list
    // edges[i] = {u, v, weight}
    // ============================================================
    private static List<List<int[]>> buildGraph(int V, int[][] edges) {
        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            adj.get(e[0]).add(new int[]{e[1], e[2]});
            adj.get(e[1]).add(new int[]{e[0], e[2]});
        }
        return adj;
    }

    public static void main(String[] args) {
        System.out.println("=== Minimum Spanning Tree (Prim's) ===\n");

        // Test 1:
        //     (1)
        // 0 ------- 1
        // |  \      |
        // (4) (3)  (2)
        // |    \    |
        // 3 ------- 2
        //     (5)
        //
        // MST: 0-1(1), 1-2(2), 0-3(4) = 7
        int[][] edges1 = {{0,1,1}, {0,2,3}, {0,3,4}, {1,2,2}, {2,3,5}};
        List<List<int[]>> adj1 = buildGraph(4, edges1);

        System.out.println("Test 1:");
        System.out.println("  Naive: " + primsNaive(4, adj1));

        adj1 = buildGraph(4, edges1);
        System.out.println("  Heap:  " + primsHeap(4, adj1));

        adj1 = buildGraph(4, edges1);
        List<int[]> mstEdges1 = new ArrayList<>();
        int w1 = primsWithEdges(4, adj1, mstEdges1);
        System.out.println("  With edges: weight=" + w1);
        for (int[] e : mstEdges1) {
            System.out.println("    " + e[0] + " -- " + e[1] + " (w=" + e[2] + ")");
        }

        // Test 2: Triangle
        //   0 --(10)-- 1
        //    \        /
        //    (6)   (5)
        //      \  /
        //       2
        //
        // MST: 0-2(6), 1-2(5) = 11
        int[][] edges2 = {{0,1,10}, {0,2,6}, {1,2,5}};
        List<List<int[]>> adj2 = buildGraph(3, edges2);
        System.out.println("\nTest 2 (triangle):");
        System.out.println("  Heap: " + primsHeap(3, adj2));

        // Test 3: Larger graph
        //   0 --(4)-- 1 --(8)-- 2
        //   |         |  \      |
        //  (8)      (11) (7)  (2)
        //   |         |    \    |
        //   3 --(7)-- 4 --(4)- 5
        int[][] edges3 = {
            {0,1,4}, {0,3,8}, {1,2,8}, {1,4,11},
            {1,5,7}, {2,5,2}, {3,4,7}, {4,5,4}
        };
        List<List<int[]>> adj3 = buildGraph(6, edges3);
        List<int[]> mstEdges3 = new ArrayList<>();
        int w3 = primsWithEdges(6, adj3, mstEdges3);
        System.out.println("\nTest 3 (6 nodes):");
        System.out.println("  Weight: " + w3);
        System.out.println("  Edges:");
        for (int[] e : mstEdges3) {
            System.out.println("    " + e[0] + " -- " + e[1] + " (w=" + e[2] + ")");
        }

        // Test 4: Single node
        List<List<int[]>> adj4 = buildGraph(1, new int[][]{});
        System.out.println("\nTest 4 (single node): " + primsHeap(1, adj4));
    }
}
