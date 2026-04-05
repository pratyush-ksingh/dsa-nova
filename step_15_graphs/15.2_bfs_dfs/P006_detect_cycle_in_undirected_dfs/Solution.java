/**
 * Problem: Detect Cycle in Undirected Graph using DFS
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
    // APPROACH 1: BRUTE FORCE (BFS-based cycle detection)
    // Time: O(V + E)  |  Space: O(V)
    // ============================================================
    static class BruteForce {
        public boolean isCycle(int V, ArrayList<ArrayList<Integer>> adj) {
            boolean[] visited = new boolean[V];

            for (int start = 0; start < V; start++) {
                if (visited[start]) continue;
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
    // APPROACH 2: OPTIMAL (DFS with parent tracking)
    // Time: O(V + E)  |  Space: O(V)
    // ============================================================
    static class Optimal {
        public boolean isCycle(int V, ArrayList<ArrayList<Integer>> adj) {
            boolean[] visited = new boolean[V];

            for (int i = 0; i < V; i++) {
                if (!visited[i]) {
                    if (dfs(i, -1, adj, visited)) {
                        return true;
                    }
                }
            }
            return false;
        }

        private boolean dfs(int node, int parent, ArrayList<ArrayList<Integer>> adj, boolean[] visited) {
            visited[node] = true;

            for (int neighbor : adj.get(node)) {
                if (!visited[neighbor]) {
                    if (dfs(neighbor, node, adj, visited)) {
                        return true;
                    }
                } else if (neighbor != parent) {
                    return true;
                }
            }
            return false;
        }
    }

    // ============================================================
    // APPROACH 3: BEST (Union-Find with path compression)
    // Time: O(V + E * alpha(V)) ~ O(V + E)  |  Space: O(V)
    // ============================================================
    static class Best {
        int[] parent, rank;

        public boolean isCycle(int V, ArrayList<ArrayList<Integer>> adj) {
            parent = new int[V];
            rank = new int[V];
            for (int i = 0; i < V; i++) {
                parent[i] = i;
            }

            for (int u = 0; u < V; u++) {
                for (int v : adj.get(u)) {
                    if (u < v) {  // process each edge once
                        if (!union(u, v)) {
                            return true;  // cycle detected
                        }
                    }
                }
            }
            return false;
        }

        private int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);  // path compression
            }
            return parent[x];
        }

        private boolean union(int x, int y) {
            int rootX = find(x), rootY = find(y);
            if (rootX == rootY) return false;  // already connected => cycle

            if (rank[rootX] < rank[rootY]) {
                parent[rootX] = rootY;
            } else if (rank[rootX] > rank[rootY]) {
                parent[rootY] = rootX;
            } else {
                parent[rootY] = rootX;
                rank[rootX]++;
            }
            return true;
        }
    }

    public static void main(String[] args) {
        System.out.println("=== Detect Cycle in Undirected DFS ===\n");

        // Test 1: cycle 0-1-2-0
        ArrayList<ArrayList<Integer>> adj1 = new ArrayList<>();
        adj1.add(new ArrayList<>(Arrays.asList(1, 2)));
        adj1.add(new ArrayList<>(Arrays.asList(0, 2)));
        adj1.add(new ArrayList<>(Arrays.asList(0, 1)));

        BruteForce bf = new BruteForce();
        Optimal opt = new Optimal();
        Best best = new Best();

        System.out.println("Test 1 (cycle): Expected true");
        System.out.println("  BFS:        " + bf.isCycle(3, adj1));
        System.out.println("  DFS:        " + opt.isCycle(3, adj1));
        System.out.println("  Union-Find: " + best.isCycle(3, adj1));

        // Test 2: tree 0-1, 0-2
        ArrayList<ArrayList<Integer>> adj2 = new ArrayList<>();
        adj2.add(new ArrayList<>(Arrays.asList(1, 2)));
        adj2.add(new ArrayList<>(Arrays.asList(0)));
        adj2.add(new ArrayList<>(Arrays.asList(0)));

        bf = new BruteForce();
        opt = new Optimal();
        best = new Best();

        System.out.println("\nTest 2 (no cycle): Expected false");
        System.out.println("  BFS:        " + bf.isCycle(3, adj2));
        System.out.println("  DFS:        " + opt.isCycle(3, adj2));
        System.out.println("  Union-Find: " + best.isCycle(3, adj2));

        // Test 3: single node
        ArrayList<ArrayList<Integer>> adj3 = new ArrayList<>();
        adj3.add(new ArrayList<>());

        bf = new BruteForce();
        opt = new Optimal();
        best = new Best();

        System.out.println("\nTest 3 (single node): Expected false");
        System.out.println("  BFS:        " + bf.isCycle(1, adj3));
        System.out.println("  DFS:        " + opt.isCycle(1, adj3));
        System.out.println("  Union-Find: " + best.isCycle(1, adj3));
    }
}
