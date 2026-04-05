/**
 * Problem: Detect Cycle in Undirected Graph using BFS
 * Difficulty: MEDIUM | XP: 25
 *
 * Given an adjacency list for an undirected graph with V vertices and E edges,
 * detect whether the graph contains a cycle or not.
 *
 * @author DSA_Nova
 */

import java.util.*;

public class Solution {

    // ============================================================
    // APPROACH 1: BRUTE FORCE (Edge-list back-edge check via BFS)
    // Time: O(V + E)  |  Space: O(V + E)
    // ============================================================
    static class BruteForce {
        public boolean isCycle(int V, ArrayList<ArrayList<Integer>> adj) {
            boolean[] visited = new boolean[V];

            for (int start = 0; start < V; start++) {
                if (visited[start]) continue;
                // BFS: queue stores (node, parent)
                Queue<int[]> queue = new LinkedList<>();
                queue.offer(new int[]{start, -1});
                visited[start] = true;

                while (!queue.isEmpty()) {
                    int[] curr = queue.poll();
                    int node = curr[0], parent = curr[1];

                    for (int neighbor : adj.get(node)) {
                        if (!visited[neighbor]) {
                            visited[neighbor] = true;
                            queue.offer(new int[]{neighbor, node});
                        } else if (neighbor != parent) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    // ============================================================
    // APPROACH 2: OPTIMAL (BFS with parent tracking)
    // Time: O(V + E)  |  Space: O(V)
    // ============================================================
    static class Optimal {
        public boolean isCycle(int V, ArrayList<ArrayList<Integer>> adj) {
            boolean[] visited = new boolean[V];

            for (int i = 0; i < V; i++) {
                if (!visited[i]) {
                    if (bfsCheck(i, adj, visited)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean bfsCheck(int src, ArrayList<ArrayList<Integer>> adj, boolean[] visited) {
            Queue<int[]> queue = new LinkedList<>();
            queue.offer(new int[]{src, -1});
            visited[src] = true;

            while (!queue.isEmpty()) {
                int[] curr = queue.poll();
                int node = curr[0], parent = curr[1];

                for (int neighbor : adj.get(node)) {
                    if (!visited[neighbor]) {
                        visited[neighbor] = true;
                        queue.offer(new int[]{neighbor, node});
                    } else if (neighbor != parent) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    // ============================================================
    // APPROACH 3: BEST (BFS with parent array - clean version)
    // Time: O(V + E)  |  Space: O(V)
    // ============================================================
    static class Best {
        public boolean isCycle(int V, ArrayList<ArrayList<Integer>> adj) {
            boolean[] visited = new boolean[V];
            int[] parent = new int[V];
            Arrays.fill(parent, -1);

            for (int start = 0; start < V; start++) {
                if (visited[start]) continue;
                visited[start] = true;
                Queue<Integer> queue = new LinkedList<>();
                queue.offer(start);

                while (!queue.isEmpty()) {
                    int node = queue.poll();
                    for (int neighbor : adj.get(node)) {
                        if (!visited[neighbor]) {
                            visited[neighbor] = true;
                            parent[neighbor] = node;
                            queue.offer(neighbor);
                        } else if (parent[node] != neighbor) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Detect Cycle in Undirected BFS ===\n");

        // Test 1: cycle 0-1-2-0
        ArrayList<ArrayList<Integer>> adj1 = new ArrayList<>();
        adj1.add(new ArrayList<>(Arrays.asList(1, 2)));
        adj1.add(new ArrayList<>(Arrays.asList(0, 2)));
        adj1.add(new ArrayList<>(Arrays.asList(0, 1)));

        BruteForce bf = new BruteForce();
        Optimal opt = new Optimal();
        Best best = new Best();

        System.out.println("Test 1 (cycle): Expected true");
        System.out.println("  Brute:   " + bf.isCycle(3, adj1));
        System.out.println("  Optimal: " + opt.isCycle(3, adj1));
        System.out.println("  Best:    " + best.isCycle(3, adj1));

        // Test 2: tree (no cycle) 0-1, 0-2
        ArrayList<ArrayList<Integer>> adj2 = new ArrayList<>();
        adj2.add(new ArrayList<>(Arrays.asList(1, 2)));
        adj2.add(new ArrayList<>(Arrays.asList(0)));
        adj2.add(new ArrayList<>(Arrays.asList(0)));

        bf = new BruteForce();
        opt = new Optimal();
        best = new Best();

        System.out.println("\nTest 2 (no cycle): Expected false");
        System.out.println("  Brute:   " + bf.isCycle(3, adj2));
        System.out.println("  Optimal: " + opt.isCycle(3, adj2));
        System.out.println("  Best:    " + best.isCycle(3, adj2));

        // Test 3: single node
        ArrayList<ArrayList<Integer>> adj3 = new ArrayList<>();
        adj3.add(new ArrayList<>());

        bf = new BruteForce();
        opt = new Optimal();
        best = new Best();

        System.out.println("\nTest 3 (single node): Expected false");
        System.out.println("  Brute:   " + bf.isCycle(1, adj3));
        System.out.println("  Optimal: " + opt.isCycle(1, adj3));
        System.out.println("  Best:    " + best.isCycle(1, adj3));
    }
}
